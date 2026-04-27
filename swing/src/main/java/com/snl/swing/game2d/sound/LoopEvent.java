package com.snl.swing.game2d.sound;

import java.util.Objects;

public class LoopEvent extends SoundEvent {

    public static final String STATE_WAITING = "waiting";
    public static final String STATE_RUNNING = "running";
    public static final String EVENT_FIRE = "fire";
    public static final String EVENT_DONE = "done";

    private String currentState;

    public LoopEvent(AudioStream audio) {
        super(audio);
        currentState = STATE_WAITING;
    }

    public void fire() {
        put( EVENT_FIRE );
    }
    public void done() {
        put( EVENT_DONE );
    }

    protected void processEvent( String event ) throws InterruptedException {
        System.out.println( "Got " + event + " event" );
        if(Objects.equals(currentState, STATE_WAITING)) {
            if(Objects.equals(event, EVENT_FIRE)) {
                audio.open();
                audio.loop( AudioStream.LOOP_CONTINUOUSLY );
                currentState = STATE_RUNNING;
            }
        } else if(Objects.equals(currentState, STATE_RUNNING)) {
            if(Objects.equals(event, EVENT_DONE)) {
                audio.stop();
                audio.close();
                currentState = STATE_WAITING;
            }
        }
    }
}
