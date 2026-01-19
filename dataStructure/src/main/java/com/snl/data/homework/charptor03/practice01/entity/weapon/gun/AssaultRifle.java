package com.snl.data.homework.charptor03.practice01.entity.weapon.gun;

import com.snl.data.homework.charptor03.practice01.Music;
import com.snl.data.homework.charptor03.practice01.entity.Sprite;
import com.snl.data.homework.charptor03.practice01.state.InputState;

public class AssaultRifle extends Gun {


    public AssaultRifle(int maxBullets,String name) {
        this(12.5,56,
                875,32,name,maxBullets);
    }

    public AssaultRifle(double originShootSpeed, double originHearingRangle,
                        int originRecoil, double originKillDamage, String name, int maxBullets) {
        super(originShootSpeed, originHearingRangle, originRecoil, originKillDamage, name, maxBullets);
    }

//    @Override
//    public void attack(InputState state) {
//        super.attack(state);
//        Music.assaultShoot();
//    }
}