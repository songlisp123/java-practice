package textEditor.util;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class audioPlay {

    private static File file = new File("test.wav");

    public static void play(boolean flag) {

         try (AudioInputStream stream = AudioSystem.getAudioInputStream(file)) {
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
