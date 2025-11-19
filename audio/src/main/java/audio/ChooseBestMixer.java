package audio;

import javax.sound.sampled.*;
import java.util.Arrays;
import java.util.Objects;

public class ChooseBestMixer {

    public static Mixer chooseMixer() {

        Line.Info targetInfo = new Line.Info(SourceDataLine.class);
        Line.Info targetInfoClip = new Line.Info(Clip.class);

        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();

        for (int i=0;i< mixerInfo.length;i++) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo[i]);
            String name = mixerInfo[i].getName();
            if (Objects.equals(name,"主声音驱动程序")) {
                return mixer;
            }
        }
        return null;
    }

}
