package com.snl.swing.practice01.level.levelwrapper;

import com.snl.swing.practice01.entity.Sprite;
import com.snl.swing.practice01.entity.player.Player;
import com.snl.swing.practice01.level.levelMapImplement;
import com.snl.swing.practice01.level.map.JumpLevelMap;
import com.snl.swing.practice01.level.Level;
import com.snl.swing.practice01.state.InputState;

import java.awt.*;

public abstract class AbstractLevelWrapper implements LevelWrapper {

    /**
     * 当前关卡索引
     */
    int currentLevel;

    /**
     * 最大关卡数量
     */
    int maxLevel;

    /**
     * 当前关卡
     */
    Level<Sprite> level;


    public AbstractLevelWrapper() {
        initData();
    }

    private void initData() {
        currentLevel = 0;
        maxLevel = JumpLevelMap.getAllLevels();
        level = new levelMapImplement<>(currentLevel);
    }

    @Override
    public int getLevel() {
        return currentLevel;
    }

    @Override
    public boolean hasNext() {
        return currentLevel  < maxLevel-1;
    }

    @Override
    public boolean hasPrevious() {
        return currentLevel > 0;
    }

    @Override
    public void next() {
        if (!hasNext())
            return;
        currentLevel++;
        //这里要做什么？
//        level =
        level = new levelMapImplement<>(currentLevel);
    }

    @Override
    public void previous() {
        if (!hasPrevious())
            return;
        currentLevel--;
        level = new levelMapImplement<>(currentLevel);
    }

    @Override
    public Player getPlayer() {
        return level.getPlayer();
    }

    @Override
    public int totalLevels() {
        return maxLevel;
    }

    //渲染关卡内的所有精灵
    public void render(Graphics g) {
        level.render(g);
    }

    public void update(double delta, InputState state, int weight, int height) {
        level.update(delta, state, weight, height);
    }

    public boolean isCrash() {
        return level.isCrash();
    }

    //当前关卡是否通关
    public boolean completed() {
        return level.completed();
    }

    //重置改关卡内的所有精灵（不包括状态）
    public void reset() {
        level.reset();
    }

}
