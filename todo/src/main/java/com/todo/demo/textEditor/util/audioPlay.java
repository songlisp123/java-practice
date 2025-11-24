package com.todo.demo.textEditor.util;

import javax.sound.sampled.*;
import java.io.IOException;
import java.nio.file.Path;

public class audioPlay {



    public static void play(boolean flag, Path path) {

         try (AudioInputStream stream = AudioSystem.getAudioInputStream(path.toFile())) {
             AudioFormat format = stream.getFormat();
             //我需要一个line对象操作该音乐数据
             DataLine.Info info = new DataLine.Info(Clip.class, format);
             //获取clip对象
             Clip clip = (Clip) AudioSystem.getLine(info);
             clip.open(stream);
             if (flag)
                 clip.start();
             else {
                 clip.stop();
             }
             clip.loop(Clip.LOOP_CONTINUOUSLY);
         } catch (UnsupportedAudioFileException e) {
             throw new RuntimeException(e);
         } catch (IOException e) {
             throw new RuntimeException(e);
         } catch (LineUnavailableException e) {
             throw new RuntimeException(e);
         }

    }



}
