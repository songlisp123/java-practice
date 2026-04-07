package com.snl.swing.practice01.entity.weapon.knife;

public class SmallKnife extends AbstractKnife {

    public SmallKnife(double xPos,double yPos,String name) {
        this(xPos,yPos,10.2,5,36,
                3,6,3,13,3);
        super.setName(name);
    }

    public SmallKnife(double xPos, double yPos, double originKillDamage,
                      double originalSpeed, double currentDurability, double hitHeight,
                      double hitWidth, double height, double slideWidth, double slideHeight) {
        super(xPos, yPos, originKillDamage, originalSpeed, currentDurability, hitHeight, hitWidth, height, slideWidth, slideHeight);
    }
}
