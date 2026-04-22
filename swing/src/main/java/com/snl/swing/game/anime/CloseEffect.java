package com.snl.swing.game.anime;

import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Polygon;
import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game.math.geo.curve.CubicCurve;
import com.snl.swing.game.math.geo.curve.Curve;
import com.snl.swing.game.math.geo.Path;
import com.snl.swing.game.utils.Geometry;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class CloseEffect implements Part {

    private final Component surf;
    public static final int WID = 1;
    public static final int HEI = 2;
    public static final int OVAL = 4;
    public static final int RECT = 8;
    public static final int RAND = 16;
    public static final int ARC = 32;
    //自定义形状
    public static final int CUSTOM = 64;
    public static final int CURVE = 128;
    private int type;
    private int beginning, ending;
    private BufferedImage bimg;
    private Shape shape;
    //缩放，增加
    private double zoom, extent;
    //递增，递减
    private double zIncr, eIncr;
    //随机性
    private boolean doRandom;

    private double rot;
    
    Polygon polygon;

    Path path;

    Curve curve = new CubicCurve(0.5,0.5,-1,1.2,-0.3,0.35,1.3,2.3);

    public CloseEffect(int type, int beg, int end, Component surf) {
        this.type = type;
        this.beginning = beg;
        this.ending = end;
        this.surf = surf;
        zIncr = -(2.0 / (ending - beginning));
        eIncr = 360.0 / (ending - beginning); //旋转角度
        doRandom = (type & RAND) != 0;
        reset(surf.getWidth(),surf.getHeight());

        //测试程序
        polygon = new Polygon(new Vector2D[]{
                new Vector2D(0,3),
                new Vector2D(2,2),
                new Vector2D(3,0),
                new Vector2D(2,-2),
                new Vector2D(0,-3),
                new Vector2D(-2,-2),
                new Vector2D(-3,0),
                new Vector2D(-2,2)
        });

        //路径
        path = new Path();
        path.moveTo(0,0);
        path.lineTo(1,1);
        path.quadTo(2,1,3,2);
        path.lineTo(5,6);
    }

    public CloseEffect(int type, int beginning, int ending, Component surf, Shape shape) {

        this.beginning = beginning;
        this.ending = ending;
        this.surf = surf;
        if (shape == null)
            this.type = RAND;
        else
        {
            this.shape = shape;
            this.type = type;
        }
        zIncr = -(2.0 / (ending - beginning));
        eIncr = 360.0 / (ending - beginning); //旋转角度
        doRandom = (type & RAND) != 0;
        reset(surf.getWidth(),surf.getHeight());
    }

    @Override
    public int getEnd() {
        return ending;
    }

    @Override
    public int getStart() {
        return beginning;
    }

    @Override
    public void render(int w, int h, Graphics2D g2) {
        g2.clip(shape);
        g2.drawImage(bimg, 0, 0, null);
    }

    @Override
    public void step(int w, int h) {
        if (bimg == null) {
            int biw = surf.getWidth();
            int bih = surf.getHeight();
            bimg = new BufferedImage(biw, bih,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D big = bimg.createGraphics();
            big.setComposite(AlphaComposite.Src);
            big.fillRect(0,0,bimg.getWidth(),bimg.getHeight());
//            big.drawImage(surf.bimg, 0, 0, null);
            big.dispose();
        }
        double z = Math.min(w, h) * zoom;
        if ((type & OVAL) != 0) {
            shape = new Ellipse2D.Double(w / 2 - z / 2, h / 2 - z / 2, z,
                    z);
        } else if ((type & ARC) != 0) {
            shape = new Arc2D.Double(-100, -100, w + 200, h + 200, 90,
                    extent, Arc2D.PIE);
            extent -= eIncr;
        } else if ((type & RECT) != 0) {
            if ((type & WID) != 0) {
                shape = new Rectangle2D.Double(w / 2 - z / 2, 0, z, h);
            } else if ((type & HEI) != 0) {
                shape = new Rectangle2D.Double(0, h / 2 - z / 2, w, z);
            } else {
                shape = new Rectangle2D.Double(w / 2 - z / 2, h / 2 - z
                        / 2, z, z);
            }
        } else if ((type & CUSTOM) != 0) {
            Matrix3x3f rotate = Matrix3x3f.rotate(Math.toRadians(rot))
                    .mul(Matrix3x3f.scale(zoom,zoom))
                    .mul(Matrix3x3f.shear(0.2,0.5));
            GeneralPath path = getPath(Geometry.transformPolygon(polygon, null, rotate));
            rot += eIncr;
            shape = path;
        } else if ((type & CURVE) != 0) {
            Matrix3x3f rotate = Matrix3x3f.rotate(Math.toRadians(rot))
                    .mul(Matrix3x3f.scale(zoom,zoom))
                    .mul(Matrix3x3f.shear(0.15,0.15));


            GeneralPath path = new GeneralPath();
            Curve c = Geometry.transFormCurve(curve, null, rotate);
            double tx = Math.abs(surf.getBounds().getCenterX());
            double ty = Math.abs(surf.getBounds().getCenterY());
            path.moveTo(c.getStartPointX() * 50 + tx,c.getStartPointY() * 50 + ty);
            path.curveTo(c.getControlPoint01X() * 50 + tx,c.getControlPoint01Y() * 50 + ty,
                    c.getControlPoint02X() * 50 + tx,c.getControlPoint02Y() * 50 + ty,
                    c.getEndPointX() * 50 + tx,c.getEndPointY() * 50 + ty);
            rot += eIncr;
            shape = path;
        }
        zoom += zIncr;
    }

    private GeneralPath getPath(Polygon polygon) {
        GeneralPath path = new GeneralPath();
        Vector2D[] vs = polygon.getVertices();
        double f = 50;
        double tx = Math.abs(surf.getBounds().getCenterX());
        double ty = Math.abs(surf.getBounds().getCenterY());
        for (int i = 0;i<vs.length;i++) {
            if (i == 0)
            {
                path.moveTo(vs[0].x * f + tx,vs[0].y * f + ty);
            }
            path.lineTo(vs[i].x * f + tx,vs[i].y * f + ty);
        }
        return path;
    }

    @Override
    public void reset(int w, int h) {
        if (doRandom) {
            int num = (int) (Math.random() * 6.0);
            switch (num) {
                case 1:
                    type = RECT;
                    break;
                case 2:
                    type = RECT | WID;
                    break;
                case 3:
                    type = RECT | HEI;
                    break;
                case 4:
                    type = ARC;
                    break;
                case 5 :
                    type = CUSTOM;
                    break;
                case 6 :
                    type = CURVE;
                    break;
                case 0:
                default:
                    type = OVAL;
            }
        }
        shape = null;
        bimg = null;
        extent = 360.0;
        zoom = 2.0;
        rot = 0.0;
    }
}
