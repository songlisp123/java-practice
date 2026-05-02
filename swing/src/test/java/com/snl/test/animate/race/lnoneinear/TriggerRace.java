package com.snl.test.animate.race.lnoneinear;

import org.jdesktop.animation.timing.triggers.ActionTrigger;

import javax.swing.*;

public class TriggerRace extends NonLinearRace {
    /**
     * Creates a new instance of NonLinearRace
     *
     * @param appName
     */
    public TriggerRace(String appName) {
        super(appName);
        JButton goButton =
                controlPanel.goButton;
        ActionTrigger.addTrigger(goButton,animator);
    }
}
