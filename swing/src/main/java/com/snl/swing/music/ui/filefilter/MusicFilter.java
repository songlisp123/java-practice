package com.snl.swing.music.ui.filefilter;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.regex.Pattern;

public class MusicFilter extends FileFilter {

    protected static final Pattern pattern =
            Pattern.compile(".+\\.(mp3|wav|flac)$");

    @Override
    public boolean accept(File f) {
        return f.getPath().matches(pattern.pattern());
    }

    @Override
    public String getDescription() {
        return "仅音乐";
    }
}
