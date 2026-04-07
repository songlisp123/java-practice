package com.snl.swing.practice01.entity.weapon.sword;

import com.snl.swing.practice01.entity.weapon.Weapon;
import com.snl.swing.practice01.entity.weapon.WeaponType;

//剑武器
public interface SwordWeapon extends Weapon {

    @Override
    default WeaponType getWeaponType() {
        return WeaponType.SWORD;
    }

    /**
     * 获取耐久度，当耐久度为0时。武器损坏
     * @return 当前武器的耐久度
     */
    double getDurability();

    /**
     * 获取攻速
     * @return 攻速
     */
    double getSpeed();
}
