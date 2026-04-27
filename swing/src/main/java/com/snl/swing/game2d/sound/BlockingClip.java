package com.snl.swing.game2d.sound;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class BlockingClip extends AudioStream {
    private Clip clip;
    private boolean restart;

    public BlockingClip(byte[] soundData) {
        super(soundData);
    }

    /*
     * This guy could throw a bunch of exceptions. We're going to wrap them all
     * in a custom exception handler that is a RuntimeException so we don't have
     * to catch and throw all these exceptions.
     */
    @Override
    public void open() {
        lock.lock();
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(soundData);
            AudioInputStream ais = AudioSystem.getAudioInputStream(in);
            clip = AudioSystem.getClip();
            clip.addLineListener(this);
            clip.open(ais);
            //阻塞到可用为止
            while( !open ) {
                cond.await();
            }
            //UPDATE
            createControls( clip );
            //UPDATE
            System.out.println( "开启✅️" );
        } catch (UnsupportedAudioFileException ex) {
            throw new SoundException(ex.getMessage(), ex);
        } catch (LineUnavailableException ex) {
            throw new SoundException(ex.getMessage(), ex);
        } catch (IOException ex) {
            throw new SoundException(ex.getMessage(), ex);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void start() {
        lock.lock();
        try {
            clip.flush();
            clip.setFramePosition(0);
            clip.start();
            while (!start) {
                cond.await();
            }
            System.out.println("开始运行……");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void loop(int count) {
        lock.lock();
        try {
            clip.flush();
            clip.setFramePosition(0);
            clip.loop(count);
            while (!start) {
                cond.await();
            }
            System.out.println("开始运行……");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void restart() {
        restart = true;
        stop();
        restart = false;
        start();
    }

    @Override
    public void fireTaskFinished() {
        if (!restart) {
            super.fireTaskFinished();
        }
    }

    @Override
    public void stop() {
        lock.lock();
        try {
            clip.stop();
            while (start) {
                cond.await();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void close() {
        lock.lock();
        try {
            clip.close();
            while (open) {
                cond.await();
            }
            clip = null;
            //UPDATE
            clearControls();
            //UPDATE
            System.out.println("关闭❌️");
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
