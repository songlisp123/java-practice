package com.snl.swing.game2d.sound;

import java.util.Objects;

public class RestartEvent extends SoundEvent {

    public static final String STATE_WAITING = "waiting";
    public static final String STATE_RUNNING = "running";
    public static final String EVENT_FIRE = "fire";
    public static final String EVENT_DONE = "done";
    private String currentState;
    public RestartEvent( AudioStream stream ) {
        super( stream );
        currentState = STATE_WAITING;
    }
    public void fire() {
        put( EVENT_FIRE );
    }
    protected void processEvent( String event ) throws InterruptedException {
        System.out.println( "Got " + event + " event" );
        if(Objects.equals(currentState, STATE_WAITING)) {
            if(Objects.equals(event, EVENT_FIRE)) {
                currentState = STATE_RUNNING;
                audio.open();
                audio.start();
            }
        } else if(Objects.equals(currentState, STATE_RUNNING)) {
            if(Objects.equals(event, EVENT_FIRE)) {
                audio.restart();
            }
            if(Objects.equals(event, EVENT_DONE)) {
                currentState = STATE_WAITING;
                audio.close();
            }
        }
    }
    @Override
    protected void onAudioFinished() {
        put( EVENT_DONE );
    }
}
