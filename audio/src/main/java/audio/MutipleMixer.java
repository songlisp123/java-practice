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
//                fadeIn(sourceDataLine,2000L);
                sourceDataLine.start();
//                setVolume(sourceDataLine,0.8f);
                //声音长度



                boolean stopped = false;
                var numberByteStore = new byte[4096];
                float gain = 0f;
                float fadeInStep = 1f / (44100 * 10); // 10秒淡入
                int read = stream.read(numberByteStore, 0, 4096);
                while (read != -1) {
//                    System.out.printf("读取字节数：%d%n",read);
                    for (int i = 0; i < read; i+=2) {
                        short sample = (short) ((numberByteStore[i + 1] << 8) | (numberByteStore[i] & 0xff));
//                        System.out.printf("原本样本：%d%n",sample);
                        float s = sample * gain;

                        short newSample = (short) s;
//                        System.out.printf("新样本：%d%n",newSample);
                        numberByteStore[i] = (byte) (newSample & 0xff);
                        numberByteStore[i+1] = (byte) ((newSample >> 8) & 0xff);

                        if (gain < 1f) gain += fadeInStep;
                    }
                    sourceDataLine.write(numberByteStore, 0, read);
                    read = stream.read(numberByteStore, 0, 4096);

                }
                fadeOut(sourceDataLine,10000L);
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
            executorService.submit(playMusic(Path.of("爱在西元前.wav")));
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
//            System.out.println(db);
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

    public static void setVolume(SourceDataLine line, float volumePercent) {
        if (volumePercent < 0f || volumePercent > 1f) {
            throw new IllegalArgumentException("音量范围必须是 0.0 ~ 1.0");
        }

        if (line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gain = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);

            // 将百分比映射到 dB
            float min = gain.getMinimum(); // 通常是 -80.0 dB
            float max = gain.getMaximum(); // 通常是 +6.0 dB

            // 线性映射（0→min，1→max）
            float dB = min + (max - min) * volumePercent;

            gain.setValue(dB);
        }
    }


}
