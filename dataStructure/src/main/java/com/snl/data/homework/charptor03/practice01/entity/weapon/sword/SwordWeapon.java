package com.snl.data.homework.charptor03.practice01.entity.weapon.sword;

import com.snl.data.homework.charptor03.practice01.entity.weapon.Weapon;
import com.snl.data.homework.charptor03.practice01.entity.weapon.WeaponType;

import java.awt.*;

//剑武器
public interface SwordWeapon extends Weapon {

    @Override
    default WeaponType getWeaponType() {
        return WeaponType.SWORD;
    }
}
