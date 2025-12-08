package com.snl.test.filechooser;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.regex.Pattern;

public class ImageFilter extends FileFilter {

    protected static final Pattern pattern =
            Pattern.compile(".+\\.(jpg|png|gif|jepg|tif|tiff)$");

    @Override
    public boolean accept(File pathname) {
        return pathname.getPath().matches(pattern.pattern());
    }

    @Override
    public String getDescription() {
        return "仅图片";
    }
}
