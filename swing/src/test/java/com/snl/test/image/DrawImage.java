package com.snl.test.image;

import com.snl.test.TEXT.TextLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.util.Arrays;

public class DrawImage extends JPanel {


    private BufferedImage image;

    public DrawImage() {
        setBackground(Color.black);
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice screenDevice = environment.getDefaultScreenDevice();
        GraphicsConfiguration configuration = screenDevice.getDefaultConfiguration();
        image = configuration.createCompatibleImage(200,200,Transparency.OPAQUE);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.green);
        g2.fillRect(0,0,image.getWidth(),image.getHeight());
        g2.dispose();
        int rgb = image.getRGB(20, 20);
        System.out.println("rgb = " + rgb);
        //你需要提供的数组长度是：w * h - (h-1)*scansize + offset
        int[] ints = new int[] {
                0x00ff0000,0x00ff0000,0x00ff0000,0x00ffff00,0x00ffff00,0x00ffff00,123,15000, 150000,15478,45622,120,78965,123,15000,150000,25,256,256,256,
                15000,150000,15478,45622,120,78965,123,15000, 150000,15478,45622,120,78965,123,15000,150000,25,256,256,256,
                15000,150000,15478,45622,120,78965,123,15000, 150000,15478,45622,120,78965,123,15000,150000,25,256,256,256,
                15000,150000,15478,45622,120,78965,123,15000, 150000,15478,45622,120,78965,123,15000,150000,25,256,256,256,
                15000,150000,15478,45622,120,78965,123,15000, 150000,15478,45622,120,78965,123,15000,150000,25,256,256,256,
                15000,150000,15478,45622,120,78965,123,15000, 150000,15478,45622,120,78965,123,15000,150000,25,256,256,256,
                15000,150000,15478,45622,120,78965,123,15000, 150000,15478,45622,120,78965,123,15000,150000,25,256,256,256,
                15000,150000,15478,45622,120,78965,123,15000, 150000,15478,45622,120,78965,123,15000,150000,25,256,256,256,
                15000,150000,15478,45622,120,78965,123,15000, 150000,15478,45622,120,78965,123,15000,150000,25,256,256,256,
                15000,150000,15478,45622,120,78965,123,15000, 150000,15478,45622,120,78965,123,15000,150000,25,256,256,256,
                15000,150000,15478,45622,120,78965,123,15000, 150000,15478,45622,120,78965,123,15000,150000,25,256,256,256,
                15000,150000,15478,45622,120,78965,123,15000, 150000,15478,45622,120,78965,123,15000,150000,25,256,256,256,
                15000,150000,15478,45622,120,78965,123,15000, 150000,15478,45622,120,78965,123,15000,150000,25,256,256,256,
                15000,150000,15478,45622,120,78965,123,15000, 150000,15478,45622,120,78965,123,15000,150000,25,256,256,256,
                15000,150000,15478,45622,120,78965,123,15000, 150000,15478,45622,120,78965,123,15000,150000,25,256,256,256,
                15000,150000,15478,45622,120,78965,123,15000, 150000,15478,45622,120,78965,123,15000,150000,25,256,256,256,
                15000,150000,15478,45622,120,78965,123,15000, 150000,15478,45622,120,78965,123,15000,150000,25,256,256,256,
                15000,150000,15478,45622,120,78965,123,15000, 150000,15478,45622,120,78965,123,15000,150000,25,256,256,256,
                15000,150000,15478,45622,120,78965,123,15000, 150000,15478,45622,120,78965,123,15000,150000,25,256,256,256,
                15000,150000,15478,45622,120,78965,123,15000, 150000,15478,45622,120,78965,123,15000,150000,25,256,256,256
        };
        image.setRGB(20,20,20,20,ints,0,20);

        ColorModel colorModel = image.getColorModel();

        WritableRaster raster = image.getRaster();
        double[] pixel = raster.getPixel(10, 10, (double[]) null); //获取绿色样本

        int[] samples = raster.getSamples(10, 10, 10, 10, 1, (int[]) null);
        System.out.println("Arrays.toString(samples) = " + Arrays.toString(samples));

        int[] g_r = new int[]{
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
                0x00,0x10,0x20,0x30,0x40,0x50,0xc2,0xd5,0x75,0x52,0xff,
        };
        raster.setSamples(10,10,20,20,1,g_r);
        System.out.println(Arrays.toString(pixel));

        raster.getSamples(10,10,10,10,1,samples);
        System.out.println("Arrays.toString(samples) = " + Arrays.toString(samples));
        
        double sampleDouble = raster.getSampleDouble(10, 10, 1);//获取绿色波段
        System.out.println("sampleDouble = " + sampleDouble);

        int[] pixels = raster.getPixels(10, 10, 10, 10, (int[]) null);//获取矩形像素值
        System.out.println("Arrays.toString(pixels) = " + Arrays.toString(pixels));
        
        Object dataElements = colorModel.getDataElements(0x00ffff00, null);
        
        
        
        int[] components = colorModel.getComponents(dataElements, null, 0);
        System.out.println(Arrays.toString(components));

        Object dataElements2 = raster.getDataElements(10, 10, null);

        int[] components1 = colorModel.getComponents(dataElements2, null, 0);
        System.out.println("Arrays.toString(components1) = " + Arrays.toString(components1));

        Object dataElements1 = colorModel.getDataElements(components, 0, null);
        System.out.println("Arrays.toString((int[]) dataElements1) = " + Arrays.toString((int[]) dataElements1));

        SampleModel sampleModel = raster.getSampleModel();
        System.out.println("sampleModel = " + sampleModel);

        //创建一个分量颜色模型
        ColorSpace space = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        int numComponents = space.getNumComponents();
        ColorModel component = new ComponentColorModel(space,new int[]{5,5,5},false,false,
                Transparency.OPAQUE,DataBuffer.TYPE_BYTE);

        Object dataElements3 = component.getDataElements(new int[]{0xc2, 0x54, 0x56}, 0, null);
        System.out.println(Arrays.toString((byte[]) dataElements3));

//        ColorModel colorModel = image.getColorModel();
//        ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_sRGB);
//        ColorModel model = new ComponentColorModel(colorSpace, new int[]{2,1,5},false,
//                false,Transparency.OPAQUE,DataBuffer.TYPE_BYTE);
//        Color color = new Color(colorSpace,new float[]{1.0f,1.0f,1.0f},0);
//        float[] components = color.getComponents(null);//获取归一化分量
//        int[] unnormalizedComponents = model.getUnnormalizedComponents(components, 0, null, 0);
//        var dataElements = model.getDataElements(unnormalizedComponents, 0, null);
//        byte[] pixels = (byte[]) dataElements;
//        System.out.println(Arrays.toString(pixels));
//
//        //接下来测试如何将样本数据转化为颜色分量
//        int[] modelComponents = model.getComponents(dataElements, null, 0);
//        System.out.println(Arrays.toString(modelComponents));
//        //获取归一化色彩分量
//        float[] normalizedComponents = model.getNormalizedComponents(dataElements, null, 0);
//        System.out.println(Arrays.toString(normalizedComponents));
        //        System.out.println("colorModel = " + colorModel);
//
//        int[] size = colorModel.getComponentSize();
//        System.out.println(Arrays.toString(size));
//
//        int pixelSize1 = colorModel.getPixelSize();
//        System.out.println("pixelSize1 = " + pixelSize1);
//        //将像素样本转换为颜色分量
//        int[] components = colorModel.getComponents(0X00FFFFFF, null, 0);
//
//
//        int red = colorModel.getRed(0X00FF0000);
//        System.out.println("red = " + red);
//        System.out.println(Arrays.toString(components));
//        //归一化颜色分量
//        float[] normalizedComponents = colorModel.getNormalizedComponents(components, 0, null, 0);
//        System.out.println(Arrays.toString(normalizedComponents));
//        //降像素样本转换为颜色分量（另一种方法，将像素作为object引用的数组传递）
//        int[] s = new int[9];
//        colorModel.getComponents(new int[]{1258,255,255},s,0);
//
//
//
//        Arrays.stream(components).forEach(System.out::println);
//        //将颜色分量转换为像素样本
//        var dataElements1 =(int[]) colorModel.getDataElements(new int[]{60,170,255}, 0,null);
//        Arrays.stream(dataElements1).forEach(System.out::println);
//        //返回改颜色模型的分量(包含alpha)
//        int numComponents = colorModel.getNumComponents();
//        System.out.println("numComponents = " + numComponents);
//        //返回颜色分量数量
//        int numColorComponents = colorModel.getNumColorComponents();
//        System.out.println("numColorComponents = " + numColorComponents);
//        //获取每个像素的位数
//        int pixelSize = colorModel.getPixelSize();
//        System.out.println("pixelSize = " + pixelSize);
//
//
//        //获取传输类型(该传输类型匹配采样模型)
//        //传输类型是像素位
//        int transferType = colorModel.getTransferType();
//        switch (transferType) {
//            case 0 -> System.out.println("byte");
//            case 1 -> System.out.println("ushort");
//            case 2 -> System.out.println("short");
//            case 3 -> System.out.println("int类型");
//            case 4 -> System.out.println("float");
//            case 5 -> System.out.println("double");
//            case 32 -> System.out.println("未定义");
//        }
//        //将像素值转换为颜色风量值
//        int imageRGB = image.getRGB(10, 10);
//        //获取光栅化器
//        WritableRaster raster = image.getRaster();
//        int[] a = new int[3];
//        Object dataElements = raster.getDataElements(10, 10, null);
//        System.out.println("dataElements = " + dataElements);
//        int[] componentSize = colorModel.getComponentSize();
//        Arrays.stream(componentSize).forEach(System.out::println);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.drawImage(image,null,20,20);
        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400,300);
    }

    private static void createUi() {
        JFrame frame = new JFrame("测试");

        var p = new DrawImage();
        frame.getContentPane().add(p);
        frame.pack();
        frame.setFocusable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DrawImage::createUi);
    }
}
