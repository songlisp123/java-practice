package com.snl.test.image.homework01;

import javax.swing.*;
import javax.swing.filechooser.FileView;
import java.io.File;

public class FileViewDemo extends FileView {

    public FileViewDemo() {
        super();
    }

    @Override
    public String getTypeDescription(File f) {
        if (f.getPath().matches(ImageFilter.pattern.pattern())) {
            return "图片格式";
        }
        return null;
    }

    @Override
    public Icon getIcon(File f) {
        if (f.getPath().matches(ImageFilter.pattern.pattern())) {
            return new ImageIcon("palette.gif");
        }
        return null;
    }

}
