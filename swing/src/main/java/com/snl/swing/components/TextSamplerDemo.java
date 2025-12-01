package com.snl.swing.components;

import audio.MutipleMixer;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Objects;

public class TextSamplerDemo  extends JPanel implements ActionListener {

    protected String newLine = "\n";
    protected static final String textFieldString = "JTextField";
    protected static final String passwordFieldString  = "JPasswordField";
    protected static final String ftfString = "JFormattedTextField";
    protected static final String buttonString = "JButton";

    protected JLabel actionLabel;

    public TextSamplerDemo() {
        super(new BorderLayout());
        //初始化组件
        initComponents();
    }

    private void initComponents() {
        //创建普通文本字段
        JTextField jTextField = new JTextField(10);
        jTextField.setEditable(true);
        jTextField.setActionCommand(textFieldString);
        jTextField.addActionListener(this);

        //创建密码字段
        JPasswordField jPasswordField = new JPasswordField(10);
        jPasswordField.setActionCommand(passwordFieldString);
        jPasswordField.addActionListener(this);

        //创建格式化字段
        JFormattedTextField jFormattedTextField = new JFormattedTextField(
                Calendar.getInstance().getTime()
        );
        jFormattedTextField.setActionCommand(ftfString);
        jFormattedTextField.addActionListener(this);

        //为字段设置标签
        JLabel jTextLabel = new JLabel("普通字段");
        jTextLabel.setLabelFor(jTextField);
        JLabel passwordLabel = new JLabel("密码字段");
        passwordLabel.setLabelFor(jPasswordField);
        JLabel formattedLabel = new JLabel("格式化字段");
        formattedLabel.setLabelFor(jFormattedTextField);

        //设置操作日志label
        actionLabel = new JLabel("你输入了：");
        actionLabel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

        //布局文本空间个label
        JPanel textControlsPanel = new JPanel();
        //TODO 今天搞明白这个东西是什么？
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        textControlsPanel.setLayout(gridBagLayout);

        JLabel[] labels = new JLabel[]{jTextLabel,passwordLabel,formattedLabel};
        JTextField[] fields = new JTextField[]{jTextField,jPasswordField,jFormattedTextField};

        //TODO 布局设置
        addLabelTextRows(labels,fields,gridBagLayout,textControlsPanel);

        //TODO 添加label信息
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 1.0f;
        textControlsPanel.add(actionLabel,constraints);
        textControlsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("文本字段"),
                BorderFactory.createEmptyBorder(5,5,5,5)
        ));

        //创建右侧文本区域
        JTextArea jTextArea = new JTextArea("""
                这是一个可编辑的区域，一个文本区域是一个普通文本组件，
                这代表着他可以表示所有的字体，但是所有的文字必须是同一种字体。
                """);
        jTextArea.setFont(new Font("Serif",Font.ITALIC,15));
        jTextArea.setLineWrap(true);
        jTextArea.setWrapStyleWord(true);
        JScrollPane jScrollPaneTextArea = new JScrollPane(jTextArea);
        jScrollPaneTextArea.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        );
        jScrollPaneTextArea.setPreferredSize(new Dimension(250,250));
        jScrollPaneTextArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("普通文本"),
                        BorderFactory.createEmptyBorder(5,5,5,5)),
                jScrollPaneTextArea.getBorder()));

        //创建一个编辑器面板
        JEditorPane jEditorPane = createEditPanel();
        JScrollPane editScrollPanel = new JScrollPane(jEditorPane);
        editScrollPanel.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        );
        editScrollPanel.setPreferredSize(new Dimension(250,145));
        editScrollPanel.setMinimumSize(new Dimension(10,10));

        //TODO 创建风格化panel
        JTextPane textPane = createTextPanel();
        JScrollPane textScrollPanel = new JScrollPane(textPane);
        textScrollPanel.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        );
        textScrollPanel.setPreferredSize(new Dimension(250,155));
        textScrollPanel.setMinimumSize(new Dimension(10,10));

        //将可编辑面板和文本面板放入到分割面板中
//        JSplitPane jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,editScrollPanel,textScrollPanel);
//        jSplitPane.setOneTouchExpandable(true);
//        jSplitPane.setResizeWeight(0.5);
//        JPanel rightPanel = new JPanel(new GridLayout(1,0));
        JPanel rightPanel = new JPanel(new BorderLayout());
//        rightPanel.add(jSplitPane);
        rightPanel.add(textScrollPanel);
//        rightPanel.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createTitledBorder("文本面板"),
//                BorderFactory.createEmptyBorder(5,5,5,5)
//        ));

        //把所有事情都放在一起
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(editScrollPanel);
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(textControlsPanel,BorderLayout.PAGE_START);
        leftPanel.add(jScrollPaneTextArea,BorderLayout.CENTER);

        add(leftPanel,BorderLayout.LINE_START);
        add(centerPanel,BorderLayout.CENTER);
        add(rightPanel,BorderLayout.LINE_END);

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
        //TODO 1
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
        //TODO 2
        ImageIcon pigIcon = createIcon("images/Pig.gif","一个可爱的🐷");
        if (pigIcon != null) {
            StyleConstants.setIcon(s,pigIcon);
        }

        s = document.addStyle("button",regular);
        StyleConstants.setAlignment(s,StyleConstants.ALIGN_CENTER);
        ImageIcon soundIcon = createIcon("images/sound.gif","声音组件");

        JButton button = new JButton();
        if (soundIcon != null) {
            button.setIcon(soundIcon);
        }else {
            button.setText("点击我听歌");
        }

        button.setCursor(Cursor.getDefaultCursor());
        button.setMargin(new Insets(0,0,0,0));
        button.setActionCommand(buttonString);
        button.addActionListener(this);
        StyleConstants.setComponent(s, button);

    }

    private static ImageIcon createIcon(String path, String title) {
        URL imageUrl = null;
        try {
            imageUrl = Path.of(path).toUri().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        if (imageUrl != null) {
            return new ImageIcon(imageUrl,title);
        }else {
            System.err.println("文件没找到！请检查路径");
            return null;
        }
    }

    private JEditorPane createEditPanel() {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        URL helpUrl = null;
        try {
            helpUrl = Path.of("html/table.html").toUri().toURL();
            if (Objects.nonNull(helpUrl)) {
                editorPane.setPage(helpUrl);
            }
            else {
                System.err.println("不能找到html文件，请检查输入");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return editorPane;
    }


    private void addLabelTextRows(JLabel[] labels,JTextField[] fields,
                                  GridBagLayout gridBag,Container container)
    {
        //TODO对便签布局管理
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        int numbers = labels.length;

        for (int i = 0;i<numbers;i++) {
            c.gridwidth = GridBagConstraints.RELATIVE;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0.0;
            container.add(labels[i],c);

            c.gridwidth = GridBagConstraints.REMAINDER;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0f;
            container.add(fields[i],c);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String prefix = "你点击了……";
        if (textFieldString.equals(e.getActionCommand())) {
            JTextField source = (JTextField) e.getSource();
            actionLabel.setText(prefix+source.getText()+"\\  ");
        } else if (passwordFieldString.equals(e.getActionCommand())) {
            JPasswordField source = (JPasswordField) e.getSource();
            actionLabel.setText(prefix + new String(source.getPassword()) + "\\ ");
        } else if (buttonString.equals(e.getActionCommand())) {
            new Thread(MutipleMixer.playMusic(Path.of("musics/爱在西元前.wav")),"音乐播放者").start();
        }
    }

    public static void createUi() {
        JFrame frame = new JFrame("测试框架");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //添加民办
        frame.add(new TextSamplerDemo());
        frame.addWindowListener(new MyListener());
        frame.pack();
        frame.setFocusable(false);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            System.out.println("程序开始");
            createUi();
        });
    }

    private static class MyListener implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {
            System.out.println("窗口开启成功");
        }

        @Override
        public void windowClosing(WindowEvent e) {
            System.out.println("窗口正在关闭……");
        }

        @Override
        public void windowClosed(WindowEvent e) {
            System.out.println("窗口关闭成功");
            System.out.println("程序退出！");
            System.exit(0);
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
}
