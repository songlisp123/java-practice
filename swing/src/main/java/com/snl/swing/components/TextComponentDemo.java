package com.snl.swing.components;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.HashMap;

public class TextComponentDemo extends JFrame implements WindowListener {

    private JTextPane jTextPane;
    private AbstractDocument doc;
    private static final int MAX_CHARS = 300;
    private JTextArea changeLog;
    private String newLine = "\n";
    protected HashMap<Object,Action> actions;

    //TODO 撤销重做【✅ 完成】
    protected UndoAction undoAction;
    private RedoAction redoAction;
    protected UndoManager undo = new UndoManager();


    //重做，撤销操作
    public TextComponentDemo(String title)  {
        super(title);

        //创建内容面板，并填充
        jTextPane = new JTextPane();
        jTextPane.setCaretPosition(0);
        jTextPane.setMargin(new Insets(5,5,5,5));
        StyledDocument styledDocument = jTextPane.getStyledDocument();
        if (styledDocument instanceof AbstractDocument) {
            doc = (AbstractDocument) styledDocument;
            //TODO 设置文件过滤器【❌ 未完成】

        }else {
            System.err.println("该组件并不是抽象文档");
            System.exit(-1);
        }
        JScrollPane scrollPane = new JScrollPane(jTextPane);
        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        );
        scrollPane.setPreferredSize(new Dimension(250,250));
        scrollPane.setMinimumSize(new Dimension(100,100));

        //创建文字区域存放日志
        changeLog = new JTextArea(5,30);
        changeLog.setEditable(false);
        JScrollPane Log = new JScrollPane(changeLog);

        //创建分割面板存放日志记录器和日志生成器
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, Log);
        splitPane.setOneTouchExpandable(true);

        //创建状态区域
        JPanel statusPanel = new JPanel(new GridLayout(1,1));
        //TODO 设置光标状态监听器[✅ 完成]
        CaretListenerLabel label = new CaretListenerLabel("光标状态栏");
        statusPanel.add(label);
        getContentPane().add(splitPane,BorderLayout.CENTER);
        getContentPane().add(statusPanel,BorderLayout.PAGE_END);

        //获取当前文件组件的所有操作对象，并将操作对象存放如action字典中
        actions = createTableActions(jTextPane);
        //创建编辑菜单
        JMenu editMenu = createEditMenu();
        //创建style菜单项
        JMenu styleMenu = createStyleMenu();
        //创建菜单栏
        JMenuBar jMenuBar = new JMenuBar();
        jMenuBar.add(editMenu);
        jMenuBar.add(styleMenu);
        setJMenuBar(jMenuBar);

        //TODO 添加键绑定[✅ 完成]
        addBindings();
        //TODO 初始化文本[✅ 完成]
        initStringTextPanel();
        jTextPane.setCaretPosition(0);
        //TODO 监控撤销和重做操作【✅ 完成】
        doc.addUndoableEditListener(new MyUndoableEditListener());
        //TODO 文本面板监听光标事件【✅ 完成】
        jTextPane.addCaretListener(label);
        //TODO 文本面板监听文档监听器【✅ 完成】
        doc.addDocumentListener(new DocumentListenerImplement());
        //TODO 添加窗口监控器【✅ 完成】
        this.addWindowListener(this);

    }

    private void initStringTextPanel() {
        String[] initString = {
                "使用鼠标控制防止光标",
                "使用编辑按钮修剪，拷贝，复制和剪切文本",
                "同时测试撤销和重做",
                "使用风格菜单栏改变文本的风格",
                "使用键盘上的箭头键或这些 Emacs 键绑定来移动插入点",
                "ctrl-b,ctrl-p,ctrl-f,ctrl-n"
        };
        //TODO 初始化文本风格[✅ 完成]
        SimpleAttributeSet[] attributeSets = initAttributes(initString.length);
        try {
            for(int i =0; i<initString.length;i++) {
                doc.insertString(doc.getLength(),initString[i]+newLine,attributeSets[i]);
            }
        } catch (BadLocationException e) {
            System.err.println(e.getMessage());
        }
    }

    private SimpleAttributeSet[] initAttributes(int length) {
        //硬编码一些属性
        SimpleAttributeSet[] attrs = new SimpleAttributeSet[length];

        attrs[0] = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attrs[0],"SansSerif");
        StyleConstants.setFontSize(attrs[0],15);

        attrs[1] = new SimpleAttributeSet(attrs[0]);
        StyleConstants.setBold(attrs[1], true);

        attrs[2] = new SimpleAttributeSet(attrs[0]);
        StyleConstants.setItalic(attrs[2], true);

        attrs[3] = new SimpleAttributeSet(attrs[0]);
        StyleConstants.setFontSize(attrs[3], 20);

        attrs[4] = new SimpleAttributeSet(attrs[0]);
        StyleConstants.setFontSize(attrs[4], 12);

        attrs[5] = new SimpleAttributeSet(attrs[0]);
        StyleConstants.setForeground(attrs[5], Color.red);

        return attrs;
    }

    private void addBindings() {
        //获取文本组件的输入映射
        InputMap inputMap = jTextPane.getInputMap();
        //ctrl-b退回一个
        KeyStroke keyStroke =
                KeyStroke.getKeyStroke(KeyEvent.VK_B, Event.CTRL_MASK);
        inputMap.put(keyStroke,DefaultEditorKit.backwardAction);
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

    private JMenu createStyleMenu() {
        JMenu menu = new JMenu("风格");

        Action action = new StyledEditorKit.BoldAction();
        action.putValue(Action.NAME,"粗体");
        menu.add(action);

        action = new StyledEditorKit.ItalicAction();
        action.putValue(Action.NAME,"斜体");
        menu.add(action);

        action = new StyledEditorKit.UnderlineAction();
        action.putValue(Action.NAME,"下划线");
        menu.add(action);

        menu.addSeparator();

        menu.add(new StyledEditorKit.FontSizeAction("12",12));
        menu.add(new StyledEditorKit.FontSizeAction("14",14));
        menu.add(new StyledEditorKit.FontSizeAction("18",18));

        menu.addSeparator();

        menu.add(new StyledEditorKit.FontFamilyAction("xx","Serif"));
        menu.add(new StyledEditorKit.FontFamilyAction("另一种字体","SansSerif"));

        menu.addSeparator();

        menu.add(new StyledEditorKit.ForegroundAction("红色",Color.RED));
        menu.add(new StyledEditorKit.ForegroundAction("蓝色",Color.BLUE));
        menu.add(new StyledEditorKit.ForegroundAction("绿色",Color.GREEN));
        menu.add(new StyledEditorKit.ForegroundAction("灰色",Color.GRAY));
        menu.add(new StyledEditorKit.ForegroundAction("桃红色",Color.MAGENTA));

        return menu;
    }

    private JMenu createEditMenu() {
        JMenu editMenu = new JMenu("编辑");

        //TODO 代做撤销和重做【✅ 完成】
        undoAction = new UndoAction();
        editMenu.add(undoAction);

        redoAction = new RedoAction();
        editMenu.add(redoAction);

        editMenu.addSeparator();

        //添加默认的操作对象
        editMenu.add(getAction(DefaultEditorKit.cutAction));
        editMenu.add(getAction(DefaultEditorKit.copyAction));
        editMenu.add(getAction(DefaultEditorKit.pasteAction));

        editMenu.addSeparator();

        editMenu.add(getAction(DefaultEditorKit.selectAllAction));
        return editMenu;
    }

    private HashMap<Object, Action> createTableActions(JTextPane jTextPane) {
        HashMap<Object,Action> map = new HashMap<>();
        //获取所有actions
        Action[] actionArray = jTextPane.getActions();
        Arrays.stream(actionArray).forEach(action -> map.put(action.getValue(Action.NAME),action));
        return map;
    }

    private Action getAction(String name) {
        return actions.get(name);
    }

    @Override
    public void windowOpened(WindowEvent e) {
        changeLog.append("✅ 窗口开启\n");
    }

    @Override
    public void windowClosing(WindowEvent e) {
        changeLog.append("窗口正在关闭……\n");
    }

    @Override
    public void windowClosed(WindowEvent e) {
        changeLog.append("窗口关闭\n");
        changeLog.append("程序结束\n");
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

    protected class CaretListenerLabel extends JLabel implements CaretListener {

        public CaretListenerLabel(String text) {
            super(text);
        }

        @Override
        public void caretUpdate(CaretEvent e) {
//            System.out.println("光标更新");
            displaySelectionInfo(e.getDot(),e.getMark());
        }

        private void displaySelectionInfo(int dot, int mark) {
            SwingUtilities.invokeLater(()->{
                if (dot == mark) {
                    //未选择内容
                    try {
                        Rectangle2D rectangle2D = jTextPane.modelToView2D(dot);
                        //TODO 将当前光标定位到视图坐标 [✅ 完成]
                        setText("光标：文本位置：" + dot + ",视图位置：[" +
                                rectangle2D.getX() + ", " + rectangle2D.getY() +
                                "]" + newLine);
                    } catch (BadLocationException e) {
                        System.err.println("发生错误……"+e.getMessage());
                        setText("光标：文本位置："+dot + newLine);
                    }
                }else if(dot < mark) {
                    //从前向后选
                    setText("选择范围：[" +dot + ", "+ mark + "]" + newLine);
                } else {
                    //从后向前选
                    setText("选择范围：[" +mark + ", "+ dot + "]" + newLine);
                }
            });
        }
    }

    protected class MyUndoableEditListener implements UndoableEditListener {

        @Override
        public void undoableEditHappened(UndoableEditEvent e) {
            undo.addEdit(e.getEdit());
            undoAction.updateUndoState();
            redoAction.updateRedoState();
        }
    }

    protected class DocumentListenerImplement implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            displayInfo(e,"插入");
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            displayInfo(e,"删除");
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            displayInfo(e,"更新");
        }

        private void displayInfo(DocumentEvent e, String state) {
            Document document = e.getDocument();
            int length = e.getLength();
            changeLog.append(state + ":" +
                    length + "字符" + "\t" + "文本长度：["+ document.getLength()
            +"]" + newLine);
        }
    }

    protected class UndoAction extends AbstractAction {
        public UndoAction() {
            super("撤销");
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            undo.undo();
            updateUndoState();
            redoAction.updateRedoState();
        }

        private void updateUndoState() {
            if (undo.canUndo()) {
                setEnabled(true);
                putValue(Action.NAME,undo.getUndoPresentationName());
            }else {
                setEnabled(false);
                putValue(Action.NAME,"撤销");
            }
        }
    }

    protected class RedoAction extends AbstractAction {

        public RedoAction() {
            super("重做");
            setEnabled(false);
            undoAction.updateUndoState();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            undo.redo();
            updateRedoState();
        }

        private void updateRedoState() {
            if (undo.canRedo()) {
                setEnabled(true);
                putValue(Action.NAME,undo.getRedoPresentationName());
            }else {
                setEnabled(false);
                putValue(Action.NAME,"重做");
            }
        }
    }

    private static void createUi() {
        TextComponentDemo frame = new TextComponentDemo("测试文本组件特性");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(TextComponentDemo::createUi);
    }
}
