package com.snl.test.image;

import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.util.Arrays;

public class BufferImageTest {

    static BufferedImage image;

    private static void fillImage() {
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

    private static void createImage() {
        GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice screenDevice = localGraphicsEnvironment.getDefaultScreenDevice();
        GraphicsConfiguration configuration = screenDevice.getDefaultConfiguration();
        image = configuration.createCompatibleImage(20, 20, Transparency.OPAQUE);
    }

    private static void getImagePixels() {
        if (image == null || image.getWidth() == 0||
                image.getHeight() == 0 || image.getMinX() < 0 ||
                    image.getMinY() < 0) return;
        int rgb = image.getRGB(10, 10);
        System.out.println("rgb = " + rgb);
    }

    public static void main(String[] args) {
//        //创建图像
//        createImage();
//        //填充图像
//        fillImage();
//        //获取图像数据
//        getImagePixels();
        //创建一个颜色分量模型
        createCompModel();
    }

    private static void createCompModel() {
        //一个正常的分量颜色模型，无意外
        ColorSpace space = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorModel colorModel = new ComponentColorModel(space,new int[]{5,6,5},false,
                false,Transparency.OPAQUE, DataBuffer.TYPE_BYTE);

        //一个异常的颜色模型，其中传入的位数小于底层颜色空间的数量
        //获取底层颜色空间的分量
        int numComponents = space.getNumComponents();
        //创建异常模型，该模型会抛出非法参数异常，表示数量不匹配
        colorModel = new ComponentColorModel(space,new int[]{5,4,5},false,false,
                Transparency.OPAQUE,DataBuffer.TYPE_BYTE);
        Object pixel = null;
        //将颜色分量转换成像素值，将会转换成具有转移类型的数组，其中元素的数量等同于分量的个数
        pixel = colorModel.getDataElements(0x00ff00ff, null);
        System.out.println(Arrays.toString((byte[]) pixel));

        //获取另一个颜色空间
        ColorModel colorModel1 = new DirectColorModel(24,0xff0000,0xff00,0xff,0);
        //将颜色分量转换成像素样本(将会打包成一个整数值，数组中只有一个分量)
        pixel = colorModel1.getDataElements(0x00ff00ff, null);
        System.out.println(Arrays.toString((int[]) pixel));

        //将directModel打包后的像素传入分解成颜色分量
        int[] components = colorModel1.getComponents(pixel, null, 0);
        System.out.println(Arrays.toString(components));

        //创建采样模型
        SampleModel sampleModel = new SinglePixelPackedSampleModel(DataBuffer.TYPE_INT,20,20,
                new int[]{0xff0000,0xff00,0xff});
        //获取采样模型波段
        int numBands = sampleModel.getNumBands();
        System.out.println("numBands = " + numBands);
        //创建一个光栅化器
        Raster raster = Raster.createWritableRaster(sampleModel,new Point(0,0));
        DataBuffer dataBuffer = raster.getDataBuffer();
        sampleModel.setPixel(2,2,new int[]{255,255,45},dataBuffer);

        //获取样本值
        Object dataElements = raster.getDataElements(5, 5, null);
        Object dataElements1 = sampleModel.getDataElements(2, 2, null, dataBuffer);
        //将样本值转换为颜色分量
        colorModel1.getComponents(dataElements,components,0);

        int[] components1 = colorModel1.getComponents(dataElements1, null, 0);
        System.out.println(Arrays.toString(components));
        System.out.println(Arrays.toString(components1));
    }




}
