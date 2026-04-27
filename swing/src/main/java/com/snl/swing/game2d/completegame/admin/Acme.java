package com.snl.swing.game2d.completegame.admin;

import com.snl.swing.game2d.CompleteGame;
import com.snl.swing.game2d.completegame.object.Ship;
import com.snl.swing.game2d.tool.Matrix3x3f;
import com.snl.swing.game2d.tool.Vector2D;
import com.snl.swing.game2d.util.Utility;

import java.awt.*;

public class Acme {
    private CompleteGame app;
    private Ship ship;

    public Acme(CompleteGame app) {
        this.app = app;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public void drawScore(Graphics2D g, int score) {
        g.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );
        String toShow = "" + score;
        while (toShow.length() < 6) {
            toShow = "0" + toShow;
        }
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(Color.GREEN);
        Utility.drawCenteredString(g, app.getScreenWidth(), 0, toShow);
    }

    public void drawLives(Graphics2D g, Matrix3x3f view, int lives) {
        double w = ship.getWidth();
        double h = ship.getHeight();
        double x = -0.95f + w;
        double y = 1.0f - h / 2.0f;
        for (int i = 0; i < lives; ++i) {
            x += w * i;
            ship.setAngle((float) Math.toRadians(90));
            ship.setPotition(new Vector2D(x, y));
            ship.update(0.0f);
            ship.draw(g, view);
        }
    }
}
