package com.snl.test.key;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyEventDemo extends JFrame implements ActionListener , KeyListener {

    protected JTextField field;
    protected JTextArea area;

    protected JButton button;
    protected static final String newline = System.lineSeparator();

    public KeyEventDemo(String title)  {
        super(title);
    }

    private void initComponents() {
        button = new JButton("清除");
        button.addActionListener(this);

        field = new JTextField(20);
        field.setEditable(true);

        field.addKeyListener(this);

        area = new JTextArea();
        area.setEditable(false);

        JScrollPane jScrollPane = new JScrollPane(area);
        jScrollPane.setPreferredSize(new Dimension(375,125));

        getContentPane().add(field,BorderLayout.PAGE_START);
        getContentPane().add(jScrollPane,BorderLayout.CENTER);
        getContentPane().add(button,BorderLayout.PAGE_END);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //处理按钮事件
        field.setText("");
        area.setText("");

        //使输入框获取焦点
        field.requestFocusInWindow();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        displayInfo(e,"输入字符");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        displayInfo(e,"按下键");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        displayInfo(e,"释放键");
    }

    private void displayInfo(KeyEvent e, String keyStatus) {
        int eID = e.getID();
        String keyString;
        if (eID == KeyEvent.KEY_TYPED) {
            char c = e.getKeyChar();
            keyString = "键盘字符：["+c + "]";
        }else {
            int keyCode = e.getKeyCode();
            keyString = "keyCode = " + keyCode +
                    "[" + KeyEvent.getKeyText(keyCode) + "]";
        }

        int modifiersEx = e.getModifiersEx();
        String modString = "扩展修饰符：" + modifiersEx;
        String temString = KeyEvent.getModifiersExText(modifiersEx);
        if (temString.length() > 0) {
            modString += "[" + temString + "]";
        }else {
            modString += "{没有预期的扩展修饰符}";
        }

        String keyAction = "动作事件?";
        if (e.isActionKey()) {
            keyAction += "YES";
        }else {
            keyAction += "NO";
        }

        String locationString = "键位置：";
        int location = e.getKeyLocation();
        if (location == KeyEvent.KEY_LOCATION_STANDARD) {
            locationString += "标准键盘";
        } else if (location == KeyEvent.KEY_LOCATION_LEFT) {
            locationString += "左侧";
        } else if (location == KeyEvent.KEY_LOCATION_RIGHT) {
            locationString += "右侧";
        } else if (location == KeyEvent.KEY_LOCATION_NUMPAD) {
            locationString += "小键盘";
        } else { // (location == KeyEvent.KEY_LOCATION_UNKNOWN)
            locationString += "未知？";
        }
        area.append(keyStatus + newline
                + "    " + keyString + newline
                + "    " + modString + newline
                + "    " + keyAction + newline
                + "    " + locationString + newline);
        area.setCaretPosition(area.getDocument().getLength());
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        KeyEventDemo frame = new KeyEventDemo("键盘事件demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set up the content pane.
        frame.initComponents();


        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(KeyEventDemo::createAndShowGUI);
    }
}
