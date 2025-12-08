package com.snl.test.colorchooser;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorChooserDemo2 extends JPanel implements ChangeListener, ActionListener {

    protected JColorChooser colorChooser;
    protected JLabel banner;
    protected JButton button;

    public ColorChooserDemo2() {
        super(new BorderLayout());

        banner = new JLabel("你好，世界！",JLabel.CENTER);
        banner.setForeground(Color.YELLOW);
        banner.setBackground(Color.BLUE);
        //下一步必须
        banner.setOpaque(true);
        banner.setFont(new Font("SansSerif", Font.BOLD, 24));
        banner.setPreferredSize(new Dimension(100, 65));

        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.add(banner,BorderLayout.CENTER);
        jPanel.setBorder(BorderFactory.createTitledBorder("横幅"));

        button = new JButton("颜色选择器");
        button.addActionListener(this);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(button);
        buttonPanel.setBorder(BorderFactory.createTitledBorder("颜色选择按钮"));

        colorChooser = new JColorChooser(new SimpleColorModelDemo());
        colorChooser.getSelectionModel().addChangeListener(this);
        colorChooser.setBorder(BorderFactory.createTitledBorder("选择颜色"));
        //从这一步分叉

        //移除预览面板
        colorChooser.setPreviewPanel(new JPanel());

        //TODO 自定义选择面板
        AbstractColorChooserPanel[] panels = {new CrayonPanel()};
        colorChooser.setChooserPanels(panels);
        colorChooser.setColor(banner.getForeground());

        add(jPanel,BorderLayout.PAGE_START);
        add(colorChooser,BorderLayout.PAGE_END);

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        System.out.println("处理事件变更逻辑开始");
        Color color = colorChooser.getColor();
        banner.setForeground(color);
        var source =(SimpleColorModelDemo) e.getSource();
        System.out.print("颜色是否相同：");
        System.out.println(source.color == color);
        System.out.println("处理事件变更逻辑结束");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Color color = JColorChooser.showDialog(
                this,
                "选择背景颜色",
                banner.getBackground()
        );

        if (color != null) {
            banner.setBackground(color);
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("ColorChooserDemo2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new ColorChooserDemo2();
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
