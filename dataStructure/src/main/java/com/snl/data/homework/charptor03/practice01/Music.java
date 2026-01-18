//package com.snl.data.homework.charptor03.practice01;
//
//
//
//import javax.sound.sampled.*;
//import java.awt.*;
//import java.io.IOException;
//import java.nio.file.Path;
//import java.util.Objects;
//
//public class Music {
//
//    private static Mixer mixer;
//    private static SourceDataLine line;
//
//    static {
//        mixer = getMixer();
//    }
//
//
//    public static Mixer getMixer() {
////        Line.Info targetInfo = new Line.Info(SourceDataLine.class);
////        Line.Info targetInfoClip = new Line.Info(Clip.class);
//        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
//        for (Mixer.Info info : mixerInfo) {
//            Mixer mixer = AudioSystem.getMixer(info);
//            String name = info.getName();
//            if (Objects.equals(name, "主声音驱动程序")) {
//                return mixer;
//            }
//        }
//        return null;
//    }
//
//    public static void beep() {
//        Toolkit.getDefaultToolkit().beep();
//    }
//
//    public static void sniparShoot() {
//        Path path = Path.of(".","wav","狙击步枪.wav");
//        shoot(path);
//    }
//
//    public static void assaultShoot() {
//        Path path = Path.of(".","wav","突击步枪.wav");
//        shoot(path);
//    }
//
//    public static void pistolShoot() {
//        Path path = Path.of(".","wav","手枪射击.wav");
//        shoot(path);
//    }
//
//    public static void backGroundMusic() {
//        Path path = Path.of(".","oct.wav");
//        shoot(path);
//    }
//
//    public static void subMacheingShoot() {
//        Path path = Path.of(".","wav","冲锋枪射击.wav");
//        shoot(path);
//    }
//
//    public static void bulletsCrashWall() {
//        Path path = Path.of(".","wav","子弹撞墙.wav");
//        shoot(path);
//    }
//
//    public static void reload() {
//        Path path = Path.of(".","wav","装弹.wav");
//        shoot(path);
//    }
//
//    public static void drawTheSword() {
//        Path path = Path.of(".","wav","拔剑.wav");
//        shoot(path);
//    }
//
//    public static void swingSword() {
//
//        Path path = Path.of(".","wav","挥剑.wav");
//        Path path1 = Path.of(".","wav","挥剑02.wav");
//        Path path2 = Path.of(".","wav","挥剑03.wav");
//        Path[] paths = {path,path1,path2};
//        shoot(path);
//    }
//
//    public static void changeGun() {
//        Path path = Path.of(".","wav","枪更换.wav");
//        shoot(path);
//    }
//
//    public static void emptyBullets() {
//        Path path = Path.of(".","wav","空弹.wav");
//        shoot(path);
//    }
//
//    public static void lightSaber() {
//        Path path = Path.of(".","wav","光剑.wav");
//        shoot(path);
//    }
//
//    private static void shoot(Path path) {
//        new Thread(run(path),"音乐播放者").start();
//    }
//
//    private static Runnable run(Path path) {
//        return ()->{
//            try(AudioInputStream stream = AudioSystem.getAudioInputStream(path.toFile())) {
//                SourceDataLine sourceDataLine = ChooseSourceLine.chooseLine(mixer);
//                AudioFormat format = stream.getFormat();
//                sourceDataLine.open(format);
//                sourceDataLine.start();
//                //声音长度
//                var numberByteStore = new byte[4096];
//                int read = stream.read(numberByteStore, 0, 4096);
//                while (read != -1) {
//                    sourceDataLine.write(numberByteStore, 0, read);
//                    read = stream.read(numberByteStore, 0, 4096);
//                }
//                sourceDataLine.drain();
//                sourceDataLine.stop();
//                sourceDataLine.close();
//
//            } catch (UnsupportedAudioFileException e) {
//                throw new RuntimeException(e);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            } catch (LineUnavailableException e) {
//                throw new RuntimeException(e);
//            }
//
//        };
//    }
//
//
//}

/**
 * ai 优化后,解决了打包后声音加载的问题
 */
package com.snl.data.homework.charptor03.practice01;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class Music {
    // 定义随机数对象，挥剑随机播放音频用
    private static Mixer mixer;
    private static int count;
    private static boolean isCompleted;

    static {
        mixer = getMixer();
    }

    public static Mixer getMixer() {
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

    public static void sniparShoot() {
        playAudio("/wav/狙击步枪.wav");
    }

    public static void assaultShoot() {
        playAudio("/wav/突击步枪.wav");
    }

    public static void pistolShoot() {
        playAudio("/wav/手枪射击.wav");
    }

    // 注意：你的背景音乐文件是根目录的oct.wav，对应路径/wav/oct.wav 按你的实际存放修改
    public static void backGroundMusic() {
        playAudio("/oct.wav");
    }

    public static void subMacheingShoot() {
        playAudio("/wav/冲锋枪射击.wav");
    }

    public static void bulletsCrashWall() {
        playAudio("/wav/子弹撞墙.wav");
    }

    public static void reload() {
        playAudio("/wav/装弹.wav");
    }

    public static void drawTheSword() {
        playAudio("/wav/拔剑.wav");
    }

    // ✅ 修复：实现挥剑3个音频随机播放
    public static void swingSword() {
        String[] swordPaths = {"/wav/挥剑.wav", "/wav/挥剑02.wav", "/wav/挥剑03.wav"};
        playAudio(swordPaths[0]);
    }

    public static void changeGun() {
        playAudio("/wav/枪更换.wav");
    }

    public static void emptyBullets() {
        playAudio("/wav/空弹.wav");
    }

    public static void lightSaber() {
        playAudio("/wav/光剑.wav");
    }

    public static void smallKnife() {
        playAudio("/wav/匕首.wav");
    }

    public static void ding() {
        playAudio("/wav/叮.wav");
    }

    public synchronized static void back01() {
        playAudio("/wav/背景1.wav");
    }

    public synchronized static void jumping() {
        playAudio("/wav/跳跃.wav");
    }

    public static void backGround() {
        String[] strings = {"/wav/背景01.wav","/wav/背景02.wav","/wav/复古.wav","/wav/快速.wav","/wav/严肃.wav"};
        playAudio(strings[4 % strings.length]);
    }


    private static void playAudio(String audioResourcePath) {
        new Thread(() -> playAudioTask(audioResourcePath), "音乐播放者").start();
    }

    // ✅ 核心修复：完整的音频播放任务，兼容IDE+打包JAR，资源释放彻底
    private static void playAudioTask(String audioResourcePath) {
        AudioInputStream stream = null;
        SourceDataLine sourceDataLine = null;
        try {
            // ========== 关键修复1：读取jar包内音频资源的正确方式 ==========
            // 使用类加载器读取资源流，路径必须以 / 开头，对应src/main/resources下的目录结构
            InputStream resourceStream = Music.class.getResourceAsStream(audioResourcePath);
            if (resourceStream == null) {
                throw new RuntimeException("音频文件不存在！路径：" + audioResourcePath + "，请检查文件是否在resources目录下");
            }
            // 转成音频流，替代原来的 AudioSystem.getAudioInputStream(path.toFile())
            // ⭐ 关键修复：必须包一层 BufferedInputStream
            BufferedInputStream bufferedStream =
                    new BufferedInputStream(resourceStream);
            stream = AudioSystem.getAudioInputStream(bufferedStream);
            AudioFormat format = stream.getFormat();
            //获取音乐时长
            int frameSize = format.getFrameSize(); //总帧
            float frameRate = format.getFrameRate(); //每秒多少帧

            //获取总秒数
            float totalSeconds = frameSize / frameRate;

            // 初始化音频输出行
            sourceDataLine = ChooseSourceLine.chooseLine(mixer);
            sourceDataLine.open(format);
            sourceDataLine.start();

            // 读取音频数据并播放
            byte[] buffer = new byte[4096];
            int readLen;
            while ((readLen = stream.read(buffer, 0, buffer.length)) != -1) {
                sourceDataLine.write(buffer, 0, readLen);
            }
            // ========== 关键修复2：正确的关闭顺序，保证音频播放完整 ==========
            sourceDataLine.drain();  // 先排空缓冲区：等待所有数据全部播放完毕
            sourceDataLine.stop();   // 再停止播放
            sourceDataLine.close();  // 最后关闭音频行
        } catch (UnsupportedAudioFileException e) {
            System.err.println("音频格式不支持！请确保是wav格式：" + audioResourcePath);
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("音频文件读取失败：" + audioResourcePath);
            throw new RuntimeException(e);
        } catch (LineUnavailableException e) {
            System.err.println("音频设备被占用，无法播放声音：" + audioResourcePath);
            throw new RuntimeException(e);
        } finally {
            // ========== 关键修复3：强制释放所有资源，防止内存泄漏 ==========
            try { if (stream != null) stream.close(); } catch (IOException e) {}
            try { if (sourceDataLine != null && sourceDataLine.isOpen()) sourceDataLine.close(); } catch (Exception e) {}
        }
    }

    public static boolean isIsCompleted() {
        return isCompleted;
    }
}
