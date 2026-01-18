package com.snl.data.homework.charptor03.practice01.entity.weapon.sword;

import com.snl.data.homework.charptor03.practice01.Music;
import com.snl.data.homework.charptor03.practice01.article.Article;
import com.snl.data.homework.charptor03.practice01.article.SwordArticle;
import com.snl.data.homework.charptor03.practice01.entity.Group;
import com.snl.data.homework.charptor03.practice01.entity.booms.BoomShape;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.random.RandomGenerator;

public abstract class SimpleSword extends AbstractSword {

    //以下是剑的抽象数据
    /**
     * 剑的左上角
     */
    private Point2D start;
    //剑柄宽
    private double swordHitWeight;
    //剑柄高
    private double swordHitHeight;
    //剑翘宽
    private double height;
    //剑刃款
    private double swordBladeWeight;
    //剑刃高
    private double swordBladeHeight;
    //剑尖宽
    private double swordTipWeight;

    private final RandomGenerator generator = RandomGenerator.getDefault();

    /**
     * 闪烁效果
     */
    private boolean hasShing;
    //闪烁生命周期
    private final long LIFE = 1000L;
    //闪烁开始时间
    private long startTime;

    public SimpleSword(double xPos, double yPos,
                         double originKillDamage, double currentDurability,
                         double originalSpeed, double swordHitWeight, double swordHitHeight,
                         double height, double swordBladeWeight,
                         double swordBladeHeight, double swordTipWeight)
    {
        super(xPos, yPos,
                (int) (swordHitWeight+height+swordBladeWeight+swordTipWeight),
                (int) (3 * swordHitHeight),originKillDamage,originalSpeed,currentDurability);
        this.swordHitWeight = swordHitWeight;
        this.swordHitHeight = swordHitHeight;
        this.height = height;
        this.swordBladeWeight = swordBladeWeight;
        this.swordBladeHeight = swordBladeHeight;
        this.swordTipWeight = swordTipWeight;
        this.start = new Point2D.Double(xPos,yPos);
        initData();
    }

    private void initData() {
        double x = start.getX();
        double y = start.getY();
         var shape = new GeneralPath();
        shape.moveTo(x,y);
        //剑柄绘制
        x = x +swordHitWeight;
        shape.lineTo(x,y);
        y = y - swordHitHeight;
        shape.lineTo( x,y);
        x = x  + height;
        shape.lineTo(x,y);
        y = start.getY() - (swordBladeHeight - swordHitHeight) / 2;
        shape.lineTo(x,y);
        double xTemp = x;
        x += swordBladeWeight;
        fillParticles(xTemp,x,y);
        shape.lineTo(x,y);
        x += swordTipWeight;
        y = start.getY() + swordHitHeight/2;
        shape.lineTo(x,y);

        x -= swordTipWeight;
        y = start.getY() + (swordBladeHeight - swordHitHeight) / 2 + swordHitHeight;
        shape.lineTo(x,y);

        xTemp = x;
        x -= swordBladeWeight;
        fillParticles(x,xTemp,y);
        shape.lineTo(x,y);

        y = start.getY() + 2 * swordHitHeight;
        shape.lineTo(x,y);

        x -= height;
        shape.lineTo(x,y);

        y = start.getY() + swordHitHeight;
        shape.lineTo(x,y);

        x = start.getX();
        shape.lineTo(x,y);
        shape.closePath();
        setShape(shape);
    }

    private void fillParticles(double xTemp, double x, double y) {
        var smokes = getSmokes();
        for (double t = xTemp; t < x ; t+=5) {
            Article article = new SwordArticle(t,y,1,1,getColor(),BoomShape.CIRCLE);
            article.setxSpeed(generator.nextDouble(-0.5,0.5));
            article.setySpeed(generator.nextDouble(-0.5,0.5));
            smokes.add(article);
        }
        setSmokes(smokes);
    }

    @Override
    public void update(double xPos, double yPos, Group agroup, Group destory, Group wall) {
        super.update(xPos, yPos, agroup, destory, wall);
        start = new Point2D.Double(xPos,yPos);
        initData();

        long now = System.currentTimeMillis();
        if (hasShing && now - startTime>=LIFE) {
            hasShing = false;
            startTime = now;
            setColor(Color.cyan);
        }
        if (!hasShing && now - startTime >= LIFE) {
            hasShing = true;
            startTime = now;
            setColor(Color.ORANGE);
        }
    }
}
