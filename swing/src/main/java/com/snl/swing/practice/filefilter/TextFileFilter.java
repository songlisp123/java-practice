package com.snl.swing.practice.filefilter;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.regex.Pattern;

public class TextFileFilter extends FileFilter {

    protected static final Pattern pattern =
            Pattern.compile(".+\\.(txt|word|md)$");

    @Override
    public boolean accept(File f) {
        return f.getPath().matches(pattern.pattern());
    }

    @Override
    public String getDescription() {
        return "仅文本";
    }
}
