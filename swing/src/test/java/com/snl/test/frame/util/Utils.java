package com.snl.test.frame.util;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Utils {

    //这个函数在本地设备上居中容器
    public static void centerContainer(Container container) {
        //判断是否可现实
        if (!container.isVisible())
            container.setVisible(true);
        //判断是否可显示
        if (!container.isDisplayable())
            container.addNotify();
        //获取本地设备
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = container.getPreferredSize();
        int x = (screenSize.width - size.width) / 2;
        int y = (screenSize.height - size.height) / 2;
        container.setLocation(x,y);
    }

    /**
     * 居中顶级框架
     * @param container 顶级容器
     * @param component 容器内子组件
     */
    public static void resizeFrame(Container container,Component component) {
        if (!container.isDisplayable()) container.addNotify();
        Insets insets = container.getInsets();
        Dimension size = component.getPreferredSize();
        int w = insets.left + size.width + insets.right;
        int h = insets.top + size.height + insets.bottom;
        container.setSize(w, h);
    }

    /**
     * 窗口被关闭时的请求
     * @return 窗口关闭操作
     */
    public static int showClosingDialog() {
        return JOptionPane.showConfirmDialog(null,
                "是否要退出",
                "退出",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null);
    }

    public static void sleep(long l)
    {
        try {
            Thread.sleep(l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前底层设备的显示模式
     * @return 底层设备的显示模式
     */
    public static DisplayMode getCurrentDisplayMode() {
        GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice screenDevice = localGraphicsEnvironment.getDefaultScreenDevice();
        return screenDevice.getDisplayMode();
    }

    /**
     * 获取当前图形设备的所有显示模式
     * @return 所有显示模式
     */
    public static DisplayMode[] listAllDisplayModes() {
        GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice screenDevice = localGraphicsEnvironment.getDefaultScreenDevice();
        return screenDevice.getDisplayModes();
    }

}
