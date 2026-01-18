package com.snl.data.homework.charptor03.practice01.entity.weapon.gun;

import com.snl.data.homework.charptor03.practice01.Music;
import com.snl.data.homework.charptor03.practice01.entity.Sprite;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public class SniperRifle extends Gun {

    //枪托
    private float gunStockWidth;
    private float gunStockHeight;

    //握把
    private float handleWidth;
    private float handleHeight;

    //枪管
    private float barrelWidth;
    private float barrelHeight;

    //狙击镜
    private float scopeWidth;
    private float scopeHeight;

    private Point2D leftPoint;

    public SniperRifle(double x,double y,String name,int maxBullets) {
        this(20,20,20,20,name,maxBullets,x,y);
    }

    public SniperRifle(double originShootSpeed, double originHearingRangle,
                       int originRecoil, double originKillDamage, String name, int maxBullets,double x,double y) {
        super(originShootSpeed, originHearingRangle, originRecoil, originKillDamage, name, maxBullets);
        initData(x,y);
        createShape();
    }

    private void initData(double x, double y) {
        gunStockWidth = 50;
        gunStockHeight = 25;
        handleWidth = gunStockWidth;
        handleHeight = gunStockHeight;
        barrelWidth = 4 * gunStockWidth;
        barrelHeight = gunStockHeight / 2;

        scopeWidth = 5;
        scopeHeight = 10;
        leftPoint = new Point2D.Double(x,y);
    }

    private void createShape() {
        float x = (float) leftPoint.getX();
        float y = (float) leftPoint.getY();
        Shape stock = new Rectangle2D.Double(x,y,gunStockWidth,gunStockHeight);
        Area a1 = new Area(stock);
        stock = new Rectangle2D.Double(x + gunStockWidth,y,handleWidth,handleHeight);
        Area a2;
        a2 = new Area(stock);
        a1.add(a2);
        //圆心的位置
        float rX = x + gunStockWidth;
        float rY = y + gunStockHeight / 2;
        stock = new Ellipse2D.Double(rX,rY,scopeHeight * 8,scopeHeight * 8);
        a2 = new Area(stock);
        a1.subtract(a2);
        stock = new Rectangle2D.Double(x + gunStockWidth + handleWidth,
                y-barrelHeight / 2,barrelWidth,barrelHeight);
        a2 = new Area(stock);
        a1.add(a2);
        float x0 = x + gunStockWidth + handleWidth + barrelWidth /2;
        float y0 = y-barrelHeight / 2;
        float x1 = x0  + barrelWidth / 2;

        for (float j = x0;j<x1;j+=10) {
            stock = new Rectangle2D.Double(j,y0,3,2);
            a2 = new Area(stock);
            a1.subtract(a2);
        }

        y0 += barrelHeight / 2;
        stock = new Rectangle2D.Double(x0,y0,200,2);
        a2 = new Area(stock);
        a1.subtract(a2);

        x0 = x + gunStockWidth + handleWidth + barrelWidth / 4;

        x1 = x0 + barrelWidth / 4;
        for (float j = x0;j<x1;j+=10) {
            stock = new Rectangle2D.Double(j,y0,3,barrelHeight);
            AffineTransform af = AffineTransform.getShearInstance(-0.1,0);
            stock = af.createTransformedShape(stock);
            a2 = new Area(stock);
            a1.subtract(a2);
        }

        x0 = x + gunStockWidth / 10;
        x1 = x0 + gunStockWidth / 2;
        y0 = y+gunStockHeight / 4;
        for (float j = x0;j<x1;j+=10) {
            stock = new Rectangle2D.Double(j,y0,3,gunStockHeight/2);
            AffineTransform af = AffineTransform.getShearInstance(0.1,0);
            stock = af.createTransformedShape(stock);
            a2 = new Area(stock);
            a1.subtract(a2);
        }

        x0 = x + gunStockWidth + handleWidth;
        y0 = y-barrelHeight / 2;

        x0 += barrelWidth / 8;
        y0 -= scopeHeight;
        stock = new Rectangle2D.Double(x0,y0,scopeWidth,scopeHeight);
        a2 = new Area(stock);
        a1.add(a2);

        x0 += barrelWidth / 6;
        stock = new Rectangle2D.Double(x0,y0,scopeWidth,scopeHeight);
        a2 = new Area(stock);
        a1.add(a2);

        x0 = x + gunStockWidth + handleWidth + barrelWidth - barrelWidth / 6;
        y0 = y+barrelHeight / 8;
        stock = new Ellipse2D.Double(x0,y0,barrelHeight / 2,barrelHeight / 2);
        a2 = new Area(stock);
        a1.add(a2);
        setShape(a1);
    }

    @Override
    public void attack(Sprite sprite) {
        if (isReload()) {
            //如果正在装填，无响应
            return;
        }
        super.attack(sprite); //这一步干了三件事情,调用shot方法，将子弹从子弹夹中取出
        Music.sniparShoot();
        setShootTime(System.currentTimeMillis());
        setReload(true);
    }

    @Override
    public void update(double x, double y) {
        leftPoint = new Point2D.Double(x,y);
        createShape();
    }

    @Override
    public void render(Graphics g) {
        //渲染武器
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        //设置纹理
        BufferedImage bi = getTextureImage();
        var r = new Rectangle2D.Double(0,0,bi.getWidth(),bi.getHeight());
        TexturePaint paint = new TexturePaint(bi,r);
        g2.setPaint(paint);
        AffineTransform af = AffineTransform.getScaleInstance(0.5,0.5);
        var shape = af.createTransformedShape(getShape());
        af = AffineTransform.getTranslateInstance(.5 * leftPoint.getX()-10,0.5  * leftPoint.getY()+10);
        shape = af.createTransformedShape(shape);
        setShape(shape);
        g2.fill(getShape());
        g2.dispose();
        super.render(g);
    }



    private BufferedImage getTextureImage() {
        int size = 2;
        BufferedImage bi = new BufferedImage(
                size,size,BufferedImage.TYPE_INT_RGB);
        var g2 = bi.createGraphics();
        g2.setPaint(Color.lightGray);
        g2.fillRect(0,0,size / 2 ,size /2);
        g2.setPaint(Color.green);
        g2.fillRect(size / 2,0,size,size / 2);
        g2.setPaint(Color.blue);
        g2.fillRect(0,size / 2,size /2 ,size);
        g2.setPaint(Color.white);
        g2.fillRect(size / 2,size /2 ,size,size);
        return bi;
    }
}
