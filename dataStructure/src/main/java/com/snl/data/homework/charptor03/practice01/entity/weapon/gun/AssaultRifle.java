package com.snl.data.homework.charptor03.practice01.entity.weapon.gun;

import com.snl.data.homework.charptor03.practice01.Music;
import com.snl.data.homework.charptor03.practice01.entity.Sprite;

public class AssaultRifle extends Gun {


    public AssaultRifle(int maxBullets,String name) {
        this(12.5,56,875,32,name,maxBullets);
    }

    public AssaultRifle(double originShootSpeed, double originHearingRangle,
                        int originRecoil, double originKillDamage, String name, int maxBullets) {
        super(originShootSpeed, originHearingRangle, originRecoil, originKillDamage, name, maxBullets);
    }

    @Override
    public void attack(Sprite sprite) {
        //装填逻辑可以判断为,当前的弹药架空时，进行装填逻辑
        super.attack(sprite); //这一步干了三件事情,调用shot方法，将子弹从子弹夹中取出
        Music.assaultShoot();
//        long shoot = System.currentTimeMillis();
//        setShootTime(shoot);
//        setReload(true);
    }
}
