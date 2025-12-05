package com.snl.swing.practice;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static com.snl.swing.practice.DocumentListenerArea.*;

public class KeyListenLogTextArea  extends JPanel implements KeyListener, ActionListener {

    protected CustomButton button;
    protected JTextArea area;

    public KeyListenLogTextArea() {
        super(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        area = new JTextArea(ROWS,COLUMNS);
        area.setEditable(false);
        area.setForeground(Color.CYAN);
        area.setBackground(Color.black);
        button = new CustomButton("清空");
        button.addActionListener(this);
        add(area,BorderLayout.CENTER);
        add(button,BorderLayout.PAGE_END);

    }

    @Override
    public void keyTyped(KeyEvent e) {
        displayInfo(e,"插入");
    }



    @Override
    public void keyPressed(KeyEvent e) {
        displayInfo(e,"按下");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        displayInfo(e,"释放");
    }
    private void displayInfo(KeyEvent e, String state) {
        //获取事件的id
        int eID = e.getID();
        String keyString;
        if (eID == KeyEvent.KEY_TYPED) {
            //如果是按下事件
            char keyChar = e.getKeyChar();
            keyString = "键盘字符：["+keyChar + "]";
        }else {
            int keyCode = e.getKeyCode();
            keyString = "keyCode = " + keyCode +
                    "[" + KeyEvent.getKeyText(keyCode) + "]";
        }

        String actionKey = "是否是功能键:  ";
        if (e.isActionKey()) {
            actionKey += "是✅";
        }else {
            actionKey += "否❌";
        }

        int modifiersEx = e.getModifiersEx();
        String modString = "扩展修饰符：" + modifiersEx;
        String temString = KeyEvent.getModifiersExText(modifiersEx);
        if (temString.length() > 0) {
            modString += "[" + temString + "]";
        }else {
            modString += "{没有预期的扩展修饰符}";
        }

        //键位值
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

        area.append(state + newline
                + "    " + keyString + newline
                + "    " + modString + newline
                + "    " + actionKey + newline
                + "    " + locationString + newline);
        area.setCaretPosition(area.getDocument().getLength());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Document document = area.getDocument();
        int length = document.getLength();
        if (length > 0) {
            try {
                document.remove(0,length);
            } catch (BadLocationException ex) {
                System.err.println("出现❌信息ℹ️："+ex.getMessage());
                ex.printStackTrace();
            }
        }else {
            try {
                document.insertString(0,"暂无要删除的日志",new SimpleAttributeSet());
            } catch (BadLocationException ex) {
                System.err.println("出现❌信息ℹ️："+ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}
