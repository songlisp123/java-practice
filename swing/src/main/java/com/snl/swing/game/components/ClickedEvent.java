package com.snl.swing.game.components;

public class ClickedEvent {

    private Object source;
    private String actionString;

    public ClickedEvent(Object source, String actionString) {
        this.source = source;
        this.actionString = actionString;
    }

    public void setActionString(String actionString) {
        this.actionString = actionString;
    }

    public String getActionString() {
        return actionString;
    }

    public Object getSource() {
        return source;
    }
}
