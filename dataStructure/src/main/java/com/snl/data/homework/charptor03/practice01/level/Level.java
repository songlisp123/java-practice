package com.snl.data.homework.charptor03.practice01.level;

import com.snl.data.homework.charptor03.practice01.entity.Sprite;
import com.snl.data.homework.charptor03.practice01.entity.player.Player;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;

/**
 * 实现类必须维护该关卡内的所有元素
 * 这其中包括：已存在的，死亡的精灵元素
 * (在目前的版本中，因为没有其他系统，所有只有银币能被吃到，只需要维护一个被吃掉的硬币即可)
 */
public interface Level<T extends Sprite> {

    /**
     * 重置精灵为起始点
     */
    void reset();

    /**
     * 渲染改关卡的所有精灵
     * @param g 参数
     */
    void render(Graphics g);

    /**
     * 获取该关卡的玩家
     * @return 玩家精灵
     */
    Player getPlayer();

    /**
     * 更新该关卡的所有精灵元素(不包括精灵元素）
     */
    void update(double delta , InputState state,int weight,int height);

    /**
     * 返回下一个关卡
     * @return 下一个关卡
     */
    Level<T> next();


    /**
     * 关卡内玩家元素是否相撞
     * @return 如果玩家与其他元素相撞，则返回true；否则返回false；
     */
    boolean isCrash();


    /**
     * 当前关卡是否同欢
     * @return 通关与否
     */
    boolean completed();

}
