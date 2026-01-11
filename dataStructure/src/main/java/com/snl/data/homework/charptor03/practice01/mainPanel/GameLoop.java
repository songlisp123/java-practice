package com.snl.data.homework.charptor03.practice01.mainPanel;

import com.snl.data.homework.charptor03.practice01.CONSTANTS.GameConstants;
import com.snl.data.homework.charptor03.practice01.entity.player.Player;
import com.snl.data.homework.charptor03.practice01.entity.weapon.gun.GunWeapon;
import com.snl.data.homework.charptor03.practice01.entity.weapon.Weapon;
import com.snl.data.homework.charptor03.practice01.level.levelpanel.ChapterPanel;
import com.snl.data.homework.charptor03.practice01.level.levelpanel.LevelPanel;
import com.snl.data.homework.charptor03.practice01.level.levelwrapper.GameLevelImplement;
import com.snl.data.homework.charptor03.practice01.state.GameState;
import com.snl.data.homework.charptor03.practice01.state.InputState;
import com.snl.swing.practice.button.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.logging.Logger;

public class GameLoop extends JPanel implements MouseMotionListener {

    //定时器类
    private Timer timer;
    //输入状态
    private InputState state;
    //游戏玩家
    private  Player player;
    //过度面板
    private LevelPanel levelPanel;

    /**
     * 游戏关卡封装类
     */
    private GameLevelImplement gameLevel;

    /**
     * 游戏状态
     */
    private GameState gameState;

    private JButton customButton;

    private Weapon[] weapons;

    private long start;

    private Thread gameThread;

    public static Logger logger = Logger.getLogger("game");


    public GameLoop() {
        this(null);
    }

    public GameLoop(InputState state) {
        this(state,null);
    }

    public GameLoop(InputState state , Container container) {
        this.state = state;
        initDate();
    }

    private void initDate() {
        setLayout(new BorderLayout());
        setBackground(Color.black);
        addMouseMotionListener(this);
        customButton = new CustomButton("点击我");
        customButton.addActionListener(e -> {
            GameState.stopping = false;
        });
//        customButton.setFocusPainted(false);
        customButton.setFocusable(false);
        alignSpace();
        gameLevel = new GameLevelImplement();
        player = gameLevel.getPlayer();
        weapons = player.allWeapons();
        gameState = new GameState(gameLevel,player);

        //游戏
        start = System.nanoTime();
        gameThread = new Thread(run(),"游戏线程");
        gameThread.start();

        /*
        另一种方法，但是随着项目的胖庞大而效率低下
         */
//        timer = new Timer(16,e -> {
//            update(16);
//            state.attackPressed = false;
//            repaint();
//        });
//        timer.start();
        //我需要新线程

    }

    private void alignSpace() {
        LayoutManager layout = getLayout();
        if (!(layout instanceof GridBagLayout)) {
            layout = new GridBagLayout();
            setLayout(layout);
        }
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 1;
        constraints.gridx = 4;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1.0f;
        constraints.weighty = 1.0f;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.NORTHEAST;
        constraints.insets = new Insets(50,100,0,30);
        add(customButton,constraints);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(GameConstants.Weight,GameConstants.Height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        var g2 = (Graphics2D) g.create();
        g2.setColor(Color.green);
        if (gameState.isLosing()) {
            g2.drawString("death",200,300);
            return;
        }
        if (gameState.isFinished()) {
            g2.drawString("恭喜通关",200,300);
            return;
        }

        if (GameState.stopping) {
            g2.drawString("游戏暂停",200,300);
            return;
        }
        if (!gameState.isHasBeenBooted()) {
            g2.scale(1.5, 1.5);
            g2.setColor(Color.red);
            if (levelPanel != null)
                g2.drawString(levelPanel.getContent(), 250, 200);
        } else {
            g2.drawString("当前生命值：%d".formatted(player.getLife()),
                    10,10);
            g2.drawString("当前得分：%d".formatted(player.getScore()),
                    250,10);
            g2.drawString("当前弹药：%d".formatted(((GunWeapon)player.getCurrentWeapon()).getBullets()),
                    600,10);
            for (int i=0;i<weapons.length;i++) {
                Weapon currentWeapon = player.getCurrentWeapon();
                if (currentWeapon == weapons[i]) {
                    g2.setColor(Color.green);
                    g2.drawString(currentWeapon.getInfo() + "   ✅",10,(i + 1) * 40);
                }else {
                    g2.setColor(Color.WHITE);
                    g2.drawString(weapons[i].getInfo(),10,(i + 1) * 40);
                }

            }
            gameLevel.render(g);
        }
        g2.dispose();
    }

    private void update(double delta) {
        if (gameState.isLosing()) {
            //失败的蛮
            logger.warning("游戏结束！");
        }else {
            //游戏正在运行
            if (!gameState.isHasBeenBooted()) {
                //过度关卡
                if (levelPanel == null)
                    levelPanel = new ChapterPanel(gameLevel.getLevel());
                if (levelPanel.isDead()) {
                    //
                    gameState.setHasBeenBooted(true);
                }
            } else {
                if (GameState.stopping) {
                    //游戏暂停？？
                    logger.info("游戏暂停");
                }else {
                    //更新游戏
                    if (levelPanel != null)
                        levelPanel = null;
                    gameLevel.update(delta, state, GameConstants.Weight, GameConstants.Height);
                    gameState.update();
                }
            }
        }
    }

    private Runnable run() {
        return () ->{
            logger.info("开始运行游戏……");
            final double frame = 16000000.0;
            while (!Thread.currentThread().isInterrupted()) {
                //如果当前线程没有卡死
                long now = System.nanoTime();
                if (now - start >= frame) {
                    start = now;

                    update(16);
                    state.attackPressed = false;
                    /*
                    Ai提示：不能再在子线程调用事件分配线程
                     */
                    SwingUtilities.invokeLater(this::repaint);
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    break;
                }
            }
        };
    }


    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        System.out.println("移动鼠标");
    }
}
