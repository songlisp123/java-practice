package com.snl.swing.practice;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class JFileChooserDemo extends JFileChooser {

    protected final FileFilter filter = new FileNameExtensionFilter(
            "图像文件","png","ipg","gif","jpeg");

    protected final FileFilter musicFilter = new FileNameExtensionFilter("音乐文件",
            "mp3","flac","wav"
    );

    public JFileChooserDemo() {
        super(".");
        setFileFilter(filter);
        addChoosableFileFilter(musicFilter);
        setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        setAccessory(new JScrollPane(new ImagePreviewer(this)));
        setFileView(new imageView(filter, new ImageIcon(".//palette.gif")));
    }
}
