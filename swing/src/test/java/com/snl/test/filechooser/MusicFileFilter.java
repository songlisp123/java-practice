package com.snl.test.filechooser;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.regex.Pattern;

public class MusicFileFilter extends FileFilter {

    protected static final Pattern pattern =
            Pattern.compile(".+\\.(map3|wav|flac)");

    @Override
    public boolean accept(File f) {
        return f.getPath().matches(pattern.pattern());
    }

    @Override
    public String getDescription() {
        return "音乐文件";
    }
}
