package com.snl.swing.practice01.entity.weapon.gun;

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