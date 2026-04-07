package com.snl.swing.practice01.entity.weapon.gun;

import com.snl.swing.practice01.CONSTANTS.GameConstants;
import com.snl.swing.practice01.Music;
import com.snl.swing.practice01.state.InputState;

import java.util.logging.Logger;

public class Pistol extends Gun {

    private static Logger logger = Logger.getLogger("game");

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

    @Override
    public void attack(InputState state) {
        super.attack(state); //这一步干了三件事情,调用shot方法，将子弹从子弹夹中取出
        Music.pistolShoot();
    }
}

