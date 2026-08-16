package com.snl.swing.animator.trigger;

import com.snl.swing.animator.Animator;

public abstract class Trigger {

    private Animator animator;
    private Animator reverseAnimator;
    private boolean disarmed,autoReverse;
    //事件
    private TriggerEvent triggerEvent;


    public Trigger(Animator animator) {
        this(animator,(TriggerEvent) null);
    }

    public Trigger(Animator animator, TriggerEvent triggerEvent) {
        this(animator,triggerEvent,false);
    }

    public Trigger(Animator animator, TriggerEvent triggerEvent, boolean autoReverse) {
        //这是什么变量？？接触武装》？？？
        this.disarmed = false;
        //默认不自动反向播放
        this.autoReverse = false;
        this.animator = animator;
        this.triggerEvent = triggerEvent;
        this.autoReverse = autoReverse;
    }


    public void fire(TriggerEvent event) {
        //TODO
        if (!disarmed) {
            if (event == this.triggerEvent) {
                if (this.autoReverse) {
                    if (this.animator.isRunning()) {
                        double timingFraction = animator.getTimingFraction();
                        this.animator.stop();
                        this.animator.setStartFraction(timingFraction);
                    }
                    else {
                        this.animator.setStartFraction(0.0f);
                    }
                }

                if (this.animator.isRunning())
                    animator.stop();

                this.animator.setDirection(Animator.Direction.FORWARD);
                this.fire();
            }
        }
    }

    public void fire() {
        if (!this.disarmed) {
            if (this.animator.isRunning())
                animator.stop();
            animator.start();
        }
    }

    public void disarm() {
        this.disarmed = true;
    }
}
