package com.snl.data.homework.charptor03.practice01.entity.weapon.sword;

import com.snl.data.homework.charptor03.practice01.Music;
import com.snl.data.homework.charptor03.practice01.article.Article;
import com.snl.data.homework.charptor03.practice01.article.SwordArticle;
import com.snl.data.homework.charptor03.practice01.entity.Group;
import com.snl.data.homework.charptor03.practice01.entity.booms.BoomShape;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.util.random.RandomGenerator;

public class LightSaber extends AbstractSword {

    /**
     * 剑的左上角
     */
    private Point2D start;
    private final RandomGenerator generator = RandomGenerator.getDefault();

    private boolean hasShing;
    private final long LIFE = 200L;
    private long startTime;

    public LightSaber(double x,double y ,String name) {
        this(x,y,40,10,12.5,13.3,100);
        super.setName(name);
        this.start = new Point2D.Double(x,y);
        startTime = System.currentTimeMillis();
        hasShing = true;
        initData();
    }

    private void initData() {
        double x = start.getX();
        double y = start.getY();
        RoundRectangle2D.Double shape = new RoundRectangle2D.Double(
                x,y,
                getWEIGHT(),getHEIGHT(), getHEIGHT(),getHEIGHT());
        fillParticles(shape);
        setShape(shape);
    }

    public LightSaber(double xPos, double yPos, int WEIGHT, int HEIGHT,
                      double originKillDamage, double originalSpeed, double currentDurability) {
        super(xPos, yPos, WEIGHT, HEIGHT, originKillDamage, originalSpeed, currentDurability);
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
            Music.lightSaber();
        }
        if (!hasShing && now - startTime >= LIFE) {
            hasShing = true;
            startTime = now;
            setColor(Color.RED);
        }
    }

    private void fillParticles(Shape shape) {
        Group<Article> smokes = getSmokes();
        double x = shape.getBounds().getX();
        double y = shape.getBounds().getY();
        double width = shape.getBounds().getWidth();
        double height = shape.getBounds().getHeight();
        for (double t = x; t < x + width ; t+=5) {
            Article article = createArticles(t,y);
            addArticle(smokes,article);
            article = createArticles(t,y+height);
            addArticle(smokes,article);
        }
        setSmokes(smokes);
    }

    private void addArticle(Group<Article> smokes,Article article) {
        if (article ==null)
            return;
        article.setxSpeed(generator.nextDouble(-0.5,0.5));
        article.setySpeed(generator.nextDouble(-0.5,0.5));
        smokes.add(article);
    }

    private Article createArticles(double x,double y) {
        return new SwordArticle(x,y,1,1,getColor(),BoomShape.CIRCLE);
    }
}
