package com.snl.swing.game2026.test;

import com.snl.swing.game.math.AABB;
import com.snl.swing.game.math.Circle;
import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game2026.map.*;
import com.snl.swing.game2026.map.tiled.objects.AABBObject;
import com.snl.swing.game2026.map.tiled.objects.CircleObject;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Iterator;

public class TestMap extends TestFrame implements MapRender {
    
    private Map map;

    @Override
    protected void gameInitial() {
        super.gameInitial();
        map = new Map();
        MapLayers layers = new MapLayers();
        MapLayer ml = new MapLayer();
        MapObjects objects = new MapObjects();
        AABBObject object = new AABBObject();
        AABB aabb = new AABB(
                new Vector2D(-0.1,-0.1),new Vector2D(0.1,.1)
        );
        object.setAabb(aabb);
        objects.addMapObject(object);
        ml.setObjects(objects);
        ml.setVisible(true);
        layers.getLayers().add(ml);


        MapLayer l2 = new MapLayer();
        l2.setVisible(true);
        MapObjects obs = new MapObjects();
        CircleObject c = new CircleObject();
        Circle circle = new Circle(1.25,new Vector2D(-1,3));
        c.setCircle(circle);
        obs.addMapObject(c);

        l2.setObjects(obs);
        layers.getLayers().add(l2);
        
        map.setLayers(layers);
        
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE)) {
            //TODO
            MapLayer first = map.getLayers().getLayers().getFirst();
            first.setVisible(!first.isVisible());
        }
    }

    @Override
    public void drawContent(Graphics2D g2) {
        this.render(g2);
    }

    public static void main(String[] args) {
        launchGame(new TestMap());
    }

    @Override
    public void render(Graphics2D g2) {
        MapLayers layers = map.getLayers();
        for (MapLayer next : layers) {
            if (!next.isVisible()) continue;
            MapObjects objects = next.getObjects();
            for (MapObject mo : objects) {
                if (mo instanceof AABBObject aabbObject)
                    drawAAbb(g2, aabbObject.getAabb(), true);
                else if (mo instanceof CircleObject co) {
                    drawCircle(g2, co.getCircle(), true);
                }
            }
        }
    }

    @Override
    public void render() {

    }

    @Override
    public void render(int[] layers) {

    }
}
