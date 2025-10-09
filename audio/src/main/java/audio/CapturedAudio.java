package audio;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class CapturedAudio {
    public static void main(String[] args) throws LineUnavailableException, IOException {
        //此程序演示捕获麦克风的声音
        TargetDataLine line;
        AudioFormat format = new AudioFormat(44100,16,2,true,false);
        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
        Mixer mixer = AudioSystem.getMixer(mixerInfo[29]);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class,format);

        if (!mixer.isLineSupported(info)) System.out.println(1);

        //获取并启东该省道
        line = (TargetDataLine) mixer.getLine(info);
        line.addLineListener(new LineEventImpl());
        line.open(format);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int numByteRead;
        long total = 0;
        boolean done = false;
        byte[] byteStorage =  new byte[8192];

        //开始音频捕获
        line.start();
        //设置初始时间
        var startTime = System.currentTimeMillis();



        while (System.currentTimeMillis() - startTime < 60000) {
            //从缓冲器读取字节流
            numByteRead = line.read(byteStorage,0,byteStorage.length);
            //写入到保存流
            stream.write(byteStorage,0,numByteRead);
            total += numByteRead;

        }

        System.out.println("一共读取到：%d 字节数".formatted(total));

        //写入到新的.wav文件中
        AudioInputStream audioStream = new AudioInputStream(
                new ByteArrayInputStream(stream.toByteArray()),
                format,
                stream.size() / format.getFrameSize()
        );

        File outFile = new File("录音.wav");
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
