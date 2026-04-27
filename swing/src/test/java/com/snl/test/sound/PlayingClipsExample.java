package com.snl.test.sound;

import javax.sound.sampled.*;
import java.io.*;

public class PlayingClipsExample implements LineListener {

    private volatile boolean open;
    private volatile boolean start;

    public byte[] readBytes(InputStream in) {
        try {
            BufferedInputStream buf = new BufferedInputStream(in);
            ByteArrayOutputStream bot = new ByteArrayOutputStream();
            int read;
            while ((read = buf.read()) != -1)
                bot.write(read);
            in.close();
            return bot.toByteArray();
        }catch (IOException ie) {
            ie.printStackTrace();
            return null;
        }
    }

    public void runTestWithoutWaiting() throws LineUnavailableException, IOException, UnsupportedAudioFileException, InterruptedException {
        System.out.println("测试无需等待的程序");
        Clip clip = AudioSystem.getClip();
        clip.addLineListener(this);
        InputStream in = new FileInputStream("./简单爱.wav");
        byte[] rawBytes = readBytes(in);
        ByteArrayInputStream bin = new ByteArrayInputStream(rawBytes);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bin);
        clip.open(audioInputStream);
        for (int i = 0;i<10;i++) {
            clip.start();
            while (!clip.isActive())
                Thread.sleep(100);
            clip.stop();
            clip.flush();
            clip.setFramePosition(0);
            clip.start();
            clip.drain();
        }
        clip.close();
    }

    public void runTestWithWaiting() throws Exception {
        System.out.println("测试需等待的程序");
        Clip clip = AudioSystem.getClip();
        clip.addLineListener(this);
        InputStream in = new FileInputStream("./简单爱.wav");
        byte[] rawBytes = readBytes(in);
        in = new ByteArrayInputStream(rawBytes);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(in);
        synchronized (this) {
            clip.open(audioInputStream);
            while (!open) {
                //如果没有打开
                wait();
            }
        }

        for (int i = 0;i<10;++i) {
            clip.setFramePosition(0);
            synchronized (this) {
                clip.start();
                while (!start)
                    wait();
            }

            clip.drain();
            synchronized (this) {
                clip.stop();
                while (start) {
                    wait();
                }
            }

            synchronized (this) {
                clip.close();
                while (open) {
                    wait();
                }
            }
        }
    }


    @Override
    public void update(LineEvent lineEvent) {
        System.out.println("Got Event: " + lineEvent.getType());
        LineEvent.Type type = lineEvent.getType();
        if (type == LineEvent.Type.OPEN) {
            open = true;
        } else if (type == LineEvent.Type.START) {
            start = true;
        } else if (type == LineEvent.Type.STOP) {
            start = false;
        } else if (type == LineEvent.Type.CLOSE) {
            open = false;
        }
        notifyAll();
    }

    public static void main(String[] args) throws Exception {
        PlayingClipsExample playingClipsExample = new PlayingClipsExample();
//        playingClipsExample.runTestWithoutWaiting();
        playingClipsExample.runTestWithWaiting();
    }
}
