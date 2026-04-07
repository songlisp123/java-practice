package com.snl.swing.practice01.level;

import com.snl.swing.practice01.entity.Sprite;
import com.snl.swing.practice01.entity.player.Player;
import com.snl.swing.practice01.state.InputState;

import java.awt.*;

/**
 * 实现类必须维护该关卡内的所有元素
 * 这其中包括：已存在的，死亡的精灵元素
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
     * 更新该关卡的所有精灵元素(不包括已损坏精灵元素）
     */
    void update(double delta , InputState state,int weight,int height);

    /**
     * 关卡内玩家元素是否相撞
     * @return 如果玩家与其他元素相撞，则返回true；否则返回false；
     */
    boolean isCrash();

    /**
     * 当前关卡是否完成
     * @return 关卡完成与否
     */
    boolean completed();

    /**
     * 填充当前关卡的精灵
     */
    void fillSprite(int index);

}
