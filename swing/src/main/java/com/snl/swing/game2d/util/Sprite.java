package com.snl.swing.game2d.util;

import com.snl.swing.game2d.tool.Matrix3x3f;
import com.snl.swing.game2d.tool.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Sprite {
    private BufferedImage image;
    private BufferedImage scaled;
    private Vector2D topLeft;
    private Vector2D bottomRight;

    public Sprite(BufferedImage image, Vector2D topLeft, Vector2D bottomRight) {
        this.image = image;
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public void render(Graphics2D g, Matrix3x3f view) {
        render(g, view, new Vector2D(), 0.0f);
    }

    public void render(Graphics2D g, Matrix3x3f view, Vector2D position, double angle) {
        if (image != null) {
            Vector2D tl = view.mul(topLeft);
            Vector2D br = view.mul(bottomRight);
            int width = (int) Math.abs(br.x - tl.x);
            int height = (int) Math.abs(br.y - tl.y);
            if (scaled == null || width != scaled.getWidth() ||
                    height != scaled.getHeight()) {
                scaled = Utility.scaleImage(image, width, height);
            }
            g.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR
            );
            Vector2D screen = view.mul(position);
            AffineTransform transform =
                    AffineTransform.getTranslateInstance(screen.x, screen.y);
            transform.rotate(-angle);
            transform.translate(-scaled.getWidth() / 2, -scaled.getHeight() / 2);
            g.drawImage(scaled, transform, null);
        }
    }

    public void scaleImage(Matrix3x3f view) {
        Vector2D screenTopLeft = view.mul(topLeft);
        Vector2D screenBottomRight = view.mul(bottomRight);
        int scaledWidth = (int) Math.abs(screenBottomRight.x - screenTopLeft.x);
        int scaledHeight = (int) Math.abs(screenBottomRight.y - screenTopLeft.y);
        scaled = Utility.scaleImage(image, scaledWidth, scaledHeight);
    }
}
