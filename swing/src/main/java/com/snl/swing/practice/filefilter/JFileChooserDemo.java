package com.snl.swing.practice.filefilter;

import javax.swing.*;

public class JFileChooserDemo extends JFileChooser {

    protected ImageFilter imageFilter;

    public JFileChooserDemo() {
        super(".");
        imageFilter = new ImageFilter();
        addChoosableFileFilter(imageFilter);
        addChoosableFileFilter(new MusicFilter());
        addChoosableFileFilter(new TextFileFilter());
        //TODO 定制更多过滤器

        setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        setAccessory(new JScrollPane(new ImagePreviewer(this)));
        setFileView(new FileViewFilter());

    }
}
