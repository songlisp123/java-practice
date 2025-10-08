package audio;


import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AudioTest {
    public static void main(String[] args) {
        try {
            // 加载音频流（让系统自己解析格式）
            File file = new File("test.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);

            // 获取可播放的音频片段
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            System.out.println("正在播放音频...");
            clip.start();

            // 防止程序提前结束
            Thread.sleep(clip.getMicrosecondLength() / 1000);

            clip.close();
            audioStream.close();

        } catch (UnsupportedAudioFileException e) {
            System.err.println("不支持的音频格式！");
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.err.println("音频线路不可用！");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("读取文件失败！");
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
