package com.snl.data.homework.charptor03.practice01.entity.weapon.knife;

import com.snl.data.homework.charptor03.practice01.article.Article;
import com.snl.data.homework.charptor03.practice01.article.SwordArticle;
import com.snl.data.homework.charptor03.practice01.entity.Group;
import com.snl.data.homework.charptor03.practice01.entity.booms.BoomShape;
import com.snl.data.homework.charptor03.practice01.entity.weapon.sword.AbstractSword;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.random.RandomGenerator;

public abstract class AbstractKnife extends AbstractSword implements Knife {

    private Point2D start ;
    //刀柄宽
    private double hitWidth ;
    //刀柄高
    private double hitHeight;
    //握把高
    private double height;
    //刀刃宽
    private double slideWidth;
    //刀刃高
    private double slideHeight;
    //随机化生成器
    private final RandomGenerator generator = RandomGenerator.getDefault();
    /**
     * 以下是闪烁效果
     */
    //是否闪烁
    private boolean hasShing;
    //闪烁生命周期
    private final long LIFE = 1000L;
    //闪烁开始时间
    private long startTime;


    public AbstractKnife(double xPos, double yPos, double originKillDamage,
                         double originalSpeed, double currentDurability,
                         double hitHeight,double hitWidth,double height,double slideWidth,double slideHeight) {
        super(xPos, yPos,
                (int) (hitWidth + height + slideWidth),
                (int) (3 * hitHeight),
                originKillDamage, originalSpeed, currentDurability);
        this.hitHeight = hitHeight;
        this.hitWidth = hitWidth;
        this.height = height;
        this.slideWidth = slideWidth;
        this.slideHeight = slideHeight;
        this.start = new Point2D.Double(xPos,yPos);
        initData();
    }

    private void initData() {
        double x = start.getX();
        double y = start.getY();
        var path = new GeneralPath();
        double dx = x;
        double dy = y;
        path.moveTo(dx,dy);
        dx += hitWidth;
        path.lineTo(dx,dy);

        dy -= (3 * hitHeight) / 2 - hitHeight / 2;
        path.lineTo(dx,dy);
        //绘制圆弧
        double ctrl1_x,ctrl1_y;
        ctrl1_x = dx;
        ctrl1_y = dy - height;
        double end_x ,end_y;
        end_x = dx + height;
        end_y = dy;
        double ctrl2_x,ctrl2_y;
        ctrl2_x = end_x;
        ctrl2_y = end_y - height;
        path.curveTo(ctrl1_x,ctrl1_y,ctrl2_x,ctrl2_y,end_x,end_y);

        dx = end_x;
        dy = y + (slideHeight - hitWidth) / 2;

        path.lineTo(dx,dy);

        double f = height / 4;
        ctrl1_x = dx + f;
        ctrl1_y = dy + f * 2;

        end_x = dx + slideWidth;
        end_y = dy - f * 2;

        double x_end ,y_end;
        ctrl2_x = end_x + f * 2;
        ctrl2_y = end_y - f;
        x_end = end_x;
        y_end = end_y;
        path.curveTo(ctrl1_x,ctrl1_y,ctrl2_x,ctrl2_y,end_x,end_y);
        fillParticles(ctrl1_x,end_x,dy);

        path.moveTo(x,y);
        end_x = x;
        end_y = y + hitHeight;
        ctrl1_x = x - height;
        ctrl1_y = y;
        ctrl2_x = ctrl1_x;
        ctrl2_y = end_y;
        path.curveTo(ctrl1_x,ctrl1_y,ctrl2_x,ctrl2_y,end_x,end_y);

        dy = end_y;
        dx = end_x + hitWidth;
        path.lineTo(dx,dy);

        dy += (3 * hitHeight) / 2 - hitHeight / 2;
        path.lineTo(dx,dy);

        ctrl1_x = dx;
        ctrl1_y = dy + height;

        end_x = dx + height;
        end_y = dy;

        ctrl2_x = end_x;
        ctrl2_y = end_y + height;
        path.curveTo(ctrl1_x,ctrl1_y,ctrl2_x,ctrl2_y,end_x,end_y);

        dx = end_x;
        dy = y + hitHeight + (slideHeight - hitHeight) / 2;
        path.lineTo(dx,dy);

        ctrl1_x = dx + f * 2;
        ctrl1_y = dy  + f / 2;


        ctrl2_x = x_end + f;
        ctrl2_y = y_end + f * 2;
        path.curveTo(ctrl1_x,ctrl1_y,ctrl2_x,ctrl2_y,x_end,y_end);
        fillParticles(ctrl1_x,x_end,dy);

        setShape(path);
    }

    private void fillParticles(double xTemp, double x, double y) {
        var smokes = getSmokes();
        for (double t = xTemp; t < x ; t+=5) {
            Article article = new SwordArticle(t,y,1,1,getColor(), BoomShape.CIRCLE);
            article.setxSpeed(generator.nextDouble(-0.5,0.5));
            article.setySpeed(generator.nextDouble(-0.5,0.5));
            smokes.add(article);
        }
        setSmokes(smokes);
    }

    @Override
    public void update(double xPos, double yPos,double delta, Group agroup, Group destory, Group wall) {
        super.update(xPos, yPos,delta, agroup, destory, wall);
        start = new Point2D.Double(xPos,yPos);
        initData();
        /*
        闪烁效果
         */
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
