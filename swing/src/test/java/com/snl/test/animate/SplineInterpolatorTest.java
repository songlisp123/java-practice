package com.snl.test.animate;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.SplineInterpolator;

public class SplineInterpolatorTest extends TimingTargetAdapter {

    private long startTime;
    private final static int DURATION = 4000;

    @Override
    public void begin() {
        startTime = System.nanoTime() / 1000000;
        System.out.println("Real\tInterpolated");
        System.out.println("----\t------------");
    }

    @Override
    public void timingEvent(float fraction) {
        long currentTime = System.nanoTime() / 1000000;
        long elapsedTime = currentTime - startTime;
        float realFraction = (float)elapsedTime / DURATION;
        System.out.println(realFraction + "\t" + fraction);
    }

    public static void main(String args[]) {
        Animator animator =
                new Animator(DURATION, new SplineInterpolatorTest());
        SplineInterpolator interpolator = new SplineInterpolator(0f, 1f, 0f, 1f);
        animator.setInterpolator(interpolator);
        // Note that you could get similar behavior by setting the following
        // acceleration/deceleration properties instead of the interpolator
        // above:
        //animator.setAcceleration(.5f);
        //animator.setDeceleration(.5f);
        animator.setStartDelay(0);
        animator.setResolution(DURATION / 10);
        animator.start();
    }
}
