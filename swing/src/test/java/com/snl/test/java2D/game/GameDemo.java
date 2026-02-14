package com.snl.test.java2D.game;

import com.snl.test.java2D.UTIL.RandomGeneratorClass;
import com.snl.test.java2D.UTIL.Star;
import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class GameDemo  extends DiKaErPlus {

    final int MAX_STARTS = 1500;
    Star[] stars;
    private PolygonWrapper wrapper;
    private PrototypeShip ship;

    private PrototypeAsteroidFactory factory;

    List<PrototypeBullet> bullets = new ArrayList<>();
    List<PrototypeAsteroid> asteroids = new ArrayList<>();


    public GameDemo() throws HeadlessException {
        wordHeight = 8;
        wordWidth = 8;
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        fillStarts();
        wrapper = new PolygonWrapper(wordWidth,wordHeight);
        ship = new PrototypeShip(wrapper);
        factory = new PrototypeAsteroidFactory(wrapper);
        createAsteroids();
        resetView();
    }

    private void fillStarts() {
        double w = wordWidth;
        double h = wordHeight;
        stars = new Star[MAX_STARTS];
        for (int i= 0;i<MAX_STARTS;i++)
        {

            double x = RandomGeneratorClass.random(w);
            double y = RandomGeneratorClass.random(h);
            Vector2D pos = new Vector2D(x,y);
            Star star = new Star(pos);
            stars[i] = star;
        }
    }

    private void createAsteroids() {
        asteroids.clear();
        for (int i=0;i<10;i++)
        {
            Vector2D p = getAsteroidStartPosition();
            asteroids.add(factory.createLargeAsteroid(p));
        }
    }

    // create random position for an asteroid
    private Vector2D getAsteroidStartPosition() {
        double w =wordWidth;
        double angle = RandomGeneratorClass.random(Math.PI * 2 );
        double min = w / 4.0;
        double max = 2 * min;
        double r = RandomGeneratorClass.random(min,max);
        Vector2D polar = Vector2D.polar(r, angle);
        System.out.println(polar);
        return Vector2D.polar(r,angle);
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDown(KeyEvent.VK_A))
            ship.rotateLeft(delta);
        if (keyBoardEvent.keyDown(KeyEvent.VK_D))
            ship.rotateRight(delta);
        ship.setThrustion(keyBoardEvent.keyDown(KeyEvent.VK_W));
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
            bullets.add(ship.launchBullets());
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
//        resetView();
        updateStarts(delta);
        ship.update(delta);
        updateBullets(delta);
        updateShiTou(delta);
    }

    @Override
    protected void resetView() {
        viewMat = Matrix3x3f.translate(ship.getPosition().inv());
        axis.createAxis(getViewportTransform(),c,wordWidth);
    }

    private void updateShiTou(double delta) {
        for (PrototypeAsteroid a : asteroids)
            a.update(delta);
    }

    private void updateBullets(double delta) {
        List<PrototypeBullet> copy= new ArrayList<>(bullets);
        for (PrototypeBullet b : copy)
        {
            updateB(b,delta);
        }
    }

    private void updateB(PrototypeBullet b, double delta) {
        b.update(delta);
        if (!(wrapper.hasInWorld(b.getPos())))
            bullets.remove(b);
        else {
            List<PrototypeAsteroid> copy = new ArrayList<>(asteroids);
            for (PrototypeAsteroid a:copy)
            {
                if (a.contains(b.getPos()))
                {
                    bullets.remove(b);
                    asteroids.remove(a);
                    blood(a);
                }
            }
        }
    }

    private void blood(PrototypeAsteroid a) {
        Vector2D pos = a.getPos();
        if (a.getSize() == PrototypeAsteroid.Size.large)
        {
            asteroids.add(factory.createMiddleAsteroid(pos));
            asteroids.add(factory.createMiddleAsteroid(pos));
        } else if (a.getSize() == PrototypeAsteroid.Size.middle) {
            asteroids.add(factory.createSmallAsteroid(pos));
            asteroids.add(factory.createSmallAsteroid(pos));
        }
    }

    private void updateStarts(double delta) {
        for (Star s : stars)
            s.update(delta,minS,maxS);
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
        drawStars(g2,view);
        g2.setColor(Color.cyan);
        ship.draw(g2,view);
        drawBullets(g2,view);
        g2.setColor(Color.gray);
        drawShiTou(g2,view);
        g2.drawString("按下 SPACE 点火",30,130);
        g2.dispose();
    }

    private void drawShiTou(Graphics2D g2, Matrix3x3f view) {
        for (PrototypeAsteroid a : asteroids)
            a.draw(g2,view);
    }

    private void drawBullets(Graphics2D g2, Matrix3x3f view) {
        for (PrototypeBullet b : bullets)
            b.draw(g2,view);
    }

    private void drawStars(Graphics2D g2, Matrix3x3f view) {
        for (Star star : stars)
            star.draw(g2,view,minS,maxS);
    }

    public static void main(String[] args) {
        launchGame(new GameDemo());
    }

}
