package com.snl.swing.game2026;

import com.snl.swing.animator.Animator;
import com.snl.swing.animator.TimingTarget;

import java.awt.*;

public class PartAdpater implements Part, TimingTarget {

    protected double start,end;
    private Animator animator;

    public PartAdpater(double start, double end) {
        this.start = start;
        this.end = end;

        this.createAnimator();
    }

    @Override
    public void begin() {

    }

    @Override
    public void end() {

    }

    @Override
    public void repeat() {

    }

    @Override
    public void timingEvent(double fraction) {
        System.out.println("fraction = " + fraction);
    }

    @Override
    public double getStart() {
        return start;
    }

    @Override
    public double getEnd() {
        return end;
    }

    @Override
    public void startAnimator() {
        if (!animator.isRunning())
            animator.start();
    }

    @Override
    public void flush() {
        if (animator == null)
            return;
        if (animator.isRunning())
            animator.stop();
        animator = null;
    }

    @Override
    public void render(Graphics2D g2) {

    }

    private void createAnimator() {
        animator = new Animator((long) ((this.end - this.start) * 1.0E3),this);
        animator.setRepeatCount(1.0f);
        animator.setEndBehavior(Animator.EndBehavior.HOLD);
    }

    public Animator getAnimator() {
        return animator;
    }
}
