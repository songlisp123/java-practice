package com.snl.test.java2D.game;

import com.snl.test.java2D.UTIL.RandomGeneratorClass;
import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class RandomAsteroidExample extends DiKaErPlus {

    PrototypeAsteroidFactory factory;
    List<PrototypeAsteroid> asteroids;

    public RandomAsteroidExample() throws HeadlessException {
        wordWidth =4;
        wordHeight = 4;
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        asteroids = new ArrayList<>();
        PolygonWrapper wrapper = new PolygonWrapper(
                wordWidth,wordHeight
        );
        factory = new PrototypeAsteroidFactory(wrapper);
        create();
    }

    private void create() {
        asteroids.clear();
        for (int i=0;i<42;i++)
        {
            asteroids.add(getRandomAsteroids());
        }
    }

    public static void main(String[] args) {
        launchGame(new RandomAsteroidExample());
    }

    public PrototypeAsteroid getRandomAsteroids() {
        double x = RandomGeneratorClass.random(wordWidth);
        double y = RandomGeneratorClass.random(wordHeight);
        Vector2D p = new Vector2D(x,y);

        PrototypeAsteroid.Size[] sizes = PrototypeAsteroid.Size.values();
        PrototypeAsteroid.Size size = sizes[RandomGeneratorClass.random(sizes.length)];
        switch (size) {
            case small -> {
                return factory.createSmallAsteroid(p);
            }
            case middle -> {
                return factory.createMiddleAsteroid(p);
            }
            default -> {
                return factory.createLargeAsteroid(p);
            }
        }
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
            create();
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        for (PrototypeAsteroid a : asteroids)
            a.update(delta);
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //TODO 待做
        g2.setPaint(Color.WHITE);
        Matrix3x3f view = getViewportTransform();
        for (PrototypeAsteroid a : asteroids)
            a.draw(g2,view);
        g2.drawString("按下 SPACE 点火",30,130);
        g2.dispose();
    }
}
