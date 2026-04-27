package com.snl.test.sound;

import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AudioStream implements LineListener {

    public static final int LOOP_CONTINUOUSLY = -1;
    public final Lock lock = new ReentrantLock();
    protected final Condition cond = lock.newCondition();

    protected volatile boolean open;
    protected volatile boolean start;

    // 控件
    protected FloatControl gainControl;
    protected FloatControl panControl;

    //语音数据
    protected byte[] soundData;

    private List<BlockingAudioListener> listeners = Collections.synchronizedList(new ArrayList<>());

    public AudioStream(byte[] soundData) {
        this.soundData = soundData;
    }

    public abstract void open() throws RuntimeException;
    public abstract void close();
    public abstract void start();
    public abstract void loop(int count);
    public abstract void restart();
    public abstract void stop();

    public void addListener(BlockingAudioListener l) {
        listeners.add(l);
    }

    @Override
    public void update(LineEvent event) {
        boolean wasStarted = start;
        lock.lock();
        try {
            LineEvent.Type type = event.getType();
            if (type == LineEvent.Type.OPEN)
                open = true;
            if (type == LineEvent.Type.START)
                start = true;
            if (type == LineEvent.Type.CLOSE)
                open = false;
            if (type == LineEvent.Type.STOP)
                start = false;
            cond.signalAll();
        }finally {
            lock.unlock();
        }
        if (wasStarted && !start)
            fireTaskFinished();
    }


    public void fireTaskFinished() {
        synchronized (listeners) {
            for (BlockingAudioListener l : listeners) {
                l.audioFinished();
            }
        }
    }

    public void clearControls() {
        gainControl = null;
        panControl = null;
    }

    public boolean hasGainControl() {
        return gainControl != null;
    }

    public void  setGain(float fGain) {
        if (hasGainControl())
            gainControl.setValue(fGain);
    }

    public float getGain() {
        return hasGainControl() ? gainControl.getValue() : 0.0f;
    }

    public float getMaximum() {
        return hasGainControl() ? gainControl.getMaximum() : 0.0f;
    }

    public float getMinimum() {
        return hasGainControl() ? gainControl.getMinimum() : 0.0f;
    }

    public boolean hasPanControl() {
        return panControl != null;
    }

    public float getPrecision() {
        return hasPanControl() ? panControl.getPrecision() : 0.0f;
    }

    public float getPan() {
        return hasPanControl() ? panControl.getValue() : 0.0f;
    }

    public void setPan(float pan) {
        if (hasPanControl()) {
            panControl.setValue(pan);
        }
    }

    public void createControls(Line line) {
        if (line.isControlSupported(FloatControl.Type.MASTER_GAIN))
            gainControl = (FloatControl) line
                    .getControl(FloatControl.Type.MASTER_GAIN);
        if (line.isControlSupported(FloatControl.Type.PAN))
            panControl = (FloatControl) line.getControl(FloatControl.Type.PAN);
    }
}
