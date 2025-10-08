package audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

public class MutipleMixer {

    public static void main(String[] args) {
        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
        Mixer mixer = AudioSystem.getMixer(mixerInfo[19]);

        Line.Info[] sourceLineInfo = mixer.getSourceLineInfo();
        try(AudioInputStream stream=AudioSystem.getAudioInputStream(new File("test.wav")))
        {
            AudioFormat format = stream.getFormat();
            SourceDataLine line = (SourceDataLine) mixer.getLine(sourceLineInfo[0]);
            DataLine.Info info = new DataLine.Info(Clip.class,format);
            Clip line2 = (Clip) AudioSystem.getLine(info);

            Line[] lines = new Line[]{line,line2};

            if (!mixer.isSynchronizationSupported(lines,true)) {
                System.out.println("不能并行播放！此混声器不能用于并行播放");
            }

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
    }

    private static class LineEventImpl implements LineListener {
        @Override
        public void update(LineEvent event) {
            if (event.getType() == LineEvent.Type.STOP) {
                if (event.getFramePosition() >= 19) {
                    System.out.println("音乐正常结束");
                } else {
                    System.out.println("音乐被暂停或者终止");
                }
            }

            if (event.getLine().isOpen()) {
                System.out.println(LocalDateTime.now());
            }
        }
    }
}
