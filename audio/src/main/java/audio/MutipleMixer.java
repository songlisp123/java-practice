package audio;

import audio.util.WindowHandler;

import javax.sound.sampled.*;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MutipleMixer {

    private static final Logger logger = Logger.getLogger("music.player");
    private static final WindowHandler handler = new WindowHandler();
    private static final CountDownLatch startSignal = new CountDownLatch(1);
    private static final CountDownLatch endSignal = new CountDownLatch(1);
    private static final ReentrantLock lock = new ReentrantLock();

    static {
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);
        handler.setLevel(Level.ALL);
        handler.setFormatter(new SimpleFormatter());
        logger.addHandler(handler);
    }

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
                startSignal.await();
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
                lock.lock();
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
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                if (lock.isLocked()) lock.unlock();
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
                logger.info("【✅】 开始播放音乐……");
                logger.info(LocalDateTime.now()+"");
                this.framePosition = event.getFramePosition();
            }

            if (event.getType() == LineEvent.Type.CLOSE) {
                logger.info("当前线程:%s%n".formatted(thread.getName()));
                if (event.getFramePosition() >= 19) {
                    logger.info("音乐正常结束");
                } else {
                    logger.info("音乐被暂停或者终止");
                }
                endSignal.countDown();
            }


            if (event.getType() == LineEvent.Type.STOP) {
                logger.info("当前线程:%s%n".formatted(thread.getName()));
                long framePosition = event.getFramePosition();
                logger.info("音乐时长:[%.2f]秒%n".formatted(Math.floor(framePosition / this.frameRate)));
            }

            if (event.getType() == LineEvent.Type.OPEN) {
               logger.info("【✅】管道开启");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        try(ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10)) {
            executorService.scheduleAtFixedRate(playMusic(Path.of("test.wav")),
                    1000L,
                    5000L,
                    TimeUnit.MILLISECONDS);
            logger.info("程序开启");
            startSignal.countDown();
            endSignal.await();
            logger.info("程序结束");

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
            float db = current - (maximum -current) * percent;
            control.setValue(db);
            try {
                Thread.sleep(sleep);
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

/**
 * 音乐的问题：
 * 重大问题：淡入淡出算法是错的
 * ❌ 2. Byte → PCM sample 转换可能错误（大小端）
 * ❌ 3. 淡出（fadeOut）算法逻辑完全错误
 * ❌ 4. 在回放线程中使用 Thread.sleep() 阻塞音频回放线程
 * ❌ 5. 执行器使用 cachedThreadPool 存在高风险
 * ❌ 6. 你的 LineListener 中使用 Thread.currentThread
 * ❌ 7. 播放结束判断逻辑不正确
 * ❌ 9. fadeOut 中 sleep 错误写成了 Thread.sleep(step)
 * ❌ 11. LineEventImpl 内部变量未按用途命名
 * 变量 framePosition、frameRate 没有清晰用途，日志也不准确
 * 评分：6.5分
 */