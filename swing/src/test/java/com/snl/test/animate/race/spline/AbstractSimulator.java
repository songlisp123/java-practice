package com.snl.test.animate.race.spline;

import javax.swing.JComponent;

public class AbstractSimulator extends JComponent {
    protected double time;
    
    public AbstractSimulator() {
        this.time = 0.0f;
    }

    public void setTime(double time) {
        this.time = time;
        repaint();
    }
}
