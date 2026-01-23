package com.snl.data.homework.charptor03.practice01.entity.weapon;


import com.snl.data.homework.charptor03.practice01.entity.Group;
import com.snl.data.homework.charptor03.practice01.entity.goods.AbstractGoods;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;

/**
 * 武器系统
 */
public interface Weapon {

    /**
     * 获取武器类型
     * @return 当前武器类型
     */
    WeaponType getWeaponType();

    /**
     * 使用武器攻击
     */
    void attack(InputState state);

    //三幻神
    void render(Graphics g);

    /**
     * 更新武器状态
     *
     * @param xPos    x坐标，用来追随玩家
     * @param yPos    y坐标，用来追随玩家
     * @param delta   时间间隔
     * @param agroup  敌人组
     * @param destory 失败组
     * @param wall    墙壁组
     * @param goods
     */
    void update(double xPos, double yPos, double delta, Group agroup, Group destory, Group wall, Group<AbstractGoods> goods);
    void reset();

    //武器信息
    String getInfo();

    /**
     * 返回此武器的杀伤力
     * @return 该武器的杀伤力
     * @apiNote 改值可能会变动，比如升级或者换配置
     */
    double getKillDamage();

    /**
     * 获取此武器的原始杀伤力
     * @return 原始杀伤力
     */
    double getOriginalDamage();

    /**
     * 增加此武器的伤害
     * @param added 增加的伤害
     */
    void addDamage(double added);

    String getName();
}
