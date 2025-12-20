package com.snl.test.filechooser;

import com.snl.swing.practice.button.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FileChooserDemo extends JFrame implements ActionListener {

    private JFileChooser fileChooser;
    private JButton button;

    public FileChooserDemo() {
        super("测试");

        button = new CustomButton("demo");
        button.addActionListener(this);

        fileChooser = new JFileChooser(System.getProperty("user.dir"));
        fileChooser.addChoosableFileFilter(new ImageFilter());
        fileChooser.addChoosableFileFilter(new TextFileFilterDemo());
        fileChooser.addChoosableFileFilter(new PdfFileFilterDemo());
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setFileView(new FileViewDemo());

        getContentPane().setBackground(Color.BLACK);
        getContentPane().setLayout(new BorderLayout());

        getContentPane().add(button,BorderLayout.PAGE_START);
        getContentPane().add(button,BorderLayout.PAGE_END);

        pack();
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int shown = fileChooser.showOpenDialog(this);
        if (shown == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile.exists()) {
                if (selectedFile.isFile()) {
                    System.out.println("挑选的是文件");
                }else {
                    System.out.println("挑选的是文件夹");
                }
            }else {
                System.err.println("错误的文件名字，请仔细检查！");
            }
        }else {
            System.out.println("用户关闭窗口");
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(FileChooserDemo::new);
    }
}
