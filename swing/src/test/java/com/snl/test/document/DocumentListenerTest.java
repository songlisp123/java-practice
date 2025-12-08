package com.snl.test.document;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DocumentListenerTest extends JPanel implements DocumentListener {

    private JTextArea area;
    private final String newLine = "\n";
    private JButton button;


    public DocumentListenerTest() {
        super(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        area = new JTextArea(5,30);
        button = new JButton("清除");

        button.addActionListener(new MyActionListener());
        area.setEditable(false);
        this.add(area,BorderLayout.CENTER);
        this.add(button,BorderLayout.PAGE_END);
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
    }

    private void updateLog(DocumentEvent e, String action) {
        Document document = e.getDocument();
        int offset = e.getOffset();
        int length = e.getLength();
        area.append(length + "字符" + action + "\t" +
                "文本长度："+document.getLength() + "\t" +
                "首次更改："+offset + newLine);
    }

    private static void createUi(){
        JFrame frame = new JFrame("测试文档监听器");
        DocumentListenerTest documentListenerTest = new DocumentListenerTest();
        JScrollPane jScrollPane = new JScrollPane(documentListenerTest);
        jScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        );
        JTextArea jTextArea = new JTextArea(5,30);
        jTextArea.setLineWrap(true);
        jTextArea.setWrapStyleWord(true);
        jTextArea.getDocument().addDocumentListener(documentListenerTest);
        JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jTextArea, jScrollPane);
        jSplitPane.setOneTouchExpandable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(jSplitPane,BorderLayout.CENTER);
        frame.setBounds(200,50,300,300);
        frame.setVisible(true);
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(DocumentListenerTest::createUi);
    }

    protected class MyActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int length = area.getDocument().getLength();
            if (length > 0) {
                try {
                    area.getDocument().remove(0,length);
                } catch (BadLocationException ex) {
                    System.err.println("发生异常，异常原因："+ex.getMessage());
                    area.append("异常："+ex.getMessage() + newLine);
                }
            }else {
                area.append("当前尚未有日志记录"+newLine);
            }
        }
    }
}
