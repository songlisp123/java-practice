package com.snl.data.homework.charptor03.practice01.entity.weapon.sword;

import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;

public class SmallSword extends SimpleSword {

    public SmallSword(double xPos,double yPos,String name) {
        this(xPos,yPos,20,20,20,
                8, 4,2,20,6,4);
        super.setName(name);
    }

    public SmallSword(double xPos, double yPos, double originKillDamage,
                      double currentDurability, double originalSpeed,
                      double swordHitWeight, double swordHitHeight, double height,
                      double swordBladeWeight, double swordBladeHeight, double swordTipWeight) {
        super(xPos, yPos, originKillDamage, currentDurability,
                originalSpeed, swordHitWeight, swordHitHeight,
                height, swordBladeWeight, swordBladeHeight, swordTipWeight);
    }

    @Override
    public void paint(Graphics g, InputState state) {
        super.paint(g, state);
    }

}
