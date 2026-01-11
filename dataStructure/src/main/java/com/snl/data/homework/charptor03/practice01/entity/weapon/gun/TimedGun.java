package com.snl.data.homework.charptor03.practice01.entity.weapon.gun;

import com.snl.data.homework.charptor03.practice01.entity.Sprite;

public class TimedGun extends Gun {


    public TimedGun(int maxBullets,String name) {
        this(2,20,
                20,20,name,maxBullets);
    }

    public TimedGun(double originShootSpeed, double originHearingRangle,
                    int originRecoil, double originKillDamage, String name, int maxBullets) {
        super(originShootSpeed, originHearingRangle, originRecoil, originKillDamage, name, maxBullets);
    }

    @Override
    public void attack(Sprite sprite) {
        super.attack(sprite);
        //播放音乐
    }
}
