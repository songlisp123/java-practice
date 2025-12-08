package com.snl.test.filechooser;

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
        } else if (f.getPath().matches(TextFileFilterDemo.pattern.pattern())) {
            return "文本格式";
        } else if (f.getPath().matches(PdfFileFilterDemo.pattern.pattern())) {
            return "pdf格式";
        } else if (f.getPath().matches(MusicFileFilter.pattern.pattern())) {
            return "音乐格式";
        }
        return null;
    }

    @Override
    public Icon getIcon(File f) {
        if (f.getPath().matches(ImageFilter.pattern.pattern())) {
            return new ImageIcon("palette.gif");
        } else if (f.getPath().matches(TextFileFilterDemo.pattern.pattern())) {
            return new ImageIcon("icons8-谷歌文档-32.png");
        } else if (f.getPath().matches(PdfFileFilterDemo.pattern.pattern())) {
            return new ImageIcon("icons8-pdf-48.png");
        } else if (f.getPath().matches(MusicFileFilter.pattern.pattern())) {
            return new ImageIcon("sound.gif");
        }
        return null;
    }

}
