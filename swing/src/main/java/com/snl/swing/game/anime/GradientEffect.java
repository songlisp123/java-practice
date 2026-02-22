package com.snl.swing.game.anime;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GradientEffect implements Part {

    public static final int INC = 1; //增加
    public static final int DEC = 2; //降低
    public static final int CNT = 4; //中心
    public static final int WID = 8; //宽度
    public static final int WI = WID | INC; //宽度增加
    public static final int WD = WID | DEC; //宽度减小哎
    public static final int HEI = 16;   //宽度
    public static final int HI = HEI | INC; //宽度增加
    public static final int HD = HEI | DEC; //宽度减小
    public static final int SPL = 32 | CNT;// 分割
    public static final int SIW = SPL | INC | WID;
    public static final int SDW = SPL | DEC | WID;
    public static final int SIH = SPL | INC | HEI;
    public static final int SDH = SPL | DEC | HEI;
    public static final int BUR = 64 | CNT; // 爆炸
    public static final int BURI = BUR | INC;
    public static final int BURD = BUR | DEC;
    public static final int NF = 128;  //无纹理填充

    //渐变颜色
    private Color c1, c2;
    //开始，结束帧
    private int start, end;
    //增加
    private float incr, index;
    //???
    private java.util.List<Rectangle2D> rect = new ArrayList<Rectangle2D>();
    //纹理
    private List<GradientPaint> grad = new ArrayList<GradientPaint>();
    //类型
    private int type;

    public GradientEffect(int type,Color c1,Color c2,int start,int end) {
        this.type = type;
        this.c1 = c1;
        this.c2 = c2;
        if (start > end) {
            this.start = end;
            this.end = start;
        }else {
            this.start = start;
            this.end = end;
        }
        reset();
    }

    @Override
    public void render(int w, int h, Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        for (int i = 0; i < grad.size(); i++) {
            g2.setPaint(grad.get(i));
            if ((type & NF) == 0) {
                g2.fill(rect.get(i));
            }
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
    }

    @Override
    public void step(int w, int h) {
        rect.clear();
        grad.clear();

        if ((type & WID) != 0) {
            float w2 , x1 , x2 ;
            if ((type & SPL) != 0) {
                w2 = w * 0.5f;
                x1 = w * (1.0f - index);
                x2 = w * index;
            } else {
                w2 = w * index;
                x1 = x2 = w2;
            }
            rect.add(new Rectangle2D.Float(0, 0, w2, h));
            rect.add(new Rectangle2D.Float(w2, 0, w - w2, h));
            grad.add(new GradientPaint(0, 0, c1, x1, 0, c2));
            grad.add(new GradientPaint(x2, 0, c2, w, 0, c1));
        } else if ((type & HEI) != 0) {
            float h2 = 0, y1 = 0, y2 = 0;
            if ((type & SPL) != 0) {
                h2 = h * 0.5f;
                y1 = h * (1.0f - index);
                y2 = h * index;
            } else {
                h2 = h * index;
                y1 = y2 = h2;
            }
            rect.add(new Rectangle2D.Float(0, 0, w, h2));
            rect.add(new Rectangle2D.Float(0, h2, w, h - h2));
            grad.add(new GradientPaint(0, 0, c1, 0, y1, c2));
            grad.add(new GradientPaint(0, y2, c2, 0, h, c1));
        } else if ((type & BUR) != 0) {

            float w2 = w / 2;
            float h2 = h / 2;

            rect.add(new Rectangle2D.Float(0, 0, w2, h2));
            rect.add(new Rectangle2D.Float(w2, 0, w2, h2));
            rect.add(new Rectangle2D.Float(0, h2, w2, h2));
            rect.add(new Rectangle2D.Float(w2, h2, w2, h2));

            float x1 = w * (1.0f - index);
            float x2 = w * index;
            float y1 = h * (1.0f - index);
            float y2 = h * index;

            grad.add(new GradientPaint(0, 0, c1, x1, y1, c2));
            grad.add(new GradientPaint(w, 0, c1, x2, y1, c2));
            grad.add(new GradientPaint(0, h, c1, x1, y2, c2));
            grad.add(new GradientPaint(w, h, c1, x2, y2, c2));
        } else if ((type & NF) != 0) {
            float y = h * index;
            grad.add(new GradientPaint(0, 0, c1, 0, y, c2));
        }

        if ((type & INC) != 0 || (type & DEC) != 0) {
            index += incr;
        }
    }

    private void reset() {
        this.reset(0,0);
    }

    @Override
    public void reset(int w, int h) {
        incr = 1.0f / (end - start);
        if ((type & CNT) != 0) {
            incr /= 2.3f;
        }
        if ((type & CNT) != 0 && (type & INC) != 0) {
            index = 0.5f;
        } else if ((type & DEC) != 0) {
            index = 1.0f;
            incr = -incr;
        } else {
            index = 0.0f;
        }
        index += incr;
    }

    @Override
    public int getEnd() {
        return end;
    }

    @Override
    public int getStart() {
        return start;
    }

}
