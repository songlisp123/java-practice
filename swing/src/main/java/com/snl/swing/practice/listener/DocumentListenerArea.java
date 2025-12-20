package com.snl.swing.practice.listener;

import com.snl.swing.practice.button.CustomButton;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

public class DocumentListenerArea extends JPanel implements DocumentListener, ActionListener {

    protected static final int ROWS = 5;
    protected static final int COLUMNS = 30;
    protected static final String DEFAULT_TEXT = "这是一个默认的文本";
    protected static final String newline = System.lineSeparator();
    protected CustomButton button;
    protected JTextArea area;
    public JLabel label;

    private static final Logger logger = Logger.getLogger("TextEditor");

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
        //TODO 搜索复杂
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateLog(e,"删除");
        // TODO 搜索复杂
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        //风格变化后调用此方法，但是这个例子中不会发生
        //TODO 风格变化实现的逻辑更复杂
        updateStyle(e);
//        System.out.println("风格发生变化");
    }

    private void updateStyle(DocumentEvent e) {
        StyledDocument document = (StyledDocument) e.getDocument();
        Element rootElement = document.getDefaultRootElement();
        if(e.getType() == DocumentEvent.EventType.CHANGE) {
            findElement(rootElement);
        }

    }

    private <T extends Element> void findElement(T t) {

        if (t instanceof AbstractDocument.BranchElement) {
            //如果有分支,获取所有分分支数量
            int elementCount = t.getElementCount();
            for (int i =0;i<elementCount;i++) {
                //便利所有子分值
                findElement(t.getElement(i));
            }
        }else {
            logger.info("子分枝是："+t);
        }
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
