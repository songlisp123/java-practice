package com.snl.test.image;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.util.Arrays;

public class ComponentColorModelTest  {

    static BufferedImage image;

    public static void main(String[] args) {
        createImage();
        fillImage();
        //获取颜色模型
        ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorSpace gray = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ColorModel model = new ComponentColorModel(gray,new int[]{5},false,
                false,Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        Color w = new Color(gray,new float[]{1.f},1);
        //获取归一化的颜色分量
        float[] components = w.getComponents(null);
        System.out.println("未归一化像素:");
        System.out.println("Arrays.toString(components) = " + Arrays.toString(components));
        //获取未归一化对策颜色分量
        System.out.println("未归一化像素:");

        int[] unnormalizedComponents = model.getUnnormalizedComponents(components, 0, null, 0);
        System.out.println("Arrays.toString(unnormalizedComponents) = " + Arrays.toString(unnormalizedComponents));

        System.out.println("颜色分量: ");
        int[] components1 = model.getComponents(unnormalizedComponents, null, 0);
        System.out.println("Arrays.toString(components1) = " + Arrays.toString(components1));

        Object dataElements = model.getDataElements(components1, 0, null);
        //获取rgb

        int rgb = model.getRGB(dataElements);
        System.out.println("rgb = " + rgb);

        int red = model.getRed(5);
        float[] normalizedComponents = model.getNormalizedComponents(new byte[]{5}, null, 0);
        System.out.println("颜色分量: ");

        System.out.println("Arrays.toString(normalizedComponents) = " + Arrays.toString(normalizedComponents));
        float[] rgb1 = gray.toRGB(normalizedComponents);
        System.out.println("转换为标准sRGB色彩空间: ");
        System.out.println("Arrays.toString(rgb1) = " + Arrays.toString(rgb1));

        int r = (int) (rgb1[0] * 255 + 0.5);
        System.out.println("red = " + red);
        System.out.println("r == red"+(r == red ? "是" : "否"));

        //转换色彩分量到样本值
        Object elements = model.getDataElements(components1, 0, null);
        System.out.println("转换为该色彩模型像素分量: ");
        System.out.println(Arrays.toString((byte[]) elements));


    }

    static void fillImage() {
//        int[] filled = new int[] {
//                0x00ff0000,0x00ff0000,0x00ff0000,0x00ff0000,0x00ff0000,0x00ff0000,0x00ff0000,0x00ff0000,0x00ff0000,0x00ff0000,
//                0x00ff0000,0x00ff0000,0x00ff0000,0x00ff0000,0x00ff0000,0x00ff0000,0x00ff0000,0x00ff0000,0x00ff0000,0x00ff0000,
//        };
//        //填充颜色
//        image.setRGB(0,0,20,20,filled,0,0);

        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.red);
        g2.fillRect(0,0,image.getWidth(),image.getHeight());
        g2.dispose();
    }

    static void createImage() {
        GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice screenDevice = localGraphicsEnvironment.getDefaultScreenDevice();
        GraphicsConfiguration configuration = screenDevice.getDefaultConfiguration();
        image = configuration.createCompatibleImage(20, 20, Transparency.OPAQUE);
    }
}
