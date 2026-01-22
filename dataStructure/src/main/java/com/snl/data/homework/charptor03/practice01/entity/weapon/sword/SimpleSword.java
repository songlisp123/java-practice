package com.snl.data.homework.charptor03.practice01.entity.weapon.sword;

import com.snl.data.homework.charptor03.practice01.article.Article;
import com.snl.data.homework.charptor03.practice01.article.SwordArticle;
import com.snl.data.homework.charptor03.practice01.entity.Group;
import com.snl.data.homework.charptor03.practice01.entity.booms.BoomShape;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.random.RandomGenerator;

/**
 * 这个类抽象了剑的几何外形
 */
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
    //随机生成器
    private final RandomGenerator generator = RandomGenerator.getDefault();
    private int alpha;
    private boolean changeing;

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

    //绘制形状
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
        for (double t = xTemp; t < x ; t+=8) {
            Article article = new SwordArticle(t,y,1,1,getColor(),BoomShape.CIRCLE);
            article.setxSpeed(generator.nextDouble(-.85,.85));
            article.setySpeed(generator.nextDouble(-.35,.35));
            smokes.add(article);
        }
        setSmokes(smokes);
    }

    @Override
    public void update(double xPos, double yPos, double delta,Group agroup, Group destory, Group wall) {
        super.update(xPos, yPos,delta, agroup, destory, wall);
        start = new Point2D.Double(xPos,yPos); //更新剑的位置
        initData(); //重新更新剑的形状
        Color color = new Color(255,255,255,alpha);
        setColor(color);

        if (alpha >= 255) {
            changeing = true;
        }

        if (changeing) {
            alpha--;
            if (alpha <= 150) {
                alpha = 150;
                changeing = false;
            }
        }else {
            alpha++;
        }
    }
}
