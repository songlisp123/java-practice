package com.snl.swing.homework01.ui.fileCHooser;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.regex.Pattern;

public class ImageFilter extends FileFilter {

    public static final Pattern pattern =
            Pattern.compile(".+\\.(jpg|png|gif|jepg|tif|tiff)$");

    @Override
    public boolean accept(File pathname) {
        return pathname.isDirectory() ||
                pathname.getPath().matches(pattern.pattern());
    }

    @Override
    public String getDescription() {
        return "仅图片";
    }
}
