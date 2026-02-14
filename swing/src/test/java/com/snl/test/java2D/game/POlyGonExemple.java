package com.snl.test.java2D.game;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class POlyGonExemple extends DiKaErPlus {

    Vector2D[] poly;
    PolygonWrapper wrapper;
    List<Vector2D[]> polys = new ArrayList<>();
    Vector2D pos;
    boolean moving;

    public POlyGonExemple() throws HeadlessException {
        appFont = new Font("隶书",Font.BOLD,18);
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        wrapper = new PolygonWrapper(wordWidth,wordHeight);
        resetPos();
    }

    void  resetPos() {
        poly = new Vector2D[] { new Vector2D(-1, 1),
                new Vector2D(1, 1), new Vector2D(1, -1),
                new Vector2D(-1, -1), };
        pos = new Vector2D();
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
        {
            moving = !moving;
        }
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        polys.clear();
        pos = wrapper.wrapPos(pos);
        if (moving)
            pos = pos.add(new Vector2D(.1,0));
        Vector2D[] world = transform(poly, Matrix3x3f.translate(pos));
        polys.add(world);
        wrapper.wrapPolygon(world, polys);
    }

    private Vector2D[] transform(Vector2D[] poly, Matrix3x3f mat) {
        Vector2D[] r= new Vector2D[poly.length];
        for (int i=0;i<poly.length;i++)
        {
            r[i] = mat.mul(poly[i]);
        }
        return r;
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //TODO 待做
        g2.setPaint(Color.WHITE);
        for (Vector2D[] s : polys)
            drawPoly(g2,s,true);
        g2.drawString("按下 SPACE 点火",30,130);
        g2.dispose();
    }

    @Override
    protected void reset() {
        super.reset();
        resetPos();
    }

    public static void main(String[] args) {
        launchGame(new POlyGonExemple());
    }
}
