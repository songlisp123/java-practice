package com.snl.data.homework.charptor03.practice01.entity.player;

import com.snl.data.homework.charptor03.practice01.entity.Group;
import com.snl.data.homework.charptor03.practice01.entity.booms.BoomGroup;
import com.snl.data.homework.charptor03.practice01.state.InputState;

public interface PlayerAction {

    /**
     * 发射子弹
     * @param group 子弹组
     */
    void shoot(BoomGroup group, InputState state);

    default void shoot(BoomGroup group) {
        shoot(group,null);
    }

    /**
     * 获取硬币等游戏物体
     */
//    void gainGoods();

    void update(double delta, InputState state, Group aGroup, Group destory,Group wall);
}
