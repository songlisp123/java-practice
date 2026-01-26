package com.snl.swing.homework01.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Utilies {

    final static Component component = new Component() {};
    static MediaTracker tracker = new MediaTracker(component);
    static int sId = 0;

    /**
     * 加载图像
     * @param image 要加载的图像
     * @return 如果图片完全加载,返回 {@code true} ,否则返回{@code false}
     */
    public static boolean waitForImage(Image image) {
        int id;
        synchronized (component) {
            id = sId++;
        };
        //添加图像
        tracker.addImage(image,id);
        //等待图片加载
        try {
            tracker.waitForID(id);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        //判断是否加载过程中出现任何异常
        return !tracker.isErrorID(id);
    }

    /**
     * 图像加载是否正在进行或者阻塞
     * @param path 本地图像路径
     * @return 如果图像加载完成
     */
    public static Image blockingLoad(String path) {
        Image image = Toolkit.getDefaultToolkit().createImage(path);
        if (image == null)
        {
            System.err.println("这是一个错误文件类型");
            return null;
        }
        if (!waitForImage(image))
        {
            //如果尚未加载成功
            return null;
        }
        return image;
    }

    public static Image blockingLoad(File f) {
        Image image = Toolkit.getDefaultToolkit().createImage(f.toString());
        if (image == null)
        {
            System.err.println("这是一个错误文件类型");
            return null;
        }
        if (!waitForImage(image))
        {
            //如果尚未加载成功
            return null;
        }
        return image;
    }

    /**
     * 创建缓冲图像的公共方法,缓冲图像的类型默认为 {@code BufferedImage.TYPE_INT_RGB}
     * @param image 原图像
     * @return 目的缓冲图像
     */
    public static BufferedImage makeBufferImage(Image image) {
        return makeBufferImage(image,BufferedImage.TYPE_INT_RGB);
    }

    /**
     * 私有方法创建缓冲图像
     * @param image 图像
     * @param typeIntRgb 缓冲图像类型
     * @return 缓冲图像
     */
    private static BufferedImage makeBufferImage(Image image, int typeIntRgb) {
        if (typeIntRgb < BufferedImage.TYPE_CUSTOM ||
                typeIntRgb > BufferedImage.TYPE_BYTE_INDEXED)
            throw new IllegalArgumentException("参数错误,参数必须在"+
                    BufferedImage.TYPE_CUSTOM+"到" + BufferedImage.TYPE_BYTE_INDEXED+"之间");
        if (!waitForImage(image)) return null;
        BufferedImage bi = new BufferedImage(
                image.getWidth(null),image.getHeight(null),
                typeIntRgb
        );
        Graphics2D g2 = bi.createGraphics();
        g2.drawImage(image,null,null);
        g2.dispose();
        return bi;
    }

    /**
     * 重新设置窗口大小
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
     * 居中框架
     * @param f 顶级框架
     */
    public static void center(Frame f) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension d = f.getSize();
        int x = (screenSize.width- d.width) / 2;
        int y = (screenSize.height - d.height) / 2;
        f.setLocation(x,y);
    }
}
