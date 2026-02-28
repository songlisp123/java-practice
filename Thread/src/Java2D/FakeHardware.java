package Java2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.random.RandomGenerator;

public class FakeHardware {

    private List<FakeHardwareListener> listeners =
            Collections.synchronizedList(new ArrayList<>());
    private volatile boolean on;
    private volatile boolean running;
    private String name;

    private static final int SLEEP_MIN = 100;
    private static final int SLEEP_MAX = 500;

    private final RandomGenerator generator =
            RandomGenerator.getDefault();

    public FakeHardware(String name) {
        this.name = name;
    }

    public boolean isOn() {
        return on;
    }

    public boolean isRunning() {
        return running;
    }

    public void turnOn() {
        new Thread(()->{
            sleep();
            setOn();
        }).start();
    }

    private void sleep() {
        int sleep = generator.nextInt(SLEEP_MIN, SLEEP_MAX);
        sleep(sleep);
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void turnOff() {}

    public void start() {}

    public void  stop() {}

    private synchronized void setOn() {}

    private synchronized void setOff() {}

    private void setStart() {}

    private synchronized void setStop() {}

}
