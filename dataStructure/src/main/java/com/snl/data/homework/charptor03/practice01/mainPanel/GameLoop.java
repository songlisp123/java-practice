package com.snl.data.homework.charptor03.practice01.mainPanel;

import com.snl.data.homework.charptor03.practice01.CONSTANTS.GameConstants;
import com.snl.data.homework.charptor03.practice01.Music;
import com.snl.data.homework.charptor03.practice01.level.*;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import javax.swing.*;
import java.awt.*;

public class GameLoop extends JPanel {

    private Timer timer;
    private InputState state;
    private LevelPanel levelPanel;
    private boolean hasBeenBooted;
    private GameLevelImplement gameLevel;

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
        timer = new Timer(12,e -> {
            update(12);
            state.attackPressed = false;
            repaint();
        });
        timer.start();
        hasBeenBooted = false;
        gameLevel = new GameLevelImplement(2);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(GameConstants.Weight,GameConstants.Height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        var g2 = (Graphics2D) g.create();
        if (!hasBeenBooted) {
            g2.scale(1.5, 1.5);
            g2.setColor(Color.red);
            if (levelPanel != null)
                g2.drawString(levelPanel.getContent(), 250, 200);
        } else {
            gameLevel.render(g);
        }

    }

    private void update(double delta) {
        if (!hasBeenBooted) {
            if (levelPanel == null)
                levelPanel = new ChapterPanel(gameLevel.getLevel());
            if (levelPanel.isDead()) {
                hasBeenBooted = true;
            }
        } else {
            if (levelPanel != null)
                levelPanel = null;
            gameLevel.update(delta, state, GameConstants.Weight, GameConstants.Height);
            if (gameLevel.isCrash())
                reset();
            if (gameLevel.completed()) {
                //关卡增加
                gameLevel.update();
                //如果通关：播放音乐
                Music.crash();
                //暂时重置
                reset();
            }
        }

    }

    private void reset() {
        gameLevel.reset();
        hasBeenBooted = false;
    }

}
