package com.snl.data.homework.charptor03.practice01.level;

import com.snl.data.homework.charptor03.practice01.entity.Sprite;
import com.snl.data.homework.charptor03.practice01.entity.player.Player;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;

/**
 * 关卡类，维护了当前的关卡元素
 */
public class GameLevelImplement {

    int level;
    int maxLevel;
    private Player player;
    Level<Sprite> panel;

    public GameLevelImplement(int maxLevel) {
        this.maxLevel = maxLevel;
        initData();
    }

    private void initData() {
        level = 0;
        player = new Player(10,10,20,20);
        panel = new LevelImplement<>(player);
    }

    public void update() {
        if (level >= maxLevel -1)
            level = -1;
        level++;
        if (level == 0)
        {
            panel = new LevelImplement<>(player);
        }else {
            panel = new GamelLevel02<>(player);
        }
    }

    public int getLevel() {
        return level;
    }

    public void render(Graphics g) {
        panel.render(g);
    }

    public void update(double delta, InputState state, int weight, int height) {
        panel.update(delta, state, weight, height);
    }

    public boolean isCrash() {
        return panel.isCrash();
    }

    public boolean completed() {
        return panel.completed();
    }

    public void reset() {
        panel.reset();
    }
}
