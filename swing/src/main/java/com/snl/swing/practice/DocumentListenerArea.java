package com.snl.swing.practice;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DocumentListenerArea extends JPanel implements DocumentListener, ActionListener {
    protected static final int ROWS = 5;
    protected static final int COLUMNS = 30;
    protected static final String DEFAULT_TEXT = "这是一个默认的文本";
    protected static final String newline = System.lineSeparator();
    protected CustomButton button;
    protected JTextArea area;
    protected JLabel label;

    public DocumentListenerArea() {
        super(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        area = new JTextArea(ROWS,COLUMNS);
        area.setEditable(false);
        area.setForeground(Color.GREEN);
        area.setBackground(Color.black);
        button = new CustomButton("清空");
        button.addActionListener(this);
        label = new JLabel("这是默认文本");
        add(area,BorderLayout.CENTER);
        add(button,BorderLayout.PAGE_END);

    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateLog(e,"插入");
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateLog(e,"删除");
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        //风格变化后调用此方法，但是这个例子中不会发生
        //TODO 风格变化
        System.out.println("风格发生变化");
    }

    private void updateLog(DocumentEvent e, String state) {
        Document document = e.getDocument();
        int offset = e.getOffset();
        int length = e.getLength();
        area.append(length + "字符" + state + "\t" +
                "文本长度："+document.getLength() + "\t" +
                "首次更改："+offset + newline);
        label.setText("一共 %d 字符".formatted(document.getLength()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Document document = area.getDocument();
        int length = document.getLength();
        if (length > 0) {
            try {
                document.remove(0,length);
            } catch (BadLocationException ex) {
                System.err.println("出现❌信息ℹ️："+ex.getMessage());
                ex.printStackTrace();
            }
        }else {
            try {
                document.insertString(0,"暂无要删除的日志",new SimpleAttributeSet());
            } catch (BadLocationException ex) {
                System.err.println("出现❌信息ℹ️："+ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

}
