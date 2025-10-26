package audio;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class CapturedAudio {
    public static void main(String[] args) throws LineUnavailableException, IOException {

        int total = 0;

        //获取该系统中安装的混音器
        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
        //获取所有安装的混音器
        int length = mixerInfo.length;
        //迭代每一个混音器
        for (Mixer.Info info : mixerInfo) {
            System.out.println("====================");
            System.out.println(info);
            //迭代每一个混音器
            Mixer mixer = AudioSystem.getMixer(info);
            total++;
            //获取混音器的位置
            System.out.printf("当前混音器的位置是：%d%n", total);
            //获取混音器的输出，在我们这个例子中，混音器是一个输入型混音器，它的输出端是targetLIne
            Line.Info[] targetLineInfo = mixer.getTargetLineInfo();
            for (Line.Info targetLine : targetLineInfo) {
                System.out.println(targetLine);
            }

        }
        //此程序演示捕获麦克风的声音
        TargetDataLine line;
        AudioFormat format = new AudioFormat(44100,16,2,true,false);

        Mixer mixer = AudioSystem.getMixer(mixerInfo[32]);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class,format);

        if (!mixer.isLineSupported(info)) {
            throw new IOException("此混音器不能捕获声音");
        }
        //获取并启东该省道
        line = (TargetDataLine) mixer.getLine(info);
        line.addLineListener(new LineEventImpl());
        line.open(format);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int numByteRead;
        long totalNumber = 0;
        boolean done = false;
        byte[] byteStorage =  new byte[8192];

        //开始音频捕获
        line.start();
        //设置初始时间
        var startTime = System.currentTimeMillis();




        while (System.currentTimeMillis() - startTime < 6000) {
            //从缓冲器读取字节流
            numByteRead = line.read(byteStorage,0,byteStorage.length);
            //写入到保存流
            stream.write(byteStorage,0,numByteRead);
            totalNumber += numByteRead;

        }
        line.close();

        System.out.printf("一共读取到：%d 字节数\n",totalNumber);

        //获取二进制流
        ByteArrayInputStream byteArrayInputStream =
                new ByteArrayInputStream(stream.toByteArray());

        //写入到新的.wav文件中
        AudioInputStream audioStream = new AudioInputStream(
                byteArrayInputStream,
                format,
                stream.size() / format.getFrameSize()
        );

        File outFile = new File("录音.wav");
        //向新文件写入二进制流
        AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, outFile);
        System.out.println("保存完成：" + outFile.getAbsolutePath());


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


}
