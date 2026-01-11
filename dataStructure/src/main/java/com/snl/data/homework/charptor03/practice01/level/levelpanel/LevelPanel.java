package com.snl.data.homework.charptor03.practice01.level.levelpanel;

import com.snl.data.homework.charptor03.practice01.text.Charpter;
import com.snl.data.homework.charptor03.practice01.text.Text;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public abstract class LevelPanel extends JPanel {

    /**
     * 当前关卡
     */
    private int level;
    /**
     * 背景颜色
     */
    private Color color ;

    /**
     * 生命长度
     */

    private final long LIFE = 3_000_000_000L;

    private long bornTime;

    private long now;

    private Text charpter;

    private JLabel label;

    public LevelPanel() {
        level = 0;
        initData();
    }

    public LevelPanel(int level) {
        this.level = level;
        initData();
    }

    private void initData() {
        color = Color.black;
        charpter = new Charpter();
        label = new JLabel(charpter.getString(0),JLabel.CENTER);
        label.setForeground(Color.PINK);
        label.setFont(new Font("隶书",Font.BOLD,30));

        bornTime = System.nanoTime();
        setBackground(color);
    }

    public void reset() {
        level = 0;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600,500);
    }

    public boolean isDead() {
        now = System.nanoTime();
        return now - bornTime >= LIFE;
    }

    public String getContent() {
        return charpter.getString(level);
    }

}
