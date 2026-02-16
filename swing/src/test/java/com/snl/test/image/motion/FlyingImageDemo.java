package com.snl.test.image.motion;

import com.snl.test.java2D.coords.DiKaErPlus;
import com.snl.test.java2D.game.PolygonWrapper;
import com.snl.test.java2D.vector.Matrix3x3f;
import com.snl.test.java2D.vector.Vector2D;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class FlyingImageDemo extends DiKaErPlus implements ListDataListener {

    Vector2D pos;
    Vector2D speed;
    double rot,rotDelta;
    BufferedImage bi;
    Vector2D imageW;
    PolygonWrapper wrapper;
    boolean Kangjuchi;
    float alpha,alpha02;
    int fadeIndex;
    float[][] fade = {
            {1.0f,-0.1f,1.0f,0.0f}, //只有第一个网格会降低alpha
            {0.0f,0.1f,1.0f,-0.1f}, //第一个网格增加alpha
            {1.0f,0.0f,0.0f,0.1f}, //第二个网格减少alpha
            {1.0f,-0.1f,1.0f,-0.1f}, //全部降低透明
            {0.0f,0.05f,0.0f,0.05f}, //全部增加透明
    };


    ComboxModelDemo m1,m2;
    final String[] rots = new String[] {
            "仿射变换","仿射变换操作","纹理绘制"
    };

    final String[] a = new String[] {
            "最近邻算法","双线性算法","三次方算法"
    };

    Paint paint;
    Paint[] paints;
    int paintIndex;

    public FlyingImageDemo() throws HeadlessException {
        createUiAndShow();
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        alpha = fade[fadeIndex][0];
        alpha02 = fade[fadeIndex][2];
        imageW = new Vector2D(8,8);
        pos = new Vector2D();
        bi = createBufferedImage((int) imageW.getX(),
                (int) imageW.getY(),BufferedImage.TYPE_INT_ARGB);
        bi = checkBox(bi);
        speed = new Vector2D(1,0);
        rotDelta = Math.PI / 3;
        wrapper = new PolygonWrapper(wordWidth,wordHeight);
        paint = createGradientPaint();
        paints = new Paint[5];
        fillPaints();
    }

    private void fillPaints() {
        float[] f = {0.0f,0.2f,0.4f,0.8f,1.0f};
        Color[] colors = {
                Color.YELLOW,Color.GREEN,Color.CYAN,Color.BLUE,new Color(1.0f,0.1f,0.5f,0.0f)
        };
        Paint p;
        p = new LinearGradientPaint(0,0,bi.getWidth(),bi.getHeight(),f,colors);
        paints[0] = p;
        float radius = (float) bi.getWidth() / 2;
        p = new RadialGradientPaint(bi.getWidth() / 2.0f,bi.getHeight() / 2.0f,radius,f,colors);
        paints[1] = p;
        ImageIcon icon = new ImageIcon("ten.gif");
        Image image = icon.getImage();
        BufferedImage si = new BufferedImage(image.getWidth(null),
                image.getHeight(null),BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = si.createGraphics();
        g2.drawImage(image,0,0,null);
        g2.dispose();
        Rectangle2D anchor = new Rectangle2D.Double(0,0,si.getWidth(),si.getHeight());
        p = new TexturePaint(si,anchor);
        paints[2] = p;
        paints[3] = createGradientPaint();
        paints[4] = createGradientPaint();
    }

    private BufferedImage checkBox(BufferedImage bi) {
        BufferedImage r = new BufferedImage(bi.getWidth(),
                bi.getHeight(),bi.getTransparency());
        Graphics2D g3 = r.createGraphics();
        g3.setComposite(AlphaComposite.Clear);
        g3.fillRect(0,0,r.getWidth(),r.getHeight());

        g3.setComposite(AlphaComposite.Src);
        g3.drawImage(bi,null,null);
        int size = 8;
        float h =(float) bi.getHeight() / size;
        float w =(float) bi.getWidth() / size;
        for (int row = 0;row<size;row++) {
            for (int col = 0;col<size;col++) {
                if (((row + col) % 2) == 0) {
                    //如果是偶数
                    g3.setColor(new Color(1.0f,1.0f,1.0f,alpha02));
                }else {
                    g3.setColor(new Color(1.0f,.0f,1.0f,alpha));
                }
                Rectangle2D c = new Rectangle2D.Double(
                        col * w,row * h,w,h
                );
                g3.fill(c);
            }
        }
        return r;

    }

    private Paint createGradientPaint() {
        return new GradientPaint(0,0,Color.RED,bi.getWidth(),0,Color.blue);
    }

    private void createUiAndShow() {
        m1 = new ComboxModelDemo(rots);
        m1.addListDataListener(this);
        var comboBox = new JComboBox<>(m1);

        m2 = new ComboxModelDemo(a);
        m2.addListDataListener(this);
        var comboBox02 = new JComboBox<>(m2);
        JPanel cp = new JPanel();

        cp.setLayout(new GridBagLayout());
        cp.setBackground(Color.black);
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0.0;
        constraints.weighty = 0.2;
        constraints.anchor = GridBagConstraints.SOUTH;
        constraints.fill = GridBagConstraints.NONE;
        constraints.insets = new Insets(10,30,10,0);
        JLabel label = new JLabel("操作", JLabel.RIGHT);
        label.setForeground(Color.GREEN);
        cp.add(label,constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.5;
        constraints.insets = new Insets(10,0,10,0);
        cp.add(comboBox,constraints);

        constraints.gridx = 2;
        constraints.weightx = .0;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.insets = new Insets(10,0,10,0);
        JLabel l1 = new JLabel("算法", JLabel.RIGHT);
        l1.setForeground(Color.GREEN);
        cp.add(l1,constraints);

        constraints.gridx = 3;
        constraints.weightx = 0.4;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.insets = new Insets(10,0,10,30);
        cp.add(comboBox02,constraints);
        getContentPane().add(cp,BorderLayout.PAGE_END);
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_B)) {
            Kangjuchi = !Kangjuchi;
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_LEFT))
        {
            paintIndex--;
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_RIGHT)) {
            paintIndex++;
        }
        if (paintIndex <0)
            paintIndex = 0;
        if (paintIndex == paints.length) {
            paintIndex = paints.length - 1;
        }
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        rot += rotDelta * delta;
        if ("仿射变换操作".equals(m1.newItem)) {
            alpha += (float) (fade[fadeIndex][1] * delta);
            alpha02 += (float) (fade[fadeIndex][3] * delta);
            if (alpha < 0.0f || alpha > 1.0f || alpha02 < 0.0f || alpha02 > 1.0f) {
                if (fadeIndex++ == fade.length - 1) {
                    fadeIndex = 0;
                }
                alpha = fade[fadeIndex][0];
                alpha02 = fade[fadeIndex][2];
            }

        }
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        if (Kangjuchi)
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //TODO 待做
        g2.setPaint(Color.WHITE);
        g2.drawString("抗锯齿: [%s]".formatted(Kangjuchi),30,130);
        g2.drawString("左右键切换纹理",30,150);

        Matrix3x3f mat = getViewportTransform();
        Vector2D p = mat.mul(pos);

        AffineTransform af  = AffineTransform.getTranslateInstance(p.getX(),p.getY());
        af.concatenate(AffineTransform.getRotateInstance(rot));
        af.concatenate(AffineTransform.getScaleInstance(scaleX,scaleY));
        af.translate(-bi.getWidth() / 2.0,-bi.getHeight() / 2.0);

        AffineTransform temp = g2.getTransform();

        if ("仿射变换".equals(m1.newItem)) {
            g2.drawImage(bi, af, null);
        } else if ("仿射变换操作".equals(m1.newItem)) {
            AffineTransformOp afo;
            int type = switch (m2.newItem) {
                case "双线性算法" -> AffineTransformOp.TYPE_BILINEAR;
                case "三次方算法" -> AffineTransformOp.TYPE_BICUBIC;
                default -> AffineTransformOp.TYPE_NEAREST_NEIGHBOR;
            };
            afo = new AffineTransformOp(af,type);
            g2.drawImage(afo.filter(bi,null), 0,0,null);
        } else if ("纹理绘制".equals(m1.newItem)) {
            //；
            g2.setTransform(af);
            g2.setPaint(paints[paintIndex]);
            g2.fillRect(0,0,bi.getWidth(),bi.getHeight());
            g2.setTransform(temp);
        }
        g2.dispose();
    }

    public static void main(String[] args) {
        launchGame(new FlyingImageDemo());
    }

    @Override
    public void intervalAdded(ListDataEvent e) {

    }

    @Override
    public void intervalRemoved(ListDataEvent e) {

    }

    @Override
    public void contentsChanged(ListDataEvent e) {
        Object source = e.getSource();
        if (source == m1)
            System.out.println(m1.newItem);
        else if (source == m2) {
            System.out.println(m2.newItem);
        }
    }

}
