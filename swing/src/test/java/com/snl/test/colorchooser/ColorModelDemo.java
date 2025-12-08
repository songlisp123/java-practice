package com.snl.test.colorchooser;

import javax.swing.*;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ColorModelDemo extends JPanel implements ColorSelectionModel {

    protected JColorChooser colorChooser;
    protected JLabel banner;
    protected Color color;
    protected final Color Default = Color.CYAN;
    protected final List<ChangeListener> listeners = new ArrayList<>();

    public ColorModelDemo() {
        super(new BorderLayout());
        //创建横幅
        banner = new JLabel("你好世界！",JLabel.CENTER);
        banner.setForeground(Color.YELLOW);
        banner.setBackground(Color.BLUE);
        //下一步必须
        banner.setOpaque(true);
        banner.setFont(new Font("SansSerif", Font.BOLD, 24));
        banner.setPreferredSize(new Dimension(100, 65));

        colorChooser = new JColorChooser(this);
//        this.addChangeListener(this);
        colorChooser.setBorder(BorderFactory.createTitledBorder("颜色选择器"));

        add(banner,BorderLayout.CENTER);
        add(colorChooser,BorderLayout.PAGE_END);

    }

    @Override
    public Color getSelectedColor() {
        return color == null ? Default:color;
    }

    @Override
    public void setSelectedColor(Color color) {
        System.out.println("设置颜色："+color);
        this.color = color;
        banner.setForeground(this.color);
        fireStateChanged();
    }

    private void fireStateChanged() {
        System.out.println("触发事件！");
        ChangeEvent event = new ChangeEvent(this);
        for (ChangeListener listener : listeners) {
            //调用方法
            listener.stateChanged(event);
        }
    }

    @Override
    public void addChangeListener(ChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }

//    @Override
//    public void stateChanged(ChangeEvent e) {
//        Color color1 = colorChooser.getColor();
//        System.out.print("挑选的颜色是否一样？答案是：");
//        System.out.println(this.color == color1);
//    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("ColorChooserDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new ColorModelDemo();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
