package audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MutipleMixer {

    public static Runnable playMusic(Path musicFilePath) {
        return () -> {
            Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
            Mixer mixer = AudioSystem.getMixer(mixerInfo[19]);

            Line.Info[] sourceLineInfo = mixer.getSourceLineInfo();
            try (AudioInputStream stream = AudioSystem.getAudioInputStream(new File(musicFilePath.toUri()))) {
                AudioFormat format = stream.getFormat();
                SourceDataLine line = (SourceDataLine) mixer.getLine(sourceLineInfo[0]);
                DataLine.Info info = new DataLine.Info(Clip.class, format);
                Clip line2 = (Clip) AudioSystem.getLine(info);
                line.addLineListener(new LineEventImpl(LocalDateTime.now()));

                Line[] lines = new Line[]{line, line2};

                if (!mixer.isSynchronizationSupported(lines, true)) {
                    System.out.println("不能并行播放！此混声器不能用于并行播放");
                }

                line.open(format);
                line.start();
                //声音长度

                boolean stopped = false;
                var numberByteStore = new byte[4096];
                int read = stream.read(numberByteStore, 0, 4096);
                while (read != -1) {
                    line.write(numberByteStore, 0, read);
                    read = stream.read(numberByteStore, 0, 4096);
                }
                line.stop();
            } catch (UnsupportedAudioFileException | LineUnavailableException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static class LineEventImpl implements LineListener {

        private LocalDateTime timeStamp;
        private Thread thread;

        public LineEventImpl(LocalDateTime time) {
            this.timeStamp = time;
            this.thread = Thread.currentThread();
        }

        @Override
        public void update(LineEvent event) {
            if (event.getType() == LineEvent.Type.CLOSE) {
                System.out.printf("当前线程:%s%n",thread.getName());
                if (event.getFramePosition() >= 19) {
                    System.out.println("音乐正常结束");
                } else {
                    System.out.println("音乐被暂停或者终止");
                }
            }

//            if (event.getLine().isOpen()) {
//                System.out.println("开始播放音乐……");
//                System.out.println(LocalDateTime.now());
//            }

            if (event.getType() == LineEvent.Type.STOP) {
                System.out.printf("当前线程:%s%n",thread.getName());
                LocalDateTime now = LocalDateTime.now();
                try (ExecutorService executorService = Executors.newCachedThreadPool()) {
                    executorService.submit(playMusic(Path.of("娘子.wav")));
                }
                int i = now.getNano() - this.timeStamp.getNano();
                long framePosition = event.getFramePosition();
                System.out.printf("当前的位置是:%d%n",framePosition);
                System.out.println("音乐时长:[%d]纳秒".formatted(i));
                System.out.println("音乐结束！");
            }

            if (event.getType() == LineEvent.Type.OPEN) {
                System.out.printf("当前线程:%s%n",thread.getName());
                System.out.println("开始播放音乐……");
                System.out.println(LocalDateTime.now());
            }
        }
    }

    public static void main(String[] args) {
        try (ExecutorService executorService = Executors.newCachedThreadPool()) {
            executorService.submit(playMusic(Path.of("爱在西元前.wav")));
//            executorService.submit(playMusic(Path.of("娘子.wav")));
        }
//        new Thread(playMusic(Path.of("爱在西元前.wav")),"爱在西元前").start();
//        new Thread(playMusic("手写的从前.wav"),"手写的从前").start();
//        new Thread(playMusic(Path.of("娘子.wav")),"娘子").start();

    }
}
