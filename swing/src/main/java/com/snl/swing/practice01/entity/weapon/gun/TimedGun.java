package com.snl.swing.practice01.entity.weapon.gun;

import com.snl.swing.practice01.state.InputState;

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
    public void attack(InputState state) {
        super.attack(state);
        //播放音乐
    }
}
