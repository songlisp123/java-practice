package com.snl.test.java2D.shape;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

public class KnifeShape extends JPanel {

    private Point2D point2D = new Point2D.Double(50,50);
    private double hitWidth = 10;
    private double hitHeight = 5;
    private double height = 5;

    private double slideWidth = 25;
    private double slideHeight = 5;

    private GeneralPath path;

    public KnifeShape() {
        setBackground(Color.black);
        path = new GeneralPath();
        double x = point2D.getX();
        double y = point2D.getY();
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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.green);
//        g2.draw(path);
        g2.fill(path);
        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400,300);
    }

    private static void createUi() {
        JFrame f = new JFrame("Java2D Gun Outline");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new KnifeShape());
        f.setLocationRelativeTo(null);
        f.pack();
        f.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(KnifeShape::createUi);
    }
}
