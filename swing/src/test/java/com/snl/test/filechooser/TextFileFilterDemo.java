package com.snl.test.filechooser;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.regex.Pattern;

public class TextFileFilterDemo extends FileFilter {

    protected static final Pattern pattern =
            Pattern.compile(".+\\.txt$");

    @Override
    public boolean accept(File f) {
        return f.getPath().matches(pattern.pattern());
    }

    @Override
    public String getDescription() {
        return "仅文本";
    }
}
