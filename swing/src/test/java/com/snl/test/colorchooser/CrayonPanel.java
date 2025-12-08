package com.snl.test.colorchooser;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

public class CrayonPanel extends AbstractColorChooserPanel implements ActionListener {

    JToggleButton redCrayon;
    JToggleButton yellowCrayon;
    JToggleButton greenCrayon;
    JToggleButton blueCrayon;

    @Override
    public void updateChooser() {
        System.out.println("更新画笔选中状态");
        Color color = getColorFromModel();
        if (Color.red.equals(color)) {
            redCrayon.setSelected(true);
        } else if (Color.yellow.equals(color)) {
            yellowCrayon.setSelected(true);
        } else if (Color.green.equals(color)) {
            greenCrayon.setSelected(true);
        }else {
            blueCrayon.setSelected(true);
        }
    }

    @Override
    protected void buildChooser() {
        System.out.println("初始化开始");
        setLayout(new GridLayout(0, 1));

        ButtonGroup boxOfCrayons = new ButtonGroup();
        Border border = BorderFactory.createEmptyBorder(4,4,4,4);

        redCrayon = createCrayon("red", border);
        boxOfCrayons.add(redCrayon);
        add(redCrayon);

        yellowCrayon = createCrayon("yellow", border);
        boxOfCrayons.add(yellowCrayon);
        add(yellowCrayon);

        greenCrayon = createCrayon("green", border);
        boxOfCrayons.add(greenCrayon);
        add(greenCrayon);

        blueCrayon = createCrayon("blue", border);
        boxOfCrayons.add(blueCrayon);
        add(blueCrayon);
        System.out.println("初始化完成");
    }

    private JToggleButton createCrayon(String name, Border border) {
        JToggleButton crayon  = new JToggleButton();
        crayon.setActionCommand(name);
        crayon.addActionListener(this);
        //设置图像
        ImageIcon icon = createImageIcon("images/" + name + ".gif");
        if (icon != null) {
            crayon.setIcon(icon);
            crayon.setToolTipText("The " + name + " crayon");
            crayon.setBorder(border);
        } else {
            crayon.setText("Image not found. This is the "
                    + name + " button.");
            crayon.setFont(crayon.getFont().deriveFont(Font.ITALIC));
            crayon.setHorizontalAlignment(JButton.HORIZONTAL);
            crayon.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }

        return crayon;

    }

    private ImageIcon createImageIcon(String s) {
        try {
            URL imgUrl = Path.of(s).toUri().toURL();
            return new ImageIcon(imgUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("点击画笔");
        Color newColor = null;
        var source = (JToggleButton)e.getSource();
        String actionCommand = source.getActionCommand();
        if ("green".equals(actionCommand)) {
            newColor = Color.green;
        } else if ("red".equals(actionCommand)) {
            newColor = Color.red;
        } else if ("yellow".equals(actionCommand)) {
            newColor = Color.YELLOW;
        } else if ("blue".equals(actionCommand)) {
            newColor = Color.blue;
        }
        System.out.println("画笔颜色 = " + newColor);
        System.out.println("触发模型事件");
        getColorSelectionModel().setSelectedColor(newColor);
    }

    @Override
    public String getDisplayName() {
        System.out.println(3);
        return "蜡笔";
    }

    @Override
    public Icon getSmallDisplayIcon() {
        System.out.println(4);
        return null;
    }

    @Override
    public Icon getLargeDisplayIcon() {
        System.out.println(5);
        return null;
    }
}
