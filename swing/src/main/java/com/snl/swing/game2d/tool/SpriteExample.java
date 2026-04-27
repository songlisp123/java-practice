package com.snl.swing.game2d.tool;

import com.snl.swing.game2d.frame.WindowFramework;
import com.snl.swing.game2d.util.ResourceLoader;
import com.snl.swing.game2d.util.Sprite;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SpriteExample extends WindowFramework {
    private Sprite sprite;
    private Vector2D pos;
    private Vector2D vel;
    private float rot;
    private float rotDelta;

    public SpriteExample() {
        appTitle = "Sprite Example";
        pos = new Vector2D();
        vel = new Vector2D(0.25f, -0.3f);
        rotDelta = (float) Math.toRadians(90.0);
    }

    @Override
    protected void initialize() {
        super.initialize();
        InputStream in = ResourceLoader.load(SpriteExample.class,
                "赛朋博克1.png", "赛朋博克1.PNG");

        try {
            BufferedImage image = ImageIO.read(in);
            Vector2D topLeft = new Vector2D(-0.25f, 0.25f);
            Vector2D bottomRight = new Vector2D(0.25f, -0.25f);
            sprite = new Sprite(image, topLeft, bottomRight);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateObjects(double delta) {
        super.updateObjects(delta);
        pos = pos.add(vel.mul(delta));
        if (pos.x < -appWorldWidth / 2.0f) {
            pos.x = -appWorldWidth / 2.0f;
            vel.x = -vel.x;
        } else if (pos.x > appWorldWidth / 2.0f) {
            pos.x = appWorldWidth / 2.0f;
            vel.x = -vel.x;
        }
        if (pos.y < -appWorldHeight / 2.0f) {
            pos.y = -appWorldHeight / 2.0f;
            vel.y = -vel.y;
        } else if (pos.y > appWorldHeight / 2.0f) {
            pos.y = appWorldHeight / 2.0f;
            vel.y = -vel.y;
        }
        rot += rotDelta * delta;
    }

    @Override
    protected void render(Graphics g) {
        super.render(g);
        g.setColor(Color.GREEN);
        g.drawRect(0, 0, getScreenWidth() - 1, getScreenHeight() - 1);
        sprite.render((Graphics2D) g, getViewportTransform(), pos, rot);
    }


    public static void main(String[] args) {
        launchApp(new SpriteExample());
    }
}
