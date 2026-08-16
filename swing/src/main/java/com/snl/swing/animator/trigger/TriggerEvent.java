package com.snl.swing.animator.trigger;

public class TriggerEvent {

    private String name;

    public TriggerEvent(String name) {
        this.name = name;
    }

    public TriggerEvent getOppositeEvent() {
        return this;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
