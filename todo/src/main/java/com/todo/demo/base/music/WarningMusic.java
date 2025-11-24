package com.todo.demo.base.music;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WarningMusic {

    private static Runnable task() {
        return () -> {
            Mixer.Info[]mixerInfo = AudioSystem.getMixerInfo();
            Mixer mixer = AudioSystem.getMixer(mixerInfo[19]);

            Mixer mixer1 = AudioSystem.getMixer(mixerInfo[29]);
            System.out.println(mixer1.getMixerInfo());

//        for (Line.Info info : mixer1.getTargetLineInfo()) {
//            System.out.println(info);
//        }
            Line.Info[] sourceLineInfo = mixer.getSourceLineInfo();

            try (AudioInputStream stream =
                     AudioSystem.getAudioInputStream(
                             new File("./todo/warning_extended_smooth.wav"))) {

//            Clip clip = (Clip) mixer.getLine(sourceLineInfo[1]);
//            clip.open(stream);
//            clip.start();
//            clip.loop(Clip.LOOP_CONTINUOUSLY);
//            long microsecondLength = clip.getMicrosecondLength();
//            System.out.println(microsecondLength/1000/1000);
//            Thread.sleep(10000L);
//            clip.close();
            AudioFormat format = stream.getFormat();
            SourceDataLine line = (SourceDataLine) mixer.getLine(sourceLineInfo[0]);

            line.open(format);
            line.start();
            //声音长度
            line.addLineListener(new LineEventImpl());
            boolean stopped = false;
            var numberByteStore = new byte[12];
            int read = stream.read(numberByteStore, 0, 12);
            while (read != -1) {
                line.write(numberByteStore, 0, read);
                read = stream.read(numberByteStore, 0, 12);
            }
            line.close();
            } catch (UnsupportedAudioFileException | LineUnavailableException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public void play() {
        new Thread(task(), "音乐守卫WARNING").start();

    }

    private static class LineEventImpl implements LineListener {
        @Override
        public void update(LineEvent event) {
            if(event.getType() == LineEvent.Type.STOP) {
                if (event.getFramePosition() >= 19) {
                    System.out.println("音乐正常结束");
                }
                else {
                    System.out.println("音乐被暂停或者终止");
                }
            }

            if (event.getLine().isOpen()) {
                System.out.println(LocalDateTime.now());
            }

            if (event.getType() == LineEvent.Type.CLOSE) {
                System.out.println("音乐终止！");
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(task());
//        executorService.shutdown();
    }
}



