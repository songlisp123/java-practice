package com.snl.swing.homework01.ui.imageFrame;

import com.snl.swing.homework01.utils.Utilies;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ImagePanel extends JPanel implements MouseListener , MouseMotionListener , ActionListener , TableModelListener {

    private BufferedImage mImage;
    private Point leftPoint;
    private Point eastPoint;
    private RectangularShape shape;
    private RectangularShape maksShape;

    //遮罩监听器
    private final List<MaskPropertyListener> listeners =
            new ArrayList<>();
    //颜色份量监听器列表
    private final List<ColorCompomentImplement> colorListeners =
            new ArrayList<>();
    //x坐标
    private  int x;
    //也坐标
    private int y;

    private final Pattern pattern = Pattern.compile("(png|gif|jepg|jpg|tff)$");

    public ImagePanel(LayoutManager layout) {
        super(layout);
    }

    public ImagePanel(String path) {
        Image image = Utilies.blockingLoad(path);
        mImage =  Utilies.makeBufferImage(image);
        //添加监听器
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public ImagePanel() {
        mImage = new BufferedImage(500,500,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = mImage.createGraphics();
        g2.setColor(Color.BLACK);
        g2.fillRect(0,0,mImage.getWidth(),mImage.getHeight());
        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(mImage.getWidth(),mImage.getHeight());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.drawImage(mImage,null,null);
        g2.setColor(Color.cyan);
        g2.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
        if (shape != null)
            g2.draw(shape);
        if (maksShape != null)
        {
            g2.setColor(Color.BLACK);
            g2.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER,0.6f
            ));
            g2.fill(maksShape);
        }
        g2.dispose();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //清空遮罩
//        maksShape = null;
//        fireUpdateEvent();
        //获取位置
        Point point = e.getPoint();
        //获取颜色模型
        ColorModel colorModel = mImage.getColorModel();
        //获取可写入光栅器
        WritableRaster raster = mImage.getRaster();
        //获取当前位置的色彩分量
        int[] components = colorModel.getComponents(raster.getDataElements(
                point.x, point.y, null
        ), null, 0);
        //显示弹窗
        JOptionPane.showMessageDialog(
                this,
                "当前色彩模型" + colorModel + "\n" +
                "R:" + components[0] + "\n" +
                "G:" + components[1] + "\n" +
                "B:" + components[2],
                "颜色分量",
                JOptionPane.INFORMATION_MESSAGE,
                new ImageIcon("queen.gif")
        );
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //不实现
        if (SwingUtilities.isLeftMouseButton(e))
        {
            //左鼠标
            leftPoint = e.getPoint();
            System.out.println("leftPoint = " + leftPoint);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //不实现
        if (SwingUtilities.isLeftMouseButton(e))
        {
            //如果左鼠标离开
            //TODO
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //不实现
        setCursor(Cursor.getPredefinedCursor(
                Cursor.HAND_CURSOR
        ));
        shape = new Rectangle2D.Double(e.getX() - 4,e.getY() - 4,8,8);
        repaint(shape.getBounds());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setCursor(null);
        shape = null;
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        shape.setFrame(e.getX() - shape.getWidth() / 2,
                e.getY() - shape.getHeight() / 2,
                shape.getWidth(),
                shape.getHeight());
        if (SwingUtilities.isLeftMouseButton(e))
        {
            //左鼠标拖动
            eastPoint = e.getPoint();
            if (leftPoint != null)
            {
                if (maksShape != null)
                {
                    maksShape.setFrameFromDiagonal(leftPoint,eastPoint);
                }else {
                    maksShape = new Rectangle2D.Double(leftPoint.x,leftPoint.y,
                            eastPoint.x - leftPoint.x,
                            eastPoint.y - leftPoint.y);
                }
                //更新事件
                fireUpdateEvent();
            }
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (shape == null)
            shape = new Rectangle2D.Double(e.getX(),e.getY(),8,8);
        else
            shape.setFrame(e.getX() - shape.getWidth() / 2,
                    e.getY() - shape.getHeight() / 2,
                    shape.getWidth(),
                    shape.getHeight());
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //已知遮罩不为null
        //获取RGB
        //设置布尔变量控制
        ColorModel colorModel = mImage.getColorModel(); //颜色模型
        WritableRaster raster = mImage.getRaster(); //光栅器
        for (y = leftPoint.y;y<eastPoint.y;y++) {
            //行
            for (x = leftPoint.x;x<eastPoint.x;x++) {
                //列
                int[] comp = colorModel.getComponents(
                        raster.getDataElements(x, y, null)
                        , null, 0);
                //过时的老东西
                int RGB = mImage.getRGB(x, y);
                //更新实践;
                fireEvent(comp,RGB);
//              System.out.printf("坐标：[%d,%d]的颜色分量%n" +
//                      "R:%d%n" +
//                      "G:%d%n" +
//                      "B:%d%n",i,j,components[0],
//                      components[1],components[2]);
            }
        }
    }

    public void addMaskPropertyListener(MaskPropertyListener l) {
        listeners.add(l);
    }

    public void removeMaskPropertyListener(MaskPropertyListener l) {
        listeners.remove(l);
    }

    public void fireUpdateEvent() {
        for (MaskPropertyListener l : listeners)
            l.updateMaks(maksShape);
    }

    public void addColorCompomentImplement(ColorCompomentImplement l) {
        colorListeners.add(l);
    }

    public void removeColorCompomentImplement(ColorCompomentImplement l) {
        colorListeners.remove(l);
    }

    public void fireEvent(int[] comp, int RGB) {
        for (ColorCompomentImplement l : colorListeners)
            l.updateColors(x,y,comp,RGB);
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        int type = e.getType();
        if (type == TableModelEvent.UPDATE)
        {
            //如果是更新事件
            int x = e.getFirstRow();
//            mImage.getRaster().setDataElements(0,0,);
            int y = e.getLastRow();
            int column = e.getColumn();
        }
    }

    public BufferedImage getmImage() {
        return mImage;
    }

    public void setmImage(BufferedImage mImage) {
        this.mImage = mImage;
    }


    public RectangularShape getMaksShape() {
        return maksShape;
    }

    public void setMaksShape(RectangularShape maksShape) {
        this.maksShape = maksShape;
        fireUpdateEvent();
    }
}
