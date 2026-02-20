package com.snl.test.java2D.thread;

import com.snl.test.java2D.UTIL.RandomGeneratorClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FakeHardware {
    final int MIN = 100;
    final int MAX = 500;

    public enum FakeHardwareEvent {
        START,STOP,ON,OFF
    };

    private volatile boolean on = false;
    private volatile boolean running = true;

    private String name;
    //如果是我写的话，我会写这个
//    private final List<FakeHardwareListener> listeners = new ArrayList<>();
    //但是作者的实现版本不一样。
    List<FakeHardwareListener> listeners = Collections.synchronizedList(new ArrayList<>());

    public FakeHardware(String name) {
        this.name = name;
    }

    public void addListener(FakeHardwareListener listener) {
        listeners.add(listener);
    }

    public void removeListener(FakeHardwareListener listener) {
        listeners.remove(listener);
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isOn() {
        return on;
    }

    public void sleep() {
        int random = (int) RandomGeneratorClass.random(MIN, MAX);
        try {
            Thread.sleep(random);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void turnOn() {
        new Thread(()->{
            sleep();
            setOn();
        }).start();
    }

    public void turnOff() {
        new Thread(()->{
            sleep();
            setOff();
        }).start();
    }

    public void start() {

    }

    public void stop() {
        new Thread(()->{
            sleep();
            setStop();
        }).start();
    }

    public void setOn() {
        if (!on)
        {
            on = true;
            fireEvent(FakeHardwareEvent.ON);
        }
    }

    public void setOff() {
        if (on) {
            //如果正在运行
            //第一步：停止
            setStop();
            //第二步：改变变量
            on = false;
            //第三步:出发时间
            fireEvent(FakeHardwareEvent.OFF);
        }
    }

    private void  setStart() {
        synchronized (this) {
            if (on && !running)
            {
                running = true;
                fireEvent(FakeHardwareEvent.START);
                return;
            }
        }
        if (running) {
            setStop();
            running = true;
            fireEvent(FakeHardwareEvent.START);
        }
    }

    public void setStop() {
        if (running)
            running = false;
        //为什么不发生事件？？？？
    }

    private void fireEvent(FakeHardwareEvent event) {
        for (FakeHardwareListener l :listeners)
            l.event(this,event);
    }
}
