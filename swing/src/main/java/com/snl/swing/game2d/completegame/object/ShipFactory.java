package com.snl.swing.game2d.completegame.object;

import com.snl.swing.game2d.tool.Vector2D;
import com.snl.swing.game2d.util.ResourceLoader;
import com.snl.swing.game2d.util.Sprite;
import com.snl.swing.game2d.util.XMLUtility;
import org.w3c.dom.Element;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Vector;

public class ShipFactory {
    private PolygonWrapper wrapper;
    private Vector2D[] polygon;
    private Sprite shipRegular;
    private Sprite shipGlow;

    public ShipFactory(PolygonWrapper wrapper) {
        this.wrapper = wrapper;
    }

    public void loadFactory(Element xml) {
        Vector<Vector2D> points = new Vector<Vector2D>();
        String spritePath = xml.getAttribute("sprite");
        String glowPath = xml.getAttribute("glow");
        String bounds = xml.getAttribute("bounds");
        for (Element coords : XMLUtility.getAllElements(xml, "coord")) {
            float x = Float.parseFloat(coords.getAttribute("x"));
            float y = Float.parseFloat(coords.getAttribute("y"));
            points.add(new Vector2D(x, y));
        }
        polygon = points.toArray(new Vector2D[0]);
        float bound = Float.parseFloat(bounds);
        Vector2D topLeft = new Vector2D(-bound / 2.0f, bound / 2.0f);
        Vector2D bottomRight = new Vector2D(bound / 2.0f, -bound / 2.0f);
        BufferedImage image = loadSprite(spritePath);
        shipRegular = new Sprite(image, topLeft, bottomRight);
        image = loadSprite(glowPath);
        shipGlow = new Sprite(image, topLeft, bottomRight);
    }

    public Ship createShip() {
        Ship ship = new Ship(wrapper);
        ship.setAlive(true);
        ship.setPolygon(polygon);
        ship.setGlowSprite(shipGlow);
        ship.setShipSprite(shipRegular);
        return ship;
    }

    private BufferedImage loadSprite(String path) {
        InputStream stream = ResourceLoader.load(
                ShipFactory.class, "./file/images/" + path, "./file/images/" + path
        );
        try {
            return ImageIO.read(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
