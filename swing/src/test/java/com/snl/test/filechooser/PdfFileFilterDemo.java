package com.snl.test.filechooser;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.regex.Pattern;

public class PdfFileFilterDemo extends FileFilter {

    protected static final Pattern pattern =
            Pattern.compile(".+\\.pdf$");

    @Override
    public boolean accept(File f) {
        return f.getPath().matches(pattern.pattern());
    }

    @Override
    public String getDescription() {
        return "仅pdf文件";
    }
}
