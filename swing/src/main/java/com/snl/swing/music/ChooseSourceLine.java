package com.snl.swing.music;

import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

public class ChooseSourceLine {

    public static SourceDataLine chooseLine(Mixer mixer) throws LineUnavailableException {
        SourceDataLine line = null;
        Line.Info[] sourceLineInfo = mixer.getSourceLineInfo();
        for(int i=0;i< sourceLineInfo.length;i++) {
            Line.Info info = sourceLineInfo[i];
            if (i == 0) {
                System.out.println(info);
                line = (SourceDataLine) mixer.getLine(info);
            }
        }
        return line;
    }

}
