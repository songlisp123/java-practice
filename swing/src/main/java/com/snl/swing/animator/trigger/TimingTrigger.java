package com.snl.swing.animator.trigger;

import com.snl.swing.animator.Animator;
import com.snl.swing.animator.TimingTarget;

public class TimingTrigger extends Trigger implements TimingTarget {

    private Animator source;
    private TimingTriggerEvent timingTriggerEvent;


    public TimingTrigger(Animator animator, TriggerEvent triggerEvent, boolean autoReverse) {
        super(animator, triggerEvent, autoReverse);
    }

    @Override
    public void begin() {

    }

    @Override
    public void end() {

    }

    @Override
    public void repeat() {

    }

    @Override
    public void timingEvent(double fraction) {

    }

    public static TimingTrigger addTrigger(Animator source, Animator target, TimingTriggerEvent event) {
        return addTrigger(source, target, event, false);
    }

    public static TimingTrigger addTrigger(Animator source, Animator target, TimingTriggerEvent event, boolean autoReverse) {
        TimingTrigger trigger = new TimingTrigger(target, event, autoReverse);
        source.addTargets(trigger);
        return trigger;
    }
}
