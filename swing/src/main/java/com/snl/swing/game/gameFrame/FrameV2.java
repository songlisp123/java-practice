package com.snl.swing.game.gameFrame;

public class FrameV2 {
    //维护一个
    private long[] mPreviousTimes;
    private int mPreviousIndex;
    private boolean mPreviousFilled;
    private double mFrameRate;

    public FrameV2() {
        //初始化
        initial();
    }

    private void initial() {
        mPreviousTimes = new long[256];
        mPreviousTimes[0] =  System.currentTimeMillis();
        mPreviousIndex = 1;
        mPreviousFilled = false;
    }

    public void calculateFrameRate() {
        long now = System.currentTimeMillis();
        int numberOfFrames = mPreviousTimes.length;
        double newRate;
        //使用历史状态
        if (mPreviousFilled)
            newRate = (double) numberOfFrames /  (double) (now - mPreviousTimes[mPreviousIndex]) * 1000.0;
        else
            newRate = 1000.0/ (double) (now - mPreviousTimes[mPreviousIndex - 1]);
        mFrameRate = newRate;
        mPreviousTimes[mPreviousIndex++] = now;
        if (mPreviousIndex >= numberOfFrames)
        {
            //如果数组充满
            mPreviousIndex = 0;
            mPreviousFilled = true;
        }
    }

    public int getmFrameRate() {
        return (int) mFrameRate;
    }

    public String getFrameRate() {
        return "FPS : %d".formatted(getmFrameRate());
    }
}
