package com.snl.swing.button;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ButtonDemo extends JButton implements ActionListener {


    public ButtonDemo(String name) {
        super(name);
        setUI(new ButtonUiImplement());
        //不绘制默认背景
        setContentAreaFilled(false);
        //设置无焦点
        setFocusPainted(true);
        //背景透明
        setOpaque(true);
//        setBackground(Color.black);
        addActionListener(this);
    }

    @Override
    public void paintComponents(Graphics g) {
        ButtonUI ui = getUI();
        if (ui != null) {
            //调用ui的paint
            ui.update(g,this);
        }
        throw new NoSuchElementException("暂无很多元素");

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300,20);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("文本编辑器");

        var b = new ButtonDemo("测试");
        var button = new JButton("你好");
        var model = new ButtonModelImplement();

        b.setModel(model);

        button.setModel(model);
        button.addActionListener(b);
        frame.add(b,BorderLayout.PAGE_START);
        frame.add(button,BorderLayout.PAGE_END);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(200,50);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
            System.out.println("e = " + e);
            repaint(getBounds());
    }

    class ButtonUiImplement extends ButtonUI {

        @Override
        public void update(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.CYAN);
            paint(g2,c);
            System.out.println(2);
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            super.paint(g, c);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.fillRoundRect(0,0,getWidth(),getHeight(),15,15);
            g2.setColor(Color.red);
            g2.drawString(getText(),getWidth() / 2,getHeight() / 2);
            g2.dispose();
        }
    }

   static class ButtonModelImplement implements ButtonModel,ActionListener {

        private boolean selected;
        private boolean enabled;
        private boolean pressed;
        private boolean rollover;
        private boolean armed;
        private int key;
        private String actionMan;
        private final List<ActionListener> listeners =
                new ArrayList<>();

        public ButtonModelImplement() {
            selected = false;
            enabled = true;
            pressed = false;
            rollover = false;
            armed = true;
            actionMan = "";
            addActionListener(this);
        }

        @Override
        public boolean isArmed() {
            System.out.println("是否准备触发： " + armed);
            return armed;
        }

        @Override
        public boolean isSelected() {
            //默认情况下这是button的默认情况
            System.out.println("是否选择 = " + selected);
            return selected;
        }

        @Override
        public boolean isEnabled() {
            System.out.println("是否允许 = " + enabled);
            return enabled;
        }

        @Override
        public boolean isPressed() {
            System.out.println("是否按压 = " + pressed);
            return pressed;
        }

        @Override
        public boolean isRollover() {
            System.out.println("rollover = " + rollover);
            return rollover;
        }

        @Override
        public void setArmed(boolean b) {
            System.out.println("armed = " + b);
            armed = b;
        }

        @Override
        public void setSelected(boolean b) {
            System.out.println("selected = " + b);
            selected = b;
        }

        @Override
        public void setEnabled(boolean b) {
            System.out.println("enabled = " + b);
            enabled = b;
        }

        @Override
        public void setPressed(boolean b) {
            System.out.println("process = " + b);
            pressed = b;
            if(b) {
                fireEvent();
            }
        }

       private void fireEvent() {
           ActionEvent actionEvent = new ActionEvent(this, 2, actionMan);
           for (ActionListener listener : listeners) {
               listener.actionPerformed(actionEvent);
           }
       }

       @Override
        public void setRollover(boolean b) {
            System.out.println("rollover = " + b);
            rollover = b;
        }

        @Override
        public void setMnemonic(int key) {
            System.out.println("key = " + key);
            this.key = key;
        }

        @Override
        public int getMnemonic() {
            return key;
        }

        @Override
        public void setActionCommand(String s) {
            actionMan = s;
        }

        @Override
        public String getActionCommand() {
            return actionMan;
        }

        @Override
        public void setGroup(ButtonGroup group) {
            //以下操作按钮并不需要
        }

        @Override
        public void addActionListener(ActionListener l) {
            listeners.add(l);
        }

        @Override
        public void removeActionListener(ActionListener l) {
            listeners.remove(l);
        }

        @Override
        public Object[] getSelectedObjects() {
            return new Object[0];
        }

        @Override
        public void addItemListener(ItemListener l) {

        }

        @Override
        public void removeItemListener(ItemListener l) {

        }

        @Override
        public void addChangeListener(ChangeListener l) {

        }

        @Override
        public void removeChangeListener(ChangeListener l) {

        }

       @Override
       public void actionPerformed(ActionEvent e) {
           System.out.println("触发时间");
       }
   }
}
