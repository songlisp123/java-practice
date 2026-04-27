package com.snl.test.sound;

import com.snl.test.java2D.coords.DiKaErPlus;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;

public class SoundPlayerExample extends DiKaErPlus {

    private OneShotEvent oneShotClip;
    private LoopEvent loopClip;
    private RestartEvent restartClip;
    private OneShotEvent oneShotStream;
    private LoopEvent loopStream;
    private RestartEvent restartStream;
    private byte[] weaponBytes;
    private byte[] rainBytes;
    private String loaded;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        try {
            InputStream in = new FileInputStream("./简单爱.wav");
            weaponBytes = readBytes(in);
            in = new FileInputStream("./爱在西元前.wav");
            rainBytes = readBytes(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] readBytes(InputStream in) {
        try {
            BufferedInputStream bf = new BufferedInputStream(in);
            ByteArrayOutputStream bot = new ByteArrayOutputStream();
            int read;
            while ((read = bf.read()) != -1) {
                bot.write(read);
            }
            in.close();
            return bot.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadWaveFile(byte[] rawData) {
        shutDownClips();
        //创建新的
        oneShotClip = new OneShotEvent(new BlockingClip(rawData));
        oneShotClip.initialize();

        loopClip = new LoopEvent(new BlockingClip(rawData));
        loopClip.initialize();

        restartClip = new RestartEvent(new BlockingClip(rawData));
        restartClip.initialize();

        oneShotStream = new OneShotEvent(new BlockingDataLine(rawData));
        oneShotStream.initialize();

        loopStream = new LoopEvent(new BlockingDataLine(rawData));
        loopStream.initialize();

        restartStream = new RestartEvent(new BlockingDataLine(rawData));
        restartStream.initialize();
    }

    public void shutDownClips() {
        if (oneShotClip != null)
            oneShotClip.shutDown();
        if (loopClip != null)
            loopClip.shutDown();
        if (restartClip != null)
            restartClip.shutDown();
        if (oneShotStream != null)
            oneShotStream.shutDown();
        if (loopStream != null)
            loopStream.shutDown();
        if (restartStream != null)
            restartStream.shutDown();
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_R)) {
            loadWaveFile(weaponBytes);
            loaded = "weapon";
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_E)) {
            loadWaveFile(rainBytes);
            loaded = "rain";
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_1)) {
            oneShotClip.fire();
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_2)) {
            oneShotClip.done();
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_3)) {
            loopClip.fire();
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_4)) {
            loopClip.done();
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_5)) {
            restartClip.fire();
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_6)) {
            oneShotStream.fire();
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_7)) {
            oneShotStream.done();
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_8)) {
            loopStream.fire();
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_9)) {
            loopStream.done();
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_0)) {
            restartStream.fire();
        }

        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_Q))
            teminate();
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setColor(Color.CYAN);
        g2.drawString("加载"+loaded,30,130);
    }

    @Override
    protected void teminate() {
        super.teminate();
        shutDownClips();
    }

    public static void main(String[] args) {
        launchGame(new SoundPlayerExample());
    }
}
