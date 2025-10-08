package wormsubject;

import wormsubject.game.SimpleFrame;
import wormsubject.game.initPanel;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class
TestDemo {
    public static void main(String[] args) throws InterruptedException {
        File music = new File("test.wav");
        EventQueue.invokeLater(()->{
            var frame = new SimpleFrame(1200,800);
            var pabel = new initPanel(frame);
            frame.add(pabel,BorderLayout.CENTER);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.setVisible(true);
        });
        Runnable task = () -> {
            try(AudioInputStream stream = AudioSystem.getAudioInputStream(music))
            {
                //获取音乐数据格式
                AudioFormat format = stream.getFormat();
                DataLine.Info info = new DataLine.Info(Clip.class,format);
                //获取音乐片段通道
                Clip line = (Clip) AudioSystem.getLine(info);
                line.open(stream);
                line.start();
                line.loop(Clip.LOOP_CONTINUOUSLY);
            } catch (UnsupportedAudioFileException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        };

        new Thread(task,"music").start();



    }
}
