package com.snl.test.image;


import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.util.Arrays;

public class SampleModelImplement extends ComponentColorModelTest {

    public SampleModelImplement() {
    }

    public static void main(String[] args) {
        createImage();
        fillImage();

        //获取采样模型
        SampleModel sampleModel = image.getSampleModel();
        System.out.println("sampleModel = " + sampleModel);
        //获取像素
        boolean b = image.getColorModel().hasAlpha();
        int[] samples = new int[b ? 3 + 1 : 3];
        System.out.println(samples.length);
        sampleModel.getPixel(5,5,samples,image.getRaster().getDataBuffer());
        System.out.println(Arrays.toString(samples));
        float[] f=  new float[3];
        sampleModel.getPixel(5,5,f,image.getRaster().getDataBuffer());
        System.out.println(Arrays.toString(f));

        int[] c = new int[]{255,255,0};
        sampleModel.setPixel(5,5,c,image.getRaster().getDataBuffer());

        //换之后
        sampleModel.getPixel(5,5,samples,image.getRaster().getDataBuffer());
        System.out.println(Arrays.toString(samples));
    }
}
