package com.snl.swing.practice01.entity.weapon.gun;

import com.snl.swing.practice01.Music;
import com.snl.swing.practice01.state.InputState;

public class SubmachineGun extends Gun {

    public SubmachineGun(int maxBullets,String name) {
        this(15,20,20,20,name,maxBullets);
    }

    public SubmachineGun(double originShootSpeed, double originHearingRangle,
                         int originRecoil, double originKillDamage, String name, int maxBullets) {
        super(originShootSpeed, originHearingRangle, originRecoil, originKillDamage, name, maxBullets);
    }

    @Override
    public void attack(InputState state) {
        super.attack(state); //这一步干了三件事情,调用shot方法，将子弹从子弹夹中取出
        Music.subMacheingShoot();
    }
}
