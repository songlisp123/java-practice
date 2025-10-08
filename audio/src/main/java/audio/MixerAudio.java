package audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

public class MixerAudio {
    public static void main(String[] args){
        //首先创建输出路径
        File file = new File("test.wav");

        try(AudioInputStream stream = AudioSystem.getAudioInputStream(file)) {
            DataLine.Info info = new DataLine.Info(Clip.class,stream.getFormat());
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
//            Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
//            Mixer mixer = AudioSystem.getMixer(mixerInfo[1]);
//            for (Line.Info info1 : mixer.getSourceLineInfo()) {
//                System.out.println(info1);
//            }
//
//            for (Line.Info info1 : mixer.getTargetLineInfo()) {
//                System.out.println(info1);
//            }
            System.out.println(fileFormat.getType());
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("错误通道");
                throw new NoSuchElementException();
            }
//            if (!mixer.isLineSupported(info)) {
//                System.out.println("错误通道");
//                throw new NoSuchElementException();
//            }
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(stream);
            clip.start();
            Thread.sleep(100000L);

        } catch (UnsupportedAudioFileException | LineUnavailableException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

//public class MixerAudio {
//    public static void main(String[] args) {
//        File file = new File("test.wav");
//
//        try (AudioInputStream inputStream = AudioSystem.getAudioInputStream(file)) {
//            AudioFormat format = inputStream.getFormat();
//            DataLine.Info info = new DataLine.Info(Clip.class, format);
//
//            if (!AudioSystem.isLineSupported(info)) {
//                System.err.println("不正确的通道");
//                throw new NoSuchElementException();
//            }
//
//            Clip line = (Clip) AudioSystem.getLine(info);
//            line.open(inputStream);
//            line.start();
//
//            // 等待播放结束
//            Thread.sleep(line.getMicrosecondLength() / 1000);
//            line.close();
//
//        } catch (UnsupportedAudioFileException e) {
//            System.err.println("文件不是有效的音频文件");
//            e.printStackTrace();
//        } catch (LineUnavailableException e) {
//            System.err.println("音频设备不可用");
//            e.printStackTrace();
//        } catch (IOException e) {
//            System.err.println("文件读取错误");
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//}
