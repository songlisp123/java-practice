package com.snl.data.homework.charptor03.practice01.entity.weapon.gun;

import com.snl.data.homework.charptor03.practice01.CONSTANTS.GameConstants;

public class Pistol extends Gun {

    public Pistol(int maxBullets,String name) {
        this(
//                GameConstants.PISTOL_ORIGIN_SHOOT_SPEED,
                8.0,
                GameConstants.PISTOL_ORIGIN_HEARING_RANGE,
                GameConstants.PISTOL_ORIGIN_RECOIL,
                GameConstants.PISTOL_ORIGIN_KILL_DAMAGE,
                name,
                maxBullets
        );
    }

    public Pistol(double originShootSpeed, double originHearingRangle,
                  int originRecoil, double originKillDamage, String name, int maxBullets) {
        super(originShootSpeed, originHearingRangle, originRecoil, originKillDamage, name, maxBullets);
    }

}
