package com.snl.swing.music.ui.filefilter;

import javax.swing.*;
import javax.swing.filechooser.FileView;
import java.io.File;

public class FileViewFilter extends FileView {


    @Override
    public Icon getIcon(File f) {
        if (f.getPath().matches(ImageFilter.pattern.pattern())) {
            return new ImageIcon("palette.gif");
        } else if (f.getPath().matches(TextFileFilter.pattern.pattern())) {
            return new ImageIcon("icons8-谷歌文档-32.png");
        } else if (f.getPath().matches(MusicFilter.pattern.pattern())) {
            return new ImageIcon("sound.gif");
        }
        return null;
    }
}
