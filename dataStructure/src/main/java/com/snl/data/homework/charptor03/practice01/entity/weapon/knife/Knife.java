package com.snl.data.homework.charptor03.practice01.entity.weapon.knife;

import com.snl.data.homework.charptor03.practice01.entity.weapon.WeaponType;
import com.snl.data.homework.charptor03.practice01.entity.weapon.sword.SwordWeapon;

public interface Knife extends SwordWeapon {

    @Override
    default WeaponType getWeaponType() {
        return WeaponType.KNIFE;
    }

}
