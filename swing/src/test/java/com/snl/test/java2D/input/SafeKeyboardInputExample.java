package com.snl.test.java2D.input;

import com.snl.test.frame.SafeKeyboardFramework;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class SafeKeyboardInputExample extends SafeKeyboardFramework {

    int spaceCount;
    float blink;
    boolean drawCursor;
    List<String> strings = new ArrayList<>();

    public SafeKeyboardInputExample() throws HeadlessException {
        appSleep = 16;
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        while (keyBoardEvent.processEvent()) {
            if (keyBoardEvent.keyDownOnce(KeyEvent.VK_DOWN))
            {
                appSleep = Math.min(appSleep / 2,1000);
            }
            if (keyBoardEvent.keyDownOnce(KeyEvent.VK_UP))
                appSleep = Math.min(appSleep * 2,1000);

            if (keyBoardEvent.keyDownOnce(KeyEvent.VK_ESCAPE))
                spaceCount = 0;

            if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
                spaceCount++;
            proceessChar();
        }
    }

    private void proceessChar() {
        Character c = keyBoardEvent.getTyped();
        if (c != null) {
            if (Character.isISOControl(c)) {
                //删除
                if (KeyEvent.VK_BACK_SPACE == c)
                {
                    removeCharacter();
                }
                if (KeyEvent.VK_ENTER == c)
                    strings.add("");
            }else {
                addChar(c);
            }
        }
    }

    private void addChar(Character c) {
        strings.add(strings.removeLast()+c);
    }

    private void removeCharacter() {
        String line = strings.removeLast();
        if (!line.isEmpty()) {
            strings.add(line.substring(0, line.length() - 1));
        }
        if (strings.isEmpty()) {
            strings.add("");
        }
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        blink += (float) delta;
        if (blink > 0.5F) {
            blink -= 0.5F;
            drawCursor = !drawCursor;
        }
    }

    @Override
    protected void draw(Graphics g) {
        super.draw(g);
        if (drawCursor) {
            FontMetrics fm = g.getFontMetrics();
            int height = fm.getAscent() + fm.getDescent() + fm.getLeading();
            int x = 20 + fm.stringWidth(strings.getLast());
            g.drawString("_", x, 20 + fm.getAscent());
        }
    }

    public static void main(String[] args) {
        launchGame(new SafeKeyboardInputExample());
    }
}
