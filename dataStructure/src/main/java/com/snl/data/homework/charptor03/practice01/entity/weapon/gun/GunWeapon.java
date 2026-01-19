package com.snl.data.homework.charptor03.practice01.entity.weapon.gun;

import com.snl.data.homework.charptor03.practice01.Music;
import com.snl.data.homework.charptor03.practice01.entity.Sprite;
import com.snl.data.homework.charptor03.practice01.entity.booms.Boom;
import com.snl.data.homework.charptor03.practice01.entity.weapon.Weapon;
import com.snl.data.homework.charptor03.practice01.entity.weapon.WeaponType;
import com.snl.data.homework.charptor03.practice01.state.InputState;

public interface GunWeapon extends Weapon {

    default WeaponType getWeaponType() {
        return WeaponType.GUN;
    }

    /**
     * 获取枪械里面的子弹
     * @return 当前枪械里面的子弹
     */
    int getBullets();

    /**
     * 获取当前枪械的最大容量子弹
     * @return 枪械的最大容量子弹
     */
    int maxBullets();

    @Override
    void attack(InputState state);

    @Override
    default void reset() {
        //由于这个方法主要是碰撞后恢复物体原来的位置的，所以默认不实现
    }
    //枪械独有的属性、方法
    /**
     * 枪械射速
     * @return 当前枪械射速
     * @apiNote 注意：射速是可以随着增加配件增加的
     */
    double shootSpeed();

    /**
     * 返回当前裸枪的原始射速
     * @return 原始射速
     */
    double originShootSpeed();

    /**
     * 射击听觉范围
     * @return 返回当前枪械的射击听觉范围
     * @apiNote 注意：可以通过增加配件减少此值
     */
    double hearingrange();

    /**
     * 原始听觉范围
     * @return 原始枪械射击时的听觉范围
     */
    double originHearingrange();

    /**
     * 枪或类似装置在发射时产生的向后作用力
     * @return 枪械后坐力
     * @apiNote 注意：可以通过增加配件减少此值
     */
    int recoil();

    /**
     * 无配饰下的后坐力
     * @return 原始后坐力
     */
    int originRecoil();

    boolean isEmpty();
}
