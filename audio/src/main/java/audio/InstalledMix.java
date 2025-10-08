package audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

public class InstalledMix {
    public static void main(String[] args) throws InterruptedException {
//        int count = 0;
//        for (Mixer.Info info : AudioSystem.getMixerInfo()) {
//
//            Mixer mixer = AudioSystem.getMixer(info);
//            System.out.println(info+"  "+count);
//            count++;
//            System.out.println("===源线路信息===");
//            for (Line.Info info1 : mixer.getSourceLineInfo()) {
//                System.out.println(info);
//                System.out.println(info1);
//            }
//            System.out.println("===目标线路信息===");
//            for (Line.Info info1 : mixer.getTargetLineInfo()) {
//                System.out.println(info);
//                System.out.println(info1);
//            }
//
//        }
        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
        Mixer mixer = AudioSystem.getMixer(mixerInfo[19]);

        Line.Info[] sourceLineInfo = mixer.getSourceLineInfo();

        try(AudioInputStream stream=AudioSystem.getAudioInputStream(new File("test.wav")))
        {

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
            while (read != -1 ) {
                line.write(numberByteStore,0,read);
                read = stream.read(numberByteStore,0,12);
            }
            line.close();
        } catch (UnsupportedAudioFileException | LineUnavailableException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //使用扬声器混合器



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
        }
    }
}
