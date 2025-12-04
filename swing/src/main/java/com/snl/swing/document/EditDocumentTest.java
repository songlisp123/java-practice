package com.snl.swing.document;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class EditDocumentTest extends JFrame {
    /**
     * 重置内容面板
     */
    private JPanel jPanel;

    public EditDocumentTest(String title) {
        super(title);
        initComponents();
    }

    private void initComponents() {
        jPanel = new JPanel(new BorderLayout());
        //获取编辑器面板
        JEditorPane jEditorPane = new JEditorPane();
        Document document = jEditorPane.getDocument();
        if (document == null) {
            System.out.println("当前编辑器面板文档对象为null");
        }else {
            System.out.println("document = " + document);
        }


        try {
            URL path = Path.of("html/table.html").toUri().toURL();
            JEditorPane jEditorPane1 = new JEditorPane();
            jEditorPane1.setPage(path);
            Document document1 = jEditorPane1.getDocument();
            System.out.println("document1 = " + document1);
            JScrollPane jScrollPane1 = new JScrollPane(jEditorPane1);
            jScrollPane1.setPreferredSize(new Dimension(250,250));
            jScrollPane1.setMinimumSize(new Dimension(100,100));
            jPanel.add(jScrollPane1,BorderLayout.CENTER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JScrollPane jScrollPane = new JScrollPane(jEditorPane);
        jScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        );
        jScrollPane.setPreferredSize(new Dimension(250,250));
        jScrollPane.setMinimumSize(new Dimension(100,100));
        jPanel.add(jScrollPane,BorderLayout.LINE_START);

        add(jPanel);
    }

    private static void createUi() {
        EditDocumentTest frame = new EditDocumentTest("编辑器面板测试");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(EditDocumentTest::createUi);
    }
}
