package com.snl.swing.game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

public class SafeKeyboardInput implements KeyListener {

    /**
     * 下面三个方法，调用的是事件队列，这是一个很重要的步骤，确保按键事件不会丢失。
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {
        eventThread.add(new Event(e,EventType.TYPED));
    }

    @Override
    public void keyPressed(KeyEvent e) {
        eventThread.add(new Event(e,EventType.PRESSED));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        eventThread.add(new Event(e,EventType.RELEASED));
    }

    enum EventType {
        PRESSED, RELEASED, TYPED;
    };

    class Event {
        KeyEvent keyEvent;
        EventType type;

        public Event(KeyEvent keyEvent, EventType type) {
            this.keyEvent = keyEvent;
            this.type = type;
        }
    }

    private LinkedList<Event> gameThread = new LinkedList<>();
    private LinkedList<Event> eventThread = new LinkedList<>();
    private Event event = null;
    private int[] polled;


    public SafeKeyboardInput() {
        polled = new int[256];
    }

    public boolean keyDown(int keyCode) {
        return event.keyEvent.getKeyCode() == keyCode &&
                polled[keyCode] > 0;
    }

    public  boolean keyDownOnce(int keyCode) {
        return event.keyEvent.getKeyCode() == keyCode &&
                polled[keyCode] == 1;
    }


    /**
     * 这个方法在每一帧调用，调用该方法应该循环迭代遍历链表
     * @return {@code true}如果遍历尚未完成，否则返回false。
     */
    public boolean processEvent() {
        event = gameThread.poll();
        if (event != null)
        {
            int keyCode = event.keyEvent.getKeyCode();
            if (keyCode >=0 && keyCode < polled.length)
            {
                if (event.type == EventType.PRESSED)
                    polled[keyCode] ++;
                else if (event.type == EventType.RELEASED) {
                    polled[keyCode] = 0;
                }
            }
        }
        return event != null;
    }


    /**
     * 这个方法在游戏线程中调用，我们交换事件
     */
    public void poll() {
        LinkedList<Event> temp = eventThread;
        eventThread = gameThread;
        gameThread = temp;

    }

    public Character getTyped() {
        if (event.type != EventType.TYPED)
            return null;
        return event.keyEvent.getKeyChar();
    }

}
