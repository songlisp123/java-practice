package com.snl.data.homework.charptor03.practice01.entity.enmry;

import com.snl.data.homework.charptor03.practice01.entity.Group;
import com.snl.data.homework.charptor03.practice01.entity.booms.Boom;
import com.snl.data.homework.charptor03.practice01.entity.booms.BoomGroup;
import com.snl.data.homework.charptor03.practice01.entity.booms.BoomShape;
import com.snl.data.homework.charptor03.practice01.state.Direction;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.random.RandomGenerator;

public class AdvancedEnemy extends Enemy {
    //TODO 暂时不完成
    //第五章boss

    private final double MAX_Y_SPEED = 5.0;
    private final double MIN_Y_SPEED = 1.0;
    private final double MIN_X_SPEED = 1.0;
    private final double MAX_X_SPEED = 5.0;
    private final RandomGenerator generator =
            RandomGenerator.getDefault();

    private BoomGroup boomGroup;

    //眼睛的位置
    private final int EYE_WEIGHT = 15;
    private final int EYE_HEIGHT = 20;

    private final int MOUSE_WEIGHT = 25;
    private final int MOSE_HEIGHT = 10;

    private long start;

    //动画属性
    //眨眼间隔
    private final long GAP = 1_000L;
    //alpha分量
    private int alpha;

    private boolean changing;

    public AdvancedEnemy(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        super(xPos, yPos, WEIGHT, HEIGHT);
        setOriginLifePoints(100);
        setxSpeed(5);
        setySpeed(0);
        //创建纹理
        createPaint();
        //创建子弹
        boomGroup = new BoomGroup(200);
        //攻击的时间
        start = System.currentTimeMillis();
    }

    private void createPaint() {
        BufferedImage bi = createBufferImage();
        Rectangle2D r = new Rectangle2D.Double(getxPos(),getyPos(),getWEIGHT(),getHEIGHT());
        paint = new TexturePaint(bi,r);
    }

    @Override
    public void update(double delta, InputState state, Group group) {
        super.update(delta, state, group);
        //发射子弹
        long now = System.currentTimeMillis();
        if (now - start >= 2_000L) {
            //进攻
            fillBooms();
            start = now;
        }
        //更新子弹
        boomGroup.update(delta,null,null,group,10.0);
        //创建纹理
        createPaint();
        //更新alpha

    }

    private void fillBooms() {
        //计算下边界
        double right = getRight();
        double left = getLeft();
        double y = getyPos() + getHEIGHT();
        for (double i = left;i<right;i+=2) {
            double x = generator.nextDouble(-3.5, 3.5);
            Boom boom = new Boom(i,y,10,10, Direction.SOUTH,
                    BoomShape.CIRCLE, Color.RED,x,3);
            boomGroup.add(boom);
        }
    }

    @Override
    public void paint(Graphics g, InputState state) {
        super.paint(g, state);
        boomGroup.render(g);
    }

    @Override
    public void reset() {
        super.reset();
        boomGroup.clear();
    }

    public BoomGroup getBoomGroup() {
        return boomGroup;
    }

    private BufferedImage createBufferImage() {
        int size = getWEIGHT();
        BufferedImage bi =  new BufferedImage(size,size,BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = bi.createGraphics();
        g2.setColor(defaultColor);
        g2.fillRect(0,0,size,size);
        g2.dispose();
        //设置RGB
        //设置颜色分量的数组
        //获取颜色分量
        int packedColor;
        int packed;

        if (alpha >= 255)
            changing = true;
        if (changing) {
            alpha--;
            if (alpha <= 0)
            {
                alpha = 0;
                changing = false;
            }
        }else
            alpha++;
        packed = (alpha << 24);
        packedColor = packed |  0X00FFFF00;
        int[] color = new int[]{
                //白色
          0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,
          0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,
          0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,
          0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,0x00000000,
          packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,
          packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,
          packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,
          packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,
          packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,
          packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,
          packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,
          packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,
          packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,
          packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,
          packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,
          packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,
          packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,
          packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,
          packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,
          packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,packedColor,
          //黑色
        };
        bi.setRGB(5,5,EYE_WEIGHT,EYE_HEIGHT,color,0,EYE_WEIGHT);
        bi.setRGB(getWEIGHT() - EYE_WEIGHT - 5,5,EYE_WEIGHT,EYE_HEIGHT,color,0,EYE_WEIGHT);

        //绘制嘴巴
        int[] mouse = new int[] {
            0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,
                0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,
                0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,
                0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,
                0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,
                0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,
                0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,
                0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,
                0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,
                0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,
                0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,
                0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,
                0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,
                0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,
                0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,
                0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,
                0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,
                0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,
                0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,
                0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,0xff00ffff,
        };
        bi.setRGB(15,30,MOUSE_WEIGHT,MOSE_HEIGHT,mouse,0,MOUSE_WEIGHT);
        return bi;
    }
}
