package com.snl.test;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.nio.file.Path;
import java.util.Objects;

public class StyleDocumentTest extends JFrame implements WindowListener {

    protected JPanel panel;
    private JTextPane textPane;

    public StyleDocumentTest(String title) {
        super(title);
        initComponents();
    }

    private void initComponents() {
        textPane = createPanel();
        getContentPane().add(textPane);
    }

    private JTextPane createPanel() {
        String[] initString = {
                "这是一个测试文件……\n",
                "这是另一个测试文件……\n",
                "这是第三个测试文件……\n",
                "这是第四个测试文件……\n",
                " ",    //这是一个图片组件，
                "\n 或者这是一个风格化的测试套件……"
        };
        String[] initStyles = {
                "regular",
                "italic",
                "bold",
                "small",
                "icon",
                "large"
        };

        JTextPane panel = new JTextPane();
        StyledDocument styledDocument = panel.getStyledDocument();

        addStyleDocument(styledDocument);

        //初始化文档
        try {
            for (int i=0;i<initString.length;i++) {
                styledDocument.insertString(styledDocument.getLength(),initString[i],
                        styledDocument.getStyle(initStyles[i]));
            }
        } catch (BadLocationException e) {
            System.err.println("出现错误:"+e.getMessage());
            e.printStackTrace();
        }

        return panel;

    }

    private void addStyleDocument(StyledDocument styledDocument) {
        //初始化样式
        Style def = StyleContext.getDefaultStyleContext()
                .getStyle(StyleContext.DEFAULT_STYLE);
        Style regular = styledDocument.addStyle("regular",def);
        StyleConstants.setFontFamily(def,"SansSerif");

        Style s = styledDocument.addStyle("italic",regular);
        StyleConstants.setItalic(s,true);

        s = styledDocument.addStyle("bold", regular);
        StyleConstants.setBold(s,true);

        s = styledDocument.addStyle("small",regular);
        StyleConstants.setFontSize(s,15);

        s = styledDocument.addStyle("icon",regular);
        Path path = Path.of("music.png");
        Icon icon = createIcon(path,"这是一个音乐播放器图片");
        if (Objects.nonNull(icon))
            StyleConstants.setIcon(s,icon);

        s = styledDocument.addStyle("large",regular);
        StyleConstants.setForeground(s,Color.GREEN);
        StyleConstants.setFontSize(s,25);

    }

    private Icon createIcon(Path path, String title) {
        return new ImageIcon(path.toString(),title);
    }

    private static void createUi() {
        StyleDocumentTest frame = new StyleDocumentTest("测试文本组件特性");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(StyleDocumentTest::createUi);
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
