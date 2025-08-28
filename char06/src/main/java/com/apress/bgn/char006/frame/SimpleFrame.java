package com.apress.bgn.char006.frame;



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleFrame extends JFrame {
    private static final int DEFAULT_WIDTH = 500;
    private static final int DEFAULT_HEIGHT =400;
    private JPanel panel;

    public SimpleFrame() {
//        var d = new Dimension(200,100);
        //Tsuper.setSize(d);
        this.panel = new JPanel();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
//        super.setSize(d);
        //居中框架
        int x = (screenWidth-DEFAULT_WIDTH) / 2;
        int y = (screenHeight-DEFAULT_HEIGHT) / 2;
        setBounds(x,y,DEFAULT_WIDTH,DEFAULT_WIDTH);
//        Component mycomponent = new MyComponent();
//        mycomponent.setBackground(Color.YELLOW);
//        this.add(mycomponent);
//        setBackground(new Color(150,25,90));

        //设置三个监听器对象
//        var listener01 = new ColorAction(Color.YELLOW);
//        var listener02 = new ColorAction(Color.GREEN);
//        var listener03 = new ColorAction(Color.RED);
//
//        //设置三个按钮
//        var button01 = new JButton("黄色");
//        var button02 = new JButton("绿色");
//        var button03 = new JButton("红色");
//         安装监听器到按钮上
//        button01.addActionListener(listener01);
//        button02.addActionListener(listener02);
//        button03.addActionListener(listener03);
//
//        将按钮添加到面板上
//        ColorAction.buttonPanel.add(button01);
//        ColorAction.buttonPanel.add(button02);
//        ColorAction.buttonPanel.add(button03);
//        ColorAction.buttonPanel.add(mycomponent);

        //将按钮面板添加到框架上
//        this.add(ColorAction.buttonPanel);

        //设置按钮面板的方向
//        ColorAction.buttonPanel.setSize(150,150);

//        makeButton("黄色",Color.YELLOW);
//        makeButton("绿色",Color.GREEN);
//        makeButton("蓝色",Color.BLUE);
//        this.add(panel);
//
//        //设置窗口事件类监听器
//        WindowListener l1 = new WindowClosed();
//        this.addWindowListener(l1);

        //创建一个WindowStateListener接口
//        WindowStateListener l2 = (enevt) -> System.out.println("窗口被操作时候调用！");
//        this.addWindowListener();

        //这是一个action接口，拓展了addActionListener接口，你可以在任何使用addActionListener
        //的地方使用Action接口，以下是接口内方法
        /*
        void actionPerformed(ActionEvent event)
        void setEnabled(boolean b)
        boolean isEnabled()
        void putValue(String key, Object value)
        Object getValue(String key)
        void addPropertyChangeListener(PropertyChangeListener listener)
        void removePropertyChangeListener(PropertyChangeListener
        listener)
         */
        //有一个类继承了Action接口，AbstractAction 类实现了除actionPerformed方法
        //之外的方法，你只需要继承它即可,就可以使用拓展了addActionListener接口了

        //使用抽象类AbstractAction实现的类
        Myaction action = new Myaction(Color.YELLOW);

        makeButton("黄色",Color.YELLOW);
        makeButton("绿色",Color.GREEN);
        makeButton("蓝色",Color.BLUE);
        var button = new JButton(action);
        button.addActionListener(action);
        panel.add(button);
        this.add(panel);

//        //设置窗口事件类监听器
//        WindowListener l1 = new WindowClosed();
//        this.addWindowListener(l1);

        //绘制顶层组件
//        InputMap im = panel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
//        im.put(KeyStroke.getKeyStroke("ctrl Y"),"yellow");
//        ActionMap aop = panel.getActionMap();
//        aop.put("yellow",action);



    }

    public void makeButton(String name,Color backgroud) {
        var button = new JButton(name);
        button.addActionListener(event->{
            panel.setBackground(backgroud);
        });
        panel.add(button);
    }

    private class WindowClosed extends WindowAdapter {
        public void windowClosing(WindowEvent event) {
            System.exit(2);
        }

        public void windowIconified(WindowEvent event) {
            System.out.println("窗口最小化！");
        }
    }

    private class Myaction extends AbstractAction {

        public Myaction(Color c) {
            super.putValue(Action.NAME,"黄色");
            super.putValue("color",c);
            super.putValue(Action.SHORT_DESCRIPTION,"将面板设置为黄色");

        }
        public void actionPerformed(ActionEvent event) {
            Color c = (Color) super.getValue("color");
            panel.setBackground(c);
            System.out.println("1！");
        }
    }
}
