package audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MutipleMixer {

    public static Runnable playMusic(Path musicFilePath) {
        return () -> {
//            Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
//            //获取一个合适的混音器
//            for (Mixer.Info info : mixerInfo) {
//                System.out.println(info);
//            }
//            Mixer mixer = AudioSystem.getMixer(mixerInfo[19]);

            Mixer mixer = ChooseBestMixer.chooseMixer();
            if (Objects.isNull(mixer)) {
                return;
            }


            try (AudioInputStream stream = AudioSystem.getAudioInputStream(musicFilePath.toFile())) {
                AudioFormat format = stream.getFormat();
                System.out.println("音乐格式："+format);
                float frameRate = format.getFrameRate();
//                Line.Info[] sourceLineInfo = mixer.getSourceLineInfo();
//                SourceDataLine line = (SourceDataLine) mixer.getLine(sourceLineInfo[0]);
//                DataLine.Info info = new DataLine.Info(Clip.class, format);
//                Clip line2 = (Clip) AudioSystem.getLine(info);
//                line.addLineListener(new LineEventImpl(frameRate));
//                Line[] lines = new Line[]{line, sourceDataLine};
//                if (!mixer.isSynchronizationSupported(lines, true)) {
//                    System.out.println("不能并行播放！此混声器不能用于并行播放");
//                }

                SourceDataLine sourceDataLine = ChooseSourceLine.chooseLine(mixer);
                sourceDataLine.addLineListener(new LineEventImpl(frameRate));
                sourceDataLine.open(format);
                fadeIn(sourceDataLine,2000L);
                sourceDataLine.start();

                //声音长度



                boolean stopped = false;
                var numberByteStore = new byte[4096];
                int read = stream.read(numberByteStore, 0, 4096);
                while (read != -1) {
                    sourceDataLine.write(numberByteStore, 0, read);
                    read = stream.read(numberByteStore, 0, 4096);

                }
                fadeOut(sourceDataLine,2000L);
                sourceDataLine.stop();
                sourceDataLine.drain();
                sourceDataLine.close();
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static class LineEventImpl implements LineListener {

        private LocalDateTime timeStamp;
        private Thread thread;
        private long framePosition;
        private float frameRate;



        public LineEventImpl() {
            this.timeStamp = LocalDateTime.now();
            this.thread = Thread.currentThread();
        }

        public LineEventImpl(float frameRate) {
            this();
            this.frameRate = frameRate;
        }

        @Override
        public void update(LineEvent event) {

            if (event.getType() == LineEvent.Type.START) {
                System.out.println("【✅】 开始播放音乐……");
                System.out.println(LocalDateTime.now());
                this.framePosition = event.getFramePosition();
            }

            if (event.getType() == LineEvent.Type.CLOSE) {
                System.out.printf("当前线程:%s%n",thread.getName());
                if (event.getFramePosition() >= 19) {
                    System.out.println("音乐正常结束");
                } else {
                    System.out.println("音乐被暂停或者终止");
                }
            }


            if (event.getType() == LineEvent.Type.STOP) {
                System.out.printf("当前线程:%s%n",thread.getName());
                long framePosition = event.getFramePosition();
                System.out.printf("起始位置：%d",this.framePosition);
                System.out.printf("当前样本数量：%d%n",framePosition);
                System.out.printf("音乐时长:[%.2f]秒%n",Math.floor(framePosition / this.frameRate));
            }

            if (event.getType() == LineEvent.Type.OPEN) {
                System.out.println("【✅】管道开启");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        try(ExecutorService executorService = Executors.newCachedThreadPool()) {
            executorService.submit(playMusic(Path.of("手写的从前.wav")));
            Thread.sleep(Duration.of(3000L, ChronoUnit.SECONDS));
        }
    }

    public static void fadeIn(SourceDataLine line,long duration) {
        if (!line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            System.out.println("该管道不能使用空间");
            return;
        }
        FloatControl control = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);

        float minimum = control.getMinimum();
        float maximum = control.getMaximum();

        int step = 200; //步数，越大越丝滑
        long sleep = duration / step;

        for (int i = 0; i<= step ; i++) {
            float percent =(float) i / step;
            float db = minimum + (maximum -minimum) * percent;
            control.setValue(db);
            try {
//                System.out.println(Thread.currentThread().getName());
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void fadeOut(SourceDataLine line,long duration) {
        if (!line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            System.out.println("该管道不能使用空间");
            return;
        }
        FloatControl control = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);

        float minimum = control.getMinimum();
        float maximum = control.getMaximum();
        float current = control.getValue();

        int step = 50; //步数，越大越丝滑
        long sleep = duration / step;

        for (int i = 0; i<= step ; i++) {
            float percent =(float) i / step;
            float db = current + (maximum -current) * percent;
            control.setValue(db);
            try {
                Thread.sleep(step);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
