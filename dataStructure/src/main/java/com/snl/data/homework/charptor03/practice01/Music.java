package com.snl.data.homework.charptor03.practice01;

import audio.ChooseSourceLine;

import javax.sound.sampled.*;
import java.awt.*;
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
        Line.Info targetInfo = new Line.Info(SourceDataLine.class);
        Line.Info targetInfoClip = new Line.Info(Clip.class);

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

    public static SourceDataLine getLine(Mixer mixer) throws LineUnavailableException {
        if (mixer == null)
            return null;
        SourceDataLine line = null;
        Line.Info[] sourceLineInfo = mixer.getSourceLineInfo();
        for(int i=0;i< sourceLineInfo.length;i++) {
            Line.Info info = sourceLineInfo[i];
            if (i == 0) {
                System.out.println(info);
                line = (SourceDataLine) mixer.getLine(info);
            }
        }
        return line;
    }

    public static void beep() {
        Toolkit.getDefaultToolkit().beep();
    }

    public static void sniparShoot() {
        Path path = Path.of(".","wav","狙击步枪.wav");
        shoot(path);
    }

    public static void assaultShoot() {
        Path path = Path.of(".","wav","突击步枪.wav");
        shoot(path);
    }

    public static void pistolShoot() {
        Path path = Path.of(".","wav","手枪射击.wav");
        shoot(path);
    }

    public static void backGroundMusic() {
        Path path = Path.of(".","oct.wav");
        shoot(path);
    }

    public static void subMacheingShoot() {
        Path path = Path.of(".","wav","冲锋枪射击.wav");
        shoot(path);
    }

    public static void bulletsCrashWall() {
        Path path = Path.of(".","wav","子弹撞墙.wav");
        shoot(path);
    }

    public static void reload() {
        Path path = Path.of(".","wav","装弹.wav");
        shoot(path);
    }

    public static void emptyBullets() {
        Path path = Path.of(".","wav","空弹.wav");
        shoot(path);
    }

    private static void shoot(Path path) {
        new Thread(run(path),"音乐播放者").start();
    }

    private static Runnable run(Path path) {
        return ()->{
            try(AudioInputStream stream = AudioSystem.getAudioInputStream(path.toFile())) {
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
                sourceDataLine.stop();
                sourceDataLine.drain();
                sourceDataLine.close();

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
