package com.snl.test.sound;

import java.util.Objects;

public class OneShotEvent extends SoundEvent {

    private static final String STATE_WAITING = "waiting";
    private static final String STATE_RUNNING = "running";

    public static final String EVENT_FIRE = "fire";
    public static final String EVENT_DONE = "done";

    private String currentState;

    public OneShotEvent(AudioStream audio) {
        super(audio);
        currentState = STATE_WAITING;
    }

    public void fire() {
        super.put(EVENT_FIRE);
    }

    public void done() {
        super.put(EVENT_DONE);
    }

    @Override
    protected void processEvent(String event) throws InterruptedException {
        System.out.println("获取" + event + "事件");
        if (Objects.equals(currentState, STATE_WAITING)) {
            if (event.equals(EVENT_FIRE))
            {
                audio.open();
                audio.start();
                currentState = STATE_RUNNING;
            }
        } else if (Objects.equals(currentState,STATE_RUNNING)) {
            if (event.equals(EVENT_DONE)) {
                audio.stop();
                audio.close();
                currentState = STATE_WAITING;
            }
        }
    }

    @Override
    protected void onAudioFinished() {
        super.put(EVENT_DONE);
    }
}
