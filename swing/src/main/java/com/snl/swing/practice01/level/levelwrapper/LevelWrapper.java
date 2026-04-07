package com.snl.swing.practice01.level.levelwrapper;

import com.snl.swing.practice01.entity.player.Player;

/**
 * 该接口封装了关卡
 */
public interface LevelWrapper {

    /**
     * 获取当前关卡索引
     * @return 当前关卡数量
     */
     int getLevel();

    /**
     * 是否还有下一关？
     * @return 存在下一关，返true，否则返回false
     */
     boolean hasNext();

    /**
     * 是否有有上一关
     * @return 存在上一关，返回{@code true} ，否则返回 {@code false}
     */
     boolean hasPrevious();

    /**
     * 前进到下一关
     */
    void next();

    /**
     * 回退到上一关
     */
    void previous();

    /**
     * 获取玩家角色
     * @return 玩家角色
     */
    Player getPlayer();

    /**
     * 全部的关卡
     * @return 全部的关卡
     */
    int totalLevels();
}
