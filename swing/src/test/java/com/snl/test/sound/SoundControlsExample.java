package com.snl.test.sound;

import com.snl.test.java2D.coords.DiKaErPlus;

import java.awt.event.KeyEvent;
import java.io.*;

public class SoundControlsExample extends DiKaErPlus {
    private BlockingClip clip;
    private LoopEvent loopClip;
    private BlockingDataLine dataLine;
    private LoopEvent loopStream;
    private byte[] rawSound;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        try {
            InputStream in = new FileInputStream("./简单爱.wav");
            rawSound = readBytes(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        clip = new BlockingClip(rawSound);
        loopClip = new LoopEvent(clip);
        loopClip.initialize();

        dataLine = new BlockingDataLine(rawSound);
        loopStream = new LoopEvent(dataLine);
        loopStream.initialize();
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

    private void shutDownClips() {
        if( loopClip != null ) loopClip.shutDown();
        if( loopStream != null ) loopStream.shutDown();
    }

    @Override
    protected void processInput( double delta ) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_1)) {
            loopClip.fire();
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_2)) {
            loopClip.done();
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_3)) {
            loopStream.fire();
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_4)) {
            loopStream.done();
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_W)) {
            increaseGain(clip);
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_S)) {
            decreaseGain(clip);
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_A)) {
            panLeft(clip);
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_D)) {
            panRight(clip);
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_I)) {
            increaseGain(dataLine);
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_K)) {
            decreaseGain(dataLine);
        }

        if( keyBoardEvent.keyDownOnce( KeyEvent.VK_J ) ) {
            panLeft( dataLine );
        }
        if( keyBoardEvent.keyDownOnce( KeyEvent.VK_L ) ) {
            panRight( dataLine );
        }
    }

    private void increaseGain( AudioStream audio ) {
        float current = audio.getGain();
        if( current < 10.0f ) {
            audio.setGain(Math.min(current + 3.0f, audio.getMaximum()));
        }
    }
    private void decreaseGain( AudioStream audio ) {
        float current = audio.getGain();
        if( current >-20.0f ) {
            audio.setGain( current- 3.0f );
        }
    }
    private void panLeft( AudioStream audio ) {
        float current = audio.getPan();
        float precision = audio.getPrecision();
        audio.setPan( current- precision * 10.0f );
    }
    private void panRight( AudioStream audio ) {
        float current = audio.getPan();
        float precision = audio.getPrecision();
        audio.setPan( current + precision * 10.0f );
    }


    @Override
    protected void teminate() {
        super.teminate();
        shutDownClips();
    }

    public static void main(String[] args ) {
        launchGame( new SoundControlsExample());
    }
}
