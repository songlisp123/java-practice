package com.snl.data.homework.charptor03.practice01.entity.player;

import com.snl.data.homework.charptor03.practice01.entity.Group;
import com.snl.data.homework.charptor03.practice01.entity.goods.AbstractGoods;
import com.snl.data.homework.charptor03.practice01.state.InputState;

public interface PlayerAction {


    /**
     * 攻击行为
     * @param state 当前输入状态
     */
    void attack(InputState state);

    /**
     * 获取硬币等游戏物体
     */
//    void gainGoods();

    void update(double delta, InputState state, Group aGroup, Group destory, Group wall, Group<AbstractGoods> goods);
}
