package com.snl.test.animate.race.linear;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The simplest version of the animation; set up a Animator to
 * move the car from one position to another over a given time period.
 * 
 * 
 * @author Chet
 */
public class BasicRace extends TimingTargetAdapter implements ActionListener {
    
    public static final int RACE_TIME = 2000;    
    Point start = TrackView.START_POS;
    Point end = TrackView.FIRST_TURN_START;
    Point current = new Point();
    //动画器
    protected Animator animator;
    //卡车
    TrackView track;
    // 控制面板
    RaceControlPanel controlPanel;
    
    /** Creates a new instance of BasicRace */
    public BasicRace(String appName) {
        RaceGUI basicGUI = new RaceGUI(appName);
        controlPanel = basicGUI.getControlPanel();
        controlPanel.addListener(this);
        track = basicGUI.getTrack();
        animator = new Animator(RACE_TIME, this);
    }
    
    //
    // Events
    //
    
    /**
     * This receives the Go/Stop events that start/stop the animation
     */
    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("Go")) {
            animator.stop();
            animator.start();
        } else if (ae.getActionCommand().equals("Stop")) {
            animator.stop();
        }
    }
    
    /**
     * TimingTarget implementation: calculate and set the current
     * car position based on the animation fraction
     */
    public void timingEvent(float fraction) {
        // Simple linear interpolation to find current position
        current.x = (int)(start.x + (end.x - start.x) * fraction);
        current.y = (int)(start.y + (end.y - start.y) * fraction);
        
        // set the new position; this will force a repaint in TrackView
        // and will display the car in the new position
        track.setCarPosition(current);
    }

    public static void main(String args[]) {
        Runnable doCreateAndShowGUI = new Runnable() {
            public void run() {
                BasicRace race = new BasicRace("BasicRace");
            }
        };
        SwingUtilities.invokeLater(doCreateAndShowGUI);
    }
    
}
