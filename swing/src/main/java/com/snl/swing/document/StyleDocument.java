package com.snl.swing.document;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class StyleDocument extends JFrame {

    protected String newLine = "\n";
    private JPanel jPanel;

    public StyleDocument(String title) {
        super(title);
        initComponents();
    }

    private void initComponents() {
        jPanel = new JPanel(new BorderLayout());
        JTextPane textPane = createTextPanel();
//        textPane.setText("""
//                <html>
//                    <head></head>
//                    <body>
//                        <h1>这是一个一级标题</h1>
//                    </body>
//                </html>
//                """);
        Document document = textPane.getDocument();
        if (document instanceof DefaultStyledDocument) {
            System.out.println("document = " + document);
        }
        Element defaultRootElement = document.getDefaultRootElement();
        checkChildren(defaultRootElement);
        jPanel.add(textPane);
        add(jPanel);
    }

    private JTextPane createTextPanel() {
        String[] initString =
                { "这是一个继承子editpanel的文本面板, ",            //regular
                        "其他的事…… ",                                   //italic
                        "风格化的…… ",                                    //bold
                        "文本…… ",                                      //small
                        "关于组件……, ",                                //large
                        "可以使用内嵌式图标..." + newLine,//regular
                        " " + newLine,                                //button
                        "...和内嵌图像..." + newLine,         //regular
                        " ",                                          //icon
                        newLine + "JTextPane is a subclass of JEditorPane that " +
                                "uses a StyledEditorKit and StyledDocument, and provides " +
                                "cover methods for interacting with those objects."
                };
        String[] initStyles =
                { "regular", "italic", "bold", "small", "large",
                        "regular", "button", "regular", "icon",
                        "regular"
                };

        JTextPane textPane = new JTextPane();
        StyledDocument document = textPane.getStyledDocument();
        addStyleDocument(document);

        try {
            for (int i = 0;i<initString.length;i++) {
                document.insertString(document.getLength(),initString[i],
                        document.getStyle(initStyles[i]));
            }
        } catch (BadLocationException e) {
            System.err.println("发生异常，异常原因:"+e.getMessage());
        }
        return textPane;
    }

    private void addStyleDocument(StyledDocument document) {
        //初始化啊一些样式
        Style def = StyleContext.getDefaultStyleContext()
                .getStyle(StyleContext.DEFAULT_STYLE);
        Style regular = document.addStyle("regular", def);
        StyleConstants.setFontFamily(def,"SansSerif");

        Style s  = document.addStyle("italic",regular);
        StyleConstants.setItalic(s,true);

        s = document.addStyle("bold",regular);
        StyleConstants.setBold(s,true);

        s = document.addStyle("small",regular);
        StyleConstants.setFontSize(s,10);

        s = document.addStyle("large",regular);
        StyleConstants.setFontSize(s,15);

        s = document.addStyle("icon",regular);
        StyleConstants.setAlignment(s,StyleConstants.ALIGN_CENTER);
    }

    private<T extends Element> void checkChildren(T t) {
        if (!(t.isLeaf())) {
            int elementCount = t.getElementCount();
//            System.out.println("elementCount = " + elementCount);
            for (int i=0;i<elementCount;i++) {
                Element element = t.getElement(i);
                checkChildren(element);
            }
        }else {
            Element parentElement = t.getParentElement();
            System.out.println("t="+t);
            System.out.println("parentElement = " + parentElement);
        }
    }

    private static void createUi() {
        StyleDocument frame = new StyleDocument("编辑器面板测试");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(StyleDocument::createUi);
    }
}
