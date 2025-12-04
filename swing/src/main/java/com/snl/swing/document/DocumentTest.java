package com.snl.swing.document;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.Arrays;

public class DocumentTest extends JFrame {

    private JPanel jPanel;

    public DocumentTest(String title) {
        super(title);
        initComponents();
    }

    private void initComponents() {
        jPanel = new JPanel();
        //创建一个文本字段
        JTextArea jTextField = new JTextArea(10,10);
        jTextField.setText("""
                这是一个的是？
                这是第二行
                这是第三行？
                """);
        //获取文档对象
        PlainDocument document = (PlainDocument) jTextField.getDocument();
        System.out.println("document = " + document);
        //获取文档的根元素
        Element defaultRootElement = document.getDefaultRootElement();
        System.out.println("defaultRootElement = " + defaultRootElement);
        String name = defaultRootElement.getName();
        System.out.println("name = " + name);
        checkElement(defaultRootElement);

        //插入字符串观察节点数量
        try {
            document.insertString(2,"新插入的字符串\n",new SimpleAttributeSet());
            checkElement(defaultRootElement);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }

        //测试删除效果,观察节点数量
        try {
            document.remove(3,15);
            checkElement(defaultRootElement);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
        jTextField.setEditable(true);
        jPanel.add(jTextField);
        add(jPanel);
    }

    public static void createUi() {
        DocumentTest frame = new DocumentTest("文本文档对象测试");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(DocumentTest::createUi);
    }

    protected <T extends Element> void checkElement(T t) {
        System.out.println("***********************************");
        if (t instanceof AbstractDocument.BranchElement) {
            int elementCount = t.getElementCount();
            System.out.println("elementCount = " + elementCount);
            if (elementCount > 1) {
                for (int i = 0;i<elementCount;i++) {
                    Element element = t.getElement(i);
                    System.out.println("element = " + element);
                    //获取内容
                    String elementName = element.getName();
                    System.out.println("elementName = " + elementName);
                }
            }
        }else {
            Element parentElement = t.getParentElement();
            System.out.println("parentElement = " + parentElement);
        }
        System.out.println("***********************************");
    }

}
