package com.snl.swing.game.anime;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

/**
 * 文本效果。剪切缩放，旋转和平移。
 */
public class TextEffect implements Part {

    public static final int INC = 1;
    public static final int DEC = 2;
    public static final int R = 4;
    public static final int RI = R | INC;
    public static final int RD = R | DEC;
    public static final int SCALE = 8;
    public static final int SCI = SCALE | INC;
    public static final int SCD = SCALE | DEC;
    public static final int SCX = 16; //沿着x负轴缩放
    public static final int SCXI = SCX | SCALE | INC;
    public static final int SCXD = SCX | SCALE | DEC;
    public static final int SCY = 32; //沿y轴缩放
    public static final int SCYI = SCY | SCALE | INC;
    public static final int SCYD = SCY | SCALE | DEC;
    public static final int AL = 64; //aliha规则
    public static final int CLIP = 128; // 剪切
    public static final int NOP = 512; // 无纹理绘制

    //效果起始时间
    private int start,end;
    //类型
    private int type;
    private double sInc,rInc;
    private Shape[] shapes,txTShapes;
    private int sw;
    //效果增强
    private double numRev;
    private Paint paint;
    private double sx, sy, rotate;

    public TextEffect(String text,Font font,int type,Paint paint,int start,int end) {
        if (text == null || text.isEmpty())
            throw new IllegalArgumentException("参数异常");
        this.type = type;
        this.paint = paint;
        if (start > end) {
            this.start = end;
            this.end = start;
        }else {
            this.start = start;
            this.end = end;
        }
        setIncrements(2);

        char[] chars = text.toCharArray();
        shapes = new Shape[chars.length];
        txTShapes = new Shape[chars.length];
        FontRenderContext frc = new FontRenderContext(null,false,false);
        TextLayout tl = new TextLayout(text,font,frc);
        sw = tl.getOutline(null).getBounds().width;
        for (int j = 0;j<chars.length;j++) {
            String s =String.valueOf(chars[j]);
            shapes[j] = new TextLayout(s,font,frc).getOutline(null);
        }
    }

    private void setIncrements(double numRev) {
        this.numRev = numRev;
        rInc = 360.0 / ((end - start) / this.numRev);
        sInc = 1.0 / (end - start);
        if ((type & SCX) != 0 || (type & SCY) != 0)
        {
            sInc *= 2;
        }
        if ((type & DEC) != 0) {
            rInc = -rInc;
            sInc = -sInc;
        }
    }

    @Override
    public int getEnd() {
        return end;
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public void render(int w, int h, Graphics2D g2) {
        Composite saveAC = null;
        if ((type & AL) != 0 && sx > 0 && sx < 1) {
            saveAC = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, (float) sx));
        }
        GeneralPath path = null;
        if ((type & CLIP) != 0) {
            path = new GeneralPath();
        }
        if (paint != null) {
            g2.setPaint(paint);
        }
        for (Shape txTShape : txTShapes) {
            if ((type & CLIP) != 0) {
                path.append(txTShape, false);
            } else {
                g2.fill(txTShape);
            }
        }
        if ((type & CLIP) != 0) {
            g2.clip(path);
        }
        if (saveAC != null) {
            g2.setComposite(saveAC);
        }
    }

    @Override
    public void step(int w, int h) {
        float charWidth = (w - sw) / 2.0F;
        for (int i = 0;i<shapes.length;i++) {
            AffineTransform af = new AffineTransform();
            Rectangle maxBounds = shapes[i].getBounds();
            af.translate(charWidth, (h + maxBounds.height) / 2.0F);
            charWidth += maxBounds.width + 1;
            Shape shape = af.createTransformedShape(shapes[i]);
            Rectangle b1 = shape.getBounds();
            if ((type & R) != 0) {
                af.rotate(Math.toRadians(rotate));
            }
            if ((type & SCALE) != 0) {
                af.scale(sx,sy);
            }
            shape = af.createTransformedShape(shapes[i]);

            Rectangle b2 = shape.getBounds();
            double xx = (b1.getX() + b1.getWidth() / 2)
                    - (b2.getX() + b2.getWidth() / 2);
            double yy = (b1.getY() + b1.getHeight() / 2)
                    - (b2.getY() + b2.getHeight() / 2);
            AffineTransform toCenterAT = new AffineTransform();
            toCenterAT.translate(xx, yy);
            toCenterAT.concatenate(af);
            txTShapes[i] = toCenterAT.createTransformedShape(shapes[i]);
        }
        rotate += rInc;
        if ((type & SCX) != 0) {
            sx += sInc;
        } else if ((type & SCY) != 0) {
            sy += sInc;
        } else {
            sx += sInc;
            sy += sInc;
        }
    }

    @Override
    public void reset(int w, int h) {
        if (type == SCXI) {
            sx = -1.0;
            sy = 1.0;
        } else if (type == SCYI) {
            sy = -1.0;
            sx = 1.0;
        }else {
            sx = sy = (type & DEC) !=0 ? 1.0 : 0.0;
        }
        rotate = 0;
    }

    public int getType() {
        return type;
    }

}
