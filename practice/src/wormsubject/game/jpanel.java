package wormsubject.game;

import wormsubject.gameElement.Pair;
import wormsubject.gameElement.Wall;
import wormsubject.gameElement.Warms;
import wormsubject.util.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.logging.Logger;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class jpanel extends JPanel {
    public static int rows;
    public static int columns;
    public int width;
    public int height;
    private Warms warm;
    private Pair food;
    private JTextField jTextField;
    private JLabel jLabel;
    private JLabel jLabel02;
    private CustomButton back;
    private int score;
    private List<Pair> pairs;
    private static int turn = 1;
    private Wall wall;
    private JFrame frame;
    private setPanel setPanel;
    private static final Logger logger = Logger.getLogger("worms");

    public jpanel(int rows, int columns, int width, int height) {
//        setBackground(new Color(30, 31, 34));   //界面颜色

        this.rows = rows;
        this.columns = columns;
        this.width = width;
        this.height = height;
        warm = new Warms(turn);
        wall = new Wall(turn,warm);
        Wall.tostring();
        food = createFood();
        pairs = new ArrayList<>();

        score = 0;
        jLabel = new JLabel("当前分数：[%d]分".formatted(score));
        jLabel02 = new JLabel("当前回合：第[%d]回合".formatted(turn));
        jTextField = new JTextField("当前分数：[%d]分".formatted(score));
        System.out.println(jLabel.getForeground());
        jLabel.setForeground(Color.WHITE);
        jLabel02.setForeground(Color.WHITE);

        //按钮
        back = new CustomButton("返回界面");
        back.addActionListener(event->{
            if (setPanel == null) {
                setPanel = new setPanel(600,600);
            }
            else {
                setPanel.setVisible(true);
            }
            setVisible(false);
        });
        add(back);
        //设置墙体

        //判断每个方块的大小是多少
        //取消默认背景

        setBackground(new Color(30, 34, 32));
        //创建蛇
        logger.info("创建蛇体成功");
        add(jLabel,FlowLayout.LEFT);
        add(jLabel02,FlowLayout.CENTER);

        this.action();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        var g2 = (Graphics2D) g;
        int squareSize =  width / columns;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        /*
        这是一个调节颜色的小程序
        getRed：获取红色份量（0-255）
        getGreen：获取绿色分量
        getBlue：获取蓝色分量
        getAlpha:获取透明通道
         */
        Color color = new Color(79, 227, 94);
        int R = color.getRed();
        int G = color.getGreen();
        int B = color.getBlue();
        double factor = 0.45;
        R = (int) (R * factor);
        G = (int) (G * factor);
        B = (int) (B * factor);
        color = new Color(R, G, B);
        g2.setColor(color);

        for (int i = 0; i < rows; i++) {
            g2.draw(new Line2D.Double(0, i * squareSize, width, i * squareSize));
            g2.draw(new Line2D.Double(i * squareSize, 0, i * squareSize, height));
        }

        //绘制变现点
        /*
        fillOval方法：绘制出原点，参数
        x:坐标x轴
        y:坐标y轴
        width:原点大小
        height:原点大小
         */
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                g2.fillOval(
                        (int) (j * squareSize + (double) squareSize / 2 - 1),
                        (int) (i * squareSize + (double) squareSize / 2 - 1),
                        2,
                        2
                );
            }
            paintChildren(g);
        }

        //绘制蛇体
        g2.setColor(Color.RED);
        //绘制食物
//        System.out.println(food);
        g2.fillOval(food.getX()*squareSize+5,
                food.getY()*squareSize+5,
                10,10);
        //绘制蛇体
        g2.setColor(Color.green);
        Pair[] body = warm.getBody();
        for (int i = 0; i < body.length; i++) {
            double x  = body[i].getX();
            double y = body[i].getY();
            g2.fill(new Rectangle2D.Double(x*squareSize,y*squareSize,squareSize,squareSize));
        }
        //绘制墙体
        g2.setColor(Color.red);
        List<Pair> wallBody = wall.showBody();
        for (Pair pair:wallBody) {
            double x = pair.getX();
            double y = pair.getY();
            g2.fill(new Rectangle2D.Double(
                    x*squareSize,y*squareSize,squareSize,squareSize
            ));
            g2.fill(new Rectangle2D.Double(
                    (x+1)*squareSize,y*squareSize,squareSize,squareSize
            ));

        }
        paintChildren(g);
    }

    //创建食物逻辑
    private Pair createFood() {
        int x;
        int y;
        Random r = new Random();
        while (true) {
            x = r.nextInt(rows-1);
            y = r.nextInt(columns-1);
            if (warm.contains(x, y)&&wall.contains(x,y)) {
                System.out.println("冲撞");
            }
            else break;
        }
        return new Pair(x, y);
    }

    //程序主逻辑
    private void action() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (warm.hit()) {
                    System.out.println("碰撞！当前得分【%d】-->[%d]".formatted(
                            score,0
                    ));
                    logger.warning("碰撞！当前得分【%d】-->[%d]".formatted(
                            score,0
                    ));
                    score = 0;
                    jLabel.setText("当前得分【%d】分".formatted(score));
                    turn = 1;
                    jLabel02.setText("当前回合：第[%d]回合".formatted(turn));
                    warm = new Warms(turn);
                    wall = new Wall(turn,warm);
                    food = createFood();
                } else {
                    boolean eat = warm.creep(food);
                    if (eat) {
                        System.out.println("得分【%d】-->[%d]".formatted(
                                score,score+5
                        ));
                        logger.info("得分【%d】-->[%d]".formatted(
                                score,score+5));
                        score += 5;
                        if (score%25 ==0) {
                            turn++;
                            wall = new Wall(turn,warm);
                        }
                        jLabel.setText("当前得分【%d】分".formatted(score));
                        jLabel02.setText("当前回合：第[%d]回合".formatted(turn));
                        food = createFood();
                    }
                }
                repaint();
            }
        }, 0, 1000/8);

        this.setFocusable(true);
        this.requestFocusInWindow();  // 代替 requestFocus，更稳
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                switch (key) {
                    case KeyEvent.VK_W:// �ϼ�ͷ���£�
                        creepTo(Warms.UP);
                        break;
                    case KeyEvent.VK_S:// �¼�ͷ���£�
                        creepTo(Warms.DOWN);
                        break;
                    case KeyEvent.VK_A:// ���ͷ���£�
                        creepTo(Warms.LEFT);
                        break;
                    case KeyEvent.VK_D:// �Ҽ�ͷ���£�
                        creepTo(Warms.RIGHT);
                        break;
                }
            }
        });
    }

    public void creepTo(int direction) {
        if (warm.hit(direction)) {
            score = 0;
            turn = 1;
            logger.warning("游戏结束……");
            jLabel.setText("当前得分【%d】分".formatted(score));
            jLabel02.setText("当前回合：第[%d]回合".formatted(turn));
            warm = new Warms(turn);
            wall = new Wall(turn,warm);
            food = createFood();
        } else {
            boolean eat = warm.creep(direction, food);
            if (eat) {
                score += 5;
                if (score%25 ==0) {
                    turn++;
                    wall = new Wall(turn,warm);
                }
//                System.out.println(score);
                logger.info("得分【%d】-->[%d]".formatted(
                        score,score+5));
                logger.info("恭喜您，获得宝物！");
                jLabel.setText("当前得分【%d】分".formatted(score));
                jLabel02.setText("当前回合：第[%d]回合".formatted(turn));
                food = createFood();
            }
        }
        repaint();
    }

}

