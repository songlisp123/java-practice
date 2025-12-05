package com.snl.swing.practice;

import audio.MutipleMixer;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

public class MainPanel extends JPanel implements ActionListener, CaretListener {

    protected JPanel jPanel;
    protected JButton button;
    protected CustomBoxTest customBoxTest;
    protected FontComBoxDemo fontComBoxDemo;
    protected IInsertComponents iInsertComponents;
    protected ColorChooserDemo colorChooserDemo;
    protected String answer;
    protected EditTextPanel editTextPanel;
    protected int port;
    protected JButton insertPictureButton;
    protected JFileChooserDemo jFileChooserDemo;
    protected CaretDemo caretDemo;
    protected TitleDemo titleDemo;

    public MainPanel() {
        super(new BorderLayout());
        alignPanel();
        initComponents();
        //添加键绑定
        addBindings();
    }

    private void alignPanel() {
        jPanel = new JPanel();
        jPanel.setBorder(BorderFactory.createTitledBorder("编辑区"));
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        jPanel.setLayout(gridBagLayout);

        customBoxTest = new CustomBoxTest();
        JLabel fontSizeLabel = new JLabel("文字大小: ");
        fontSizeLabel.setLabelFor(customBoxTest);

        fontComBoxDemo = new FontComBoxDemo();
        JLabel fontTypeLabel = new JLabel("字体类型:");
        fontTypeLabel.setLabelFor(fontComBoxDemo);

        button = new JButton("插入组件");
        iInsertComponents = new IInsertComponents();
        JLabel componentType = new JLabel("组件类型:");
        componentType.setLabelFor(iInsertComponents);

        colorChooserDemo = new ColorChooserDemo();

        insertPictureButton = new JButton("插入图片");

        titleDemo = new TitleDemo();
        JLabel titleTabel = new JLabel("文字标题");
        titleTabel.setLabelFor(titleDemo);


        constraints.anchor = GridBagConstraints.EAST;


        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.NONE;
        jPanel.add(titleTabel,constraints);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0.5;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0,10,0,10);
        jPanel.add(titleDemo,constraints);

        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.NONE;
        jPanel.add(fontSizeLabel,constraints);
        constraints.gridx = 3;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0.5;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0,10,0,10);
        jPanel.add(customBoxTest,constraints);

        constraints.gridx = 4;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.NONE;
        jPanel.add(fontTypeLabel,constraints);
        constraints.gridx = 5;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0.5;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0,10,0,10);
        jPanel.add(fontComBoxDemo,constraints);

//        constraints.gridx = 4;
//        constraints.gridwidth = 1;
//        constraints.weightx = 0.0;
//        constraints.fill = GridBagConstraints.NONE;
//        jPanel.add(componentType,constraints);
//        constraints.gridx = 5;
//        constraints.weightx = 1.0;
//        constraints.insets = new Insets(0,5,0,5);
//        constraints.fill = GridBagConstraints.HORIZONTAL;
//        jPanel.add(iInsertComponents,constraints);

        constraints.gridx = 6;
        constraints.weightx = 1.0;
        constraints.insets = new Insets(0,5,0,5);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        button.addActionListener(this);
        jPanel.add(button,constraints);


        constraints.gridx = 7;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.insets = new Insets(0,5,0,5);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0f;
        insertPictureButton.addActionListener(new OpenFileChooser());
        jPanel.add(insertPictureButton,constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.weightx = 1.0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0,5,0,5);
        jPanel.add(colorChooserDemo,constraints);

    }

    private void initComponents() {

        DocumentListenerArea documentListenerArea =
                new DocumentListenerArea();
        KeyListenLogTextArea keyListenLogTextArea =
                new KeyListenLogTextArea();

        JScrollPane jScrollPane = wrapToScrollPanel(documentListenerArea);
        JScrollPane jScrollPane2 = wrapToScrollPanel(keyListenLogTextArea);


        editTextPanel = new EditTextPanel();
        if (caretDemo == null) {
            caretDemo = new CaretDemo(2);
        }
        editTextPanel.getPane().setCaretColor(Color.WHITE);
        editTextPanel.getPane().setCaret(caretDemo);
        editTextPanel.setBorder(
                BorderFactory.createTitledBorder("书写区")
        );
        editTextPanel.getPane().getDocument().addDocumentListener(documentListenerArea);
        editTextPanel.getPane().addKeyListener(keyListenLogTextArea);
        editTextPanel.getPane().addCaretListener(customBoxTest);
        editTextPanel.getPane().addCaretListener(fontComBoxDemo);
        editTextPanel.getPane().addCaretListener(colorChooserDemo);
        editTextPanel.getPane().addCaretListener(this);
        editTextPanel.getPane().addCaretListener(titleDemo);

        customBoxTest.setT(editTextPanel.getPane());
        fontComBoxDemo.setT(editTextPanel.getPane());
        iInsertComponents.setPane(editTextPanel.getPane());
        colorChooserDemo.setPane(editTextPanel.getPane());
        titleDemo.setPane(editTextPanel.getPane());



        JScrollPane wrappedToScrollPanel = wrapToScrollPanel(editTextPanel);
        wrappedToScrollPanel.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        JSplitPane splitPanel02 =
                new JSplitPane(JSplitPane.VERTICAL_SPLIT, jPanel, wrappedToScrollPanel);
        splitPanel02.setOneTouchExpandable(false);
        splitPanel02.setEnabled(true);
        splitPanel02.setResizeWeight(0.2);

        JSplitPane jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                jScrollPane2, jScrollPane);
        jSplitPane.setOneTouchExpandable(true);
        jSplitPane.setResizeWeight(0.65);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jSplitPane, splitPanel02);
        jSplitPane.setOneTouchExpandable(true);
        jSplitPane.setResizeWeight(0.5);
        add(documentListenerArea.label,BorderLayout.PAGE_END);
        add(splitPane,BorderLayout.CENTER);
    }

    protected <T extends JComponent> JScrollPane wrapToScrollPanel(T t) {
        JScrollPane jScrollPane = new JScrollPane(t);
        jScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        );
        jScrollPane.setPreferredSize(new Dimension(200,200));
        jScrollPane.setMinimumSize(new Dimension(200,200));
        return jScrollPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        System.out.println("点击按钮");
        //TODO 点击事件
//        new Thread(MutipleMixer.playMusic(Path.of("爱在西元前.wav")),"音乐播放者").start();
        Object[] pos = {"button","textArea","textFiled","_NULL_"};
         answer = (String)JOptionPane.showInputDialog(
                this,
                "请选择要添加的组件",
                "选择组件",
                JOptionPane.PLAIN_MESSAGE,
                new ImageIcon("music.png"),
                pos,
                pos[0]
        );
        StyledDocument styledDocument = editTextPanel.getPane().getStyledDocument();
        Style style = styledDocument.getStyle("music");
        if (style==null) {
            style = styledDocument.addStyle("music",null);
        }
        if (Objects.nonNull(answer)) {
            if (answer.equals(pos[0])) {
                ImageIcon icon = createIcon("sound.gif");
                JButton button = new JButton("播放音乐🎵", icon);
                button.setCursor(Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                ));
                button.addActionListener(new MyListenImplement());
                StyleConstants.setAlignment(style, StyleConstants.ALIGN_CENTER);
                StyleConstants.setComponent(style, button);
            } else if (answer.equals(pos[1])) {
                JTextArea jTextArea = new JTextArea(5, 30);
                JScrollPane jScrollPane = new JScrollPane(jTextArea);
                jScrollPane.setHorizontalScrollBarPolicy(
                        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
                );
                jScrollPane.setPreferredSize(new Dimension(200,200));
                StyleConstants.setComponent(style,jScrollPane);
            } else if (answer.equals(pos[2])) {
                JTextField jTextField = new JTextField(10);
                StyleConstants.setAlignment(style,StyleConstants.ALIGN_CENTER);
                StyleConstants.setComponent(style,jTextField);
            }
            try {
                styledDocument.insertString(port, " ", style);
            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            }
        }
//        else {
//            JColorChooser jColorChooser = new JColorChooser(Color.RED);
//            jColorChooser.setCursor(Cursor.getPredefinedCursor(
//                    Cursor.HAND_CURSOR
//            ));
//            StyleConstants.setComponent(style,jColorChooser);

//        }

    }

    private ImageIcon createIcon(String path) {
        ImageIcon icon = new ImageIcon(path);
//        if (icon.getIconWidth() > getWidth() * 0.5)
//            icon = new ImageIcon(icon.getImage().getScaledInstance(
//                    getWidth(), -1, Image.SCALE_SMOOTH));
//        else {
//            icon = new ImageIcon(icon.getImage().getScaledInstance(
//                    getWidth(), -1, Image.SCALE_SMOOTH));
//        };
        if (icon.getIconHeight() > 50 && icon.getIconWidth() > 50) {
            icon = new ImageIcon(icon.getImage().getScaledInstance(
                    (int) (icon.getIconWidth() * 0.25),
                    (int) (icon.getIconHeight() * 0.25),
                    Image.SCALE_SMOOTH
            ));
        }
        return icon;
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        port = e.getDot();
    }

    private void addBindings() {
        //获取文本组件的输入映射
        InputMap inputMap = editTextPanel.getPane().getInputMap();
        //ctrl-b退回一个
        KeyStroke keyStroke =
                KeyStroke.getKeyStroke(KeyEvent.VK_B, Event.CTRL_MASK);
        inputMap.put(keyStroke, DefaultEditorKit.backwardAction);
        //ctrl-f回到下一个
        keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.CTRL_MASK);
        inputMap.put(keyStroke,DefaultEditorKit.forwardAction);

        //ctrl-p跳到上一行
        keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_P,Event.CTRL_MASK);
        inputMap.put(keyStroke,DefaultEditorKit.upAction);

        //ctrl-n 调到下一行
        keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_N,Event.CTRL_MASK);
        inputMap.put(keyStroke,DefaultEditorKit.downAction);
    }

    protected class MyListenImplement implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (jFileChooserDemo == null) jFileChooserDemo = new JFileChooserDemo();
            int answer = jFileChooserDemo.showOpenDialog(null);
            if (answer == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jFileChooserDemo.getSelectedFile();
                System.out.println("selectedFile = " + selectedFile);
                if (selectedFile.isFile() && selectedFile.toString().endsWith(".wav")) {
                    new Thread(MutipleMixer.playMusic(Path.of(selectedFile.getPath())),"🎵播发器")
                            .start();
                }else {
                    System.err.println("所挑选的不是音乐文件，请重试！");
                }
            }else {
                System.out.println("取消");
            }
        }
    }

    protected class OpenFileChooser implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            StyledDocument styledDocument = editTextPanel.getPane().getStyledDocument();
            Style style = styledDocument.getStyle("icon");
            if (style == null) {
                style = styledDocument.addStyle("icon",null);
            }
            StyleConstants.setAlignment(style,StyleConstants.ALIGN_RIGHT);
            //TODO 文件选择
            if (jFileChooserDemo == null) jFileChooserDemo = new JFileChooserDemo();
            int answer = jFileChooserDemo.showOpenDialog(null);
            if (answer == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jFileChooserDemo.getSelectedFile();
                if (selectedFile != null) {
                    ImageIcon icon = createIcon(selectedFile.getPath());
                    StyleConstants.setIcon(style,icon);
                    try {
                        styledDocument.insertString(port," ",style);
                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }else {
                System.out.println("取消");
            }
        }
    }
}
