package com.snl.test.display;

import com.snl.swing.practice.button.CustomButton;
import com.snl.test.frame.util.Utils;
import javax.swing.*;
import java.awt.*;

public class DisplayModeTest extends JPanel implements DisplayModeInterface {

    JComboBox comboBox;
    static JFrame frame;
    JButton fullScreenButton;
    JButton exitFullScreenButton;
    GraphicsDevice gd;

    public DisplayModeTest() {
        initial();
    }

    //初始化
    private void initial() {
        setBackground(Color.black);
        //获取图形设备
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gd = ge.getDefaultScreenDevice();
        //下拉框模型
        var model = new ComBoxModelImplement();
        model.addDisplayListener(this);
        //下拉框
        comboBox = new JComboBox<>(model);
        //全屏模式按钮
        fullScreenButton = new CustomButton("全屏");
        fullScreenButton.setToolTipText("全屏模式");
        fullScreenButton.addActionListener(e -> {
            if(gd != null)
            {
                if (gd.isFullScreenSupported())
                {
                    DisplayMode newMode = getSelectModel();
                    gd.setFullScreenWindow(frame);
                    gd.setDisplayMode(newMode);
                }
            }
        });

        exitFullScreenButton = new CustomButton("退出全屏");
        exitFullScreenButton.setToolTipText("退出全屏模式");
        exitFullScreenButton.addActionListener(e -> {
            if (gd.isDisplayChangeSupported()) {
                gd.setDisplayMode((DisplayMode) comboBox.getSelectedItem());
                gd.setFullScreenWindow(null);
            }
        });

        add(comboBox);
        add(fullScreenButton);
        add(exitFullScreenButton);
    }

    private DisplayMode getSelectModel() {
        var d = (DisplayMode) comboBox.getSelectedItem();
        int width = d.getWidth();
        int height = d.getHeight();
        int bitDepth = d.getBitDepth();
        int refresh = DisplayMode.REFRESH_RATE_UNKNOWN;
        return new DisplayMode(width,height,bitDepth,refresh);
    }

    @Override
    public Dimension getPreferredSize() {
        var currentDisplayModel = (DisplayMode)comboBox.getSelectedItem();
        return new Dimension(currentDisplayModel.getWidth()
                ,currentDisplayModel.getHeight());
    }

    private static void createUi() {
        frame = new JFrame("测试框架");
        var d = new DisplayModeTest();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Utils.centerContainer(frame);
        frame.getContentPane().add(d);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void update() {
        Utils.resizeFrame(frame,this);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DisplayModeTest::createUi);
    }
}
