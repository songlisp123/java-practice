package com.snl.test.textMove;

import audio.ChooseSourceLine;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class Music {

    private static Mixer mixer;
    private static SourceDataLine line;

    static {
        mixer = getMixer();
    }


    public static Mixer getMixer() {
//        Line.Info targetInfo = new Line.Info(SourceDataLine.class);
//        Line.Info targetInfoClip = new Line.Info(Clip.class);
        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
        for (Mixer.Info info : mixerInfo) {
            Mixer mixer = AudioSystem.getMixer(info);
            String name = info.getName();
            if (Objects.equals(name, "主声音驱动程序")) {
                return mixer;
            }
        }
        return null;
    }

    public static void beep() {
        Toolkit.getDefaultToolkit().beep();
    }

    public static void shoot() {
        Path path = Path.of(".","wav","点击.wav");
        shoot(path);
    }

    private static void shoot(Path path) {
        new Thread(run(path),"音乐播放者").start();
    }

    private static Runnable run(Path path) {
        return ()->{
            try {
                //获取文件
                BufferedInputStream inputStream = new BufferedInputStream(
                        new FileInputStream(path.toFile())
                );
                AudioInputStream stream = AudioSystem.getAudioInputStream(inputStream);
                SourceDataLine sourceDataLine = ChooseSourceLine.chooseLine(mixer);
                AudioFormat format = stream.getFormat();
                sourceDataLine.open(format);
                sourceDataLine.start();
                //声音长度
                var numberByteStore = new byte[4096];
                int read = stream.read(numberByteStore, 0, 4096);
                while (read != -1) {
                    sourceDataLine.write(numberByteStore, 0, read);
                    read = stream.read(numberByteStore, 0, 4096);
                }
                sourceDataLine.drain();
                sourceDataLine.stop();
                sourceDataLine.close();
                inputStream.close();
                stream.close();
            } catch (UnsupportedAudioFileException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }

        };
    }


}