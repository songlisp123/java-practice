package com.snl.swing.practice;

import audio.MutipleMixer;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.nio.file.Path;
import java.util.Arrays;


public class IInsertComponents extends JComboBox<String> implements ItemListener,ActionListener {

    protected String[] options;
    protected JTextPane pane;
    protected int port;

    public IInsertComponents() {
        options = new String[]{"button","label","colorChooser"};
        Arrays.stream(options).forEach(this::addItem);
        this.addItemListener(this);
    }

    public JTextPane getPane() {
        return pane;
    }

    public void setPane(JTextPane pane) {
        this.pane = pane;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        String item = (String) e.getItem();
        if (e.getStateChange() == ItemEvent.SELECTED) {
            System.out.println("当前选项："+item);
//            insertComponents(item);
        }else {
            System.out.println("上一个选项：" + item);
        }
    }

    private void insertComponents(String item) {
        if (pane != null) {
            StyledDocument styledDocument = pane.getStyledDocument();
            Style style = styledDocument.getStyle(item);
            if (style == null) {
                //如果没有风格
                style = styledDocument.addStyle(item, null);
            }
            if (item.equals(options[0])) {
                //是按钮
                ImageIcon icon = createIcon("jack.gif");
                JButton button = new JButton("播放音乐🎵", icon);
                button.setCursor(Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                ));
                button.addActionListener(this);
                StyleConstants.setAlignment(style,StyleConstants.ALIGN_CENTER);
                StyleConstants.setComponent(style, button);


            }else if (item.equals(options[1])){
                ImageIcon icon = createIcon("Ours_en_peluche_-_15.jpg");
//                JLabel label = new JLabel("片段", icon, JLabel.CENTER);
//                label.setCursor(Cursor.getPredefinedCursor(
//                        Cursor.HAND_CURSOR
//                ));
//                label.setForeground(Color.GREEN);
//                label.setBackground(Color.BLUE);
//                label.setPreferredSize(new Dimension(150,80));
//                label.setOpaque(true);
                StyleConstants.setIcon(style,icon);
            }else {
                JColorChooser jColorChooser = new JColorChooser(Color.RED);
                jColorChooser.setCursor(Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                ));
                StyleConstants.setComponent(style,jColorChooser);
            }
            try {
                styledDocument.insertString(port, " ", style);
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private ImageIcon createIcon(String path) {
        return   new ImageIcon(path);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new Thread(MutipleMixer.playMusic(Path.of("爱在西元前.wav")),"音乐播放器").start();
    }


}
