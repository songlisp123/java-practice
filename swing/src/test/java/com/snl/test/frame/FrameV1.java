package com.snl.test.frame;

public class FrameV1 {

    private String frameRate;
    private long lastTime;
    private long delta;
    private int frameCount;

    public FrameV1() {
        init();
    }

    public void init() {
        lastTime = System.currentTimeMillis();
        frameRate = "FPS : 0";
    }

    public void calculate() {
        long now = System.currentTimeMillis();
        delta += now - lastTime;
        lastTime = now;
        frameCount++;
        if (delta > 1000L)
        {
            //如果大于1000
//            delta -= 1000L; //这个方法有问题，因为可能造成剩余
            delta = 0;
            frameRate = "FPS : %d".formatted(frameCount);
            frameCount = 0;
        }
    }

    public String getFrameRate() {
        return frameRate;
    }
}
