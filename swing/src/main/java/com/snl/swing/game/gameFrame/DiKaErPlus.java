package com.snl.swing.game.gameFrame;

import com.snl.swing.game.math.*;
import com.snl.swing.game.math.Polygon;
import com.snl.swing.game.math.geo.curve.CubicCurve;
import com.snl.swing.game.math.geo.curve.Curve;
import com.snl.swing.game.math.geo.curve.QuadCurve;
import com.snl.swing.game.math.geo.Path;
import com.snl.swing.game.utils.AxisPlus;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.List;

public class DiKaErPlus extends SimpleGameFramePlus implements MouseWheelListener {

    //是否拖动坐标系
    protected boolean dragging;
    //鼠标点，第二个变量是每帧鼠标移动的距离
    /* 注意：这两个变量都是以像素为单位 */
    protected Vector2D mousePos,mouseDelta;
    //坐标轴
    protected AxisPlus axis;
    //原点
    Point2D originPoint;
    /* 维护的窗口矩阵 */
    protected Vector2D minS,maxS;
    //是否绘制边框
    protected boolean drawAxis = true;

    //是否开启抗锯齿渲染
    protected boolean showAnti;

    public DiKaErPlus() throws HeadlessException {
        super();
        addMouseWheelListener(this);
    }

    //**********************************************************************//
    /* ******************          游戏初始化         *********************** */
    //**********************************************************************//

    @Override
    protected void gameInitial() {
        super.gameInitial();
        mousePos = new Vector2D();
        mouseDelta = new Vector2D();
        //创建轴
        axis = new AxisPlus();
        axis.createAxis(getViewportTransform(),c,wordWidth);
        //初始化原点
        originPoint = new Point2D.Double();
        //初始化屏幕左下角点
        resetPos();
        //TODO
    }

    protected Vector2D getMousePointInVector() {
        Matrix3x3f r = getReverseWorldTransForm();
        return r.mul(mousePos);
    }

    //**********************************************************************//
    /* ******************          游戏循环         *********************** */
    //**********************************************************************//

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_H))
        {
            viewMat = Matrix3x3f.identity();
            axis.createAxis(getViewportTransform(),c,wordWidth);
        }
        //获取当前坐标点
        Vector2D pos = new Vector2D(mouseInputEvent.getCurrentPoint());
        //获取坐标在当前帧移动的距离
        mouseDelta = pos.sub(mousePos);
        //将当前帧的鼠标坐标复制
        mousePos = pos;
        dragging = mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON2);
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        if (dragging)
        {
            // 像素 → 世界单位
            Matrix3x3f re = getReverseScaleViewPortMat();
            Vector2D v = re.mul(mouseDelta);
            viewMat = Matrix3x3f.translate(-v.getX(),v.getY()).mul(viewMat);
            setCursor(Cursor.getPredefinedCursor(
                    Cursor.HAND_CURSOR));
            axis.createAxis(getViewportTransform(),c,wordWidth);
        }
        else
            setCursor(null);
        //TODO
        resetPos();
        Matrix3x3f r = viewMat.getReverseTranslation();
        minS = r.mul(minS);
        maxS = r.mul(maxS);
    }

    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        if (showAnti)
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        super.draw(g);
        //TODO
        if (drawAxis) {
            axis.draw(g2);
            drawPoint(g2, new Point2D.Double(2, 0));
            drawPoint(g2, new Point2D.Double(0, 2));
            drawPoint(g2, new Point2D.Double(0, -2));
            drawPoint(g2, new Point2D.Double(-2, 0));
            drawPoint(g2, originPoint);
        }
        g2.dispose();
    }

    //**********************************************************************//
    /* ******************          绘制多边形         *********************** */
    //**********************************************************************//

    //绘制点
    protected void drawPoint(Graphics2D g2, Point2D point) {
        g2.setColor(Color.MAGENTA);
        Matrix3x3f mat = getViewportTransform();
        Point2D p = mat.mul(point);
        Shape o = new Ellipse2D.Double(
                p.getX() - 4,p.getY() - 4,
                8,8
        );
        g2.fill(o);
        g2.drawString("[%.2f,%.2f]".formatted(point.getX(),point.getY()),
                (int) p.getX(), (int) (p.getY() - 10));
    }

    //绘制线
    protected void drawLine(Graphics2D g2,Line line) {
        //TODO 这是一个绘制线的请求
        int mode = line.getMode();
        if (mode == Line.XIANSHI) {
            Vector2D pos = line.getPos(); //基点
            drawCircle(g2,pos,.1,true);
            Vector2D moveD = line.getMoveD(); //运动方向
            Vector2D end;
            end = pos.add(moveD);
            drawCircle(g2,end,.1,true);
            end = pos.add(moveD.mul(wordWidth));
            pos = pos.sub(moveD.mul(wordWidth));
            drawLine(g2,pos,end);
        }
        if (mode == Line.YINGSHI) {
            //TODO 隐式
            Vector2D n = line.getN();
            Vector2D pos = line.getPos(); //基点
            Vector2D d = n.prep();
            Vector2D end = pos.add(d);
            drawLine(g2,pos,end);
        }
    }

    //绘制AABB矩形
    protected void drawAAbb(Graphics2D g2, AABB aabb,boolean filling){
        this.drawAABB(g2,aabb.getMin(),aabb.getMax(),filling);
    }

    private void drawAABB(Graphics2D g2, Vector2D min, Vector2D max,boolean fill) {
        Vector2D left = new Vector2D(min.getX(),max.getY());
        Vector2D bottom = new Vector2D(max.getX(), min.getY());

        Matrix3x3f view = getViewportTransform();
        Vector2D leftS = view.mul(left);
        Vector2D bottomS = view.mul(bottom);

        //左上角点
        double sx = leftS.getX();
        double sy = leftS.getY();

        double w = Math.abs(bottomS.getX() - leftS.getX());
        double h = Math.abs(bottomS.getY() - leftS.getY());

        Shape s = new Rectangle2D.Double(sx,sy,w,h);
        if (fill)
            g2.fill(s);
        else
            g2.draw(s);
    }

    protected void drawOrientedRectangle(Graphics2D g2,OrientedRectangle or,boolean filling) {
        Vector2D[] r = new Vector2D[4];
        Vector2D halfExtend = or.getHalfExtend();
        double rot = or.getRot();
        Vector2D center = or.getCenter();
        Vector2D lup = new Vector2D( -halfExtend.getX(),-halfExtend.getY());
        Vector2D rup = new Vector2D( +halfExtend.getX(), -halfExtend.getY());
        Vector2D rbm = new Vector2D( +halfExtend.getX(), +halfExtend.getY());
        Vector2D lbm = new Vector2D( -halfExtend.getX(),+halfExtend.getY());
        r[0] = lup;
        r[1] = rup;
        r[2] = rbm;
        r[3] = lbm;
        Matrix3x3f rotate = Matrix3x3f.rotate(rot);
        for (int i = 0;i<r.length;i++) {
            r[i] = rotate.mul(r[i]).add(center);
        }
        drawPoly(g2,r,filling);
    }

    //绘制圆形
    protected void drawCircle(Graphics2D g2,Vector2D p,double r) {
        this.drawCircle(g2,p,r,false);
    }

    //绘制圆形
    public void drawCircle(Graphics2D g2, Vector2D p, double r, boolean fill) {
        this.drawEllipse(g2,p,r,r,fill);
    }

    protected void drawCircle(Graphics2D g2, Circle circle, boolean filling) {
        this.drawCircle(g2,circle.center.add(circle.offset),circle.r,filling);
    }

    //绘制椭圆
    protected void drawEllipse(Graphics2D g2,Vector2D p,double ra,double rb) {
        this.drawEllipse(g2,p,ra,rb,false);
    }

    protected void drawEllipse(Graphics2D g2,Vector2D p,double ra,double rb,boolean fill) {
        Matrix3x3f vt = getViewportTransform();
        Matrix3x3f scale = getScaleViewPortMat();
        Vector2D c0 = vt.mul(p);
        Vector2D v = new Vector2D(2 * ra,2 * rb);
        v = scale.mul(v);
        //获取坐标
        double w = v.getX();
        double h = v.getY();
        double leftX = c0.getX() - w / 2.0;
        double leftY =c0.getY() - h / 2.0;
        Shape s = new Ellipse2D.Double(leftX,leftY,w,h);
        if (fill) {
            g2.fill(s);
//            g2.drawString("[%.2f,%.2f]".formatted(p.getX(),p.getY()),
//                    (int) c0.getX(), (int) (c0.getY() - 10));
        }
        else
            g2.draw(s);

//         Shape radius = new Ellipse2D.Double(c0.getX(),c0.getY() - 10,4,4);
//         g2.fill(radius);
    }

    //绘制多边形
    protected void drawPolygon(Graphics2D g2, Vector2D[] polygon) {
        drawPoly(g2,polygon,false);
    }

    //绘制多边形，是否是正确的？？？？？
    protected void drawPolyGon(Graphics2D g2,Polygon polygon,boolean filling) {
        drawPoly(g2,polygon.getVertices(),filling);
    }

    protected void  drawPolygon(Graphics2D g2,Vector2D[] polygon,boolean filling) {
        if(polygon == null || polygon.length == 0)
            return;
        if (g2 == null)
            return;
        if (filling)
        {
            GeneralPath path = new GeneralPath();
            Vector2D p;
            Vector2D f = polygon[polygon.length -1];
            path.moveTo(f.getX(),f.getY());
            for (Vector2D v : polygon)
            {
                p = v;
                path.lineTo(p.getX(),p.getY());
            }
            g2.fill(path);
        }
        else
        {
            Vector2D p;
            Vector2D f = polygon[polygon.length -1];
            for (Vector2D v : polygon) {
                p = v;
                Line2D l = new Line2D.Double(
                        f.getX(),f.getY(),
                        p.getX(),p.getY()
                );
                g2.draw(l);
                f = p;
            }
        }
    }

    protected void drawPoly(Graphics2D g2,List<Vector2D> polys,boolean fill){
        this.drawPoly(g2,polys.toArray(Vector2D[]::new),fill);
    }

    public void drawPolyLine(Graphics2D g2, Vector2D[] poly,boolean showVertices) {
        if(poly == null || poly.length == 0)
            return;
        if (g2 == null)
            return;
        Matrix3x3f view = getViewportTransform();
        Vector2D[] copy = new Vector2D[poly.length];
        int i;


        for ( i=0;i<poly.length;i++)
        {
            copy[i] = view.mul(poly[i]);
        }


        if (showVertices)
        {
            TextLayout tl;
            Font font = g2.getFont();
            FontRenderContext frc = g2.getFontRenderContext();
            for (i = 0; i < poly.length; i++)
            {
                this.drawCircle(g2,poly[i],0.05,true);
                this.drawCircle(g2,poly[i],0.1,false);
                tl = new TextLayout("[%.2f,%.2f]".formatted(
                        poly[i].getX(),poly[i].getY()
                ),font,frc);
                tl.draw(g2, (float) copy[i].x, (float) copy[i].y);
            }
        }

        Vector2D start = copy[0];
        for (i = 1; i<copy.length;i++) {
            Vector2D end = copy[i];
            Line2D l = new Line2D.Double(
                    start.getX(),start.getY(),
                    end.getX(),end.getY()
            );
            g2.draw(l);
            start = end;
        }
    }

    public void drawPolyLine(Graphics2D g2, Vector2D[] poly) {
        if(poly == null || poly.length == 0)
            return;
        if (g2 == null)
            return;
        Matrix3x3f view = getViewportTransform();
        Vector2D[] copy = new Vector2D[poly.length];
        for (int i=0;i<poly.length;i++)
        {
            drawCircle(g2,poly[i],0.05,true);
            drawCircle(g2,poly[i],.01,false);
            copy[i] = view.mul(poly[i]);
        }

        Vector2D start = copy[0];
        for (int i = 1; i<copy.length;i++) {
            Vector2D end = copy[i];
            Line2D l = new Line2D.Double(
                    start.getX(),start.getY(),
                    end.getX(),end.getY()
            );
            g2.draw(l);
            start = end;
        }
    }

    public void drawPoly(Graphics2D g2, Vector2D[] poly, boolean filling) {
        if(poly == null || poly.length == 0)
            return;
        if (g2 == null)
            return;
        Matrix3x3f view = getViewportTransform();
        Vector2D[] copy = new Vector2D[poly.length];
        for (int i=0;i<poly.length;i++)
        {
            copy[i] = view.mul(poly[i]);
        }

        if (filling)
        {
            GeneralPath path = new GeneralPath();
            Vector2D f = copy[copy.length -1];
            path.moveTo(f.getX(),f.getY());
            for (Vector2D v : copy)
            {
                f = v;
                path.lineTo(f.getX(),f.getY());
            }
            path.closePath();
            g2.fill(path);
        }
        else
        {
            Vector2D p;
            Vector2D f = copy[copy.length -1];
            for (Vector2D v : copy) {
                p = v;
                Line2D l = new Line2D.Double(
                        f.getX(),f.getY(),
                        p.getX(),p.getY()
                );
                g2.draw(l);
                f = p;
            }
        }

    }

    //绘制多边形2-该方法接受一个坐标列表
    protected void drawPolygon(Graphics2D g2, List<Vector2D> polygon) {
        this.drawPoly(g2,polygon.toArray(Vector2D[]::new),false);
    }

    //绘制文本
    public void drawText(Graphics2D g2,TextLayout layout,Vector2D p) {
        Matrix3x3f vt = getViewportTransform();
        Vector2D c0 = vt.mul(p);
        //左上角
        Rectangle2D bounds = layout.getBounds();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        float leftX = (float) (c0.getX() - width / 2.0);
        float leftY = (float) (c0.getY() - height / 2.0);
        layout.draw(g2,leftX,leftY);
    }

    //绘制形状
    protected void drawShape(Graphics2D g2, Shape shape, Vector2D p) {
        Matrix3x3f vt = getViewportTransform();
        Vector2D c0 = vt.mul(p);
        //左上角
        Rectangle2D bounds = shape.getBounds();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        float leftX = (float) (c0.getX() - width / 2.0);
        float leftY = (float) (c0.getY() - height / 2.0);
        g2.fill(shape);
    }

    //绘制图像
    protected void drawImage(Graphics2D g2,Image image,Vector2D p)
    {
        Matrix3x3f vt = getViewportTransform();
        Vector2D c0 = vt.mul(p);
        //获取缩放
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        //获取坐标
        double leftX = c0.getX() - w / 2.0;
        double leftY = c0.getY() - h / 2.0;
        g2.drawImage(image, (int) leftX, (int) leftY,null);
    }

    public void drawLine(Graphics2D g2,Vector2D start,Vector2D end)
    {
        Matrix3x3f vt = getViewportTransform();
        Vector2D v1 = vt.mul(start);
        Vector2D v2 = vt.mul(end);
        Shape l = new Line2D.Double(v1.getX(),v1.getY(),v2.getX(),v2.getY());
        g2.draw(l);
        drawCircle(g2,start,0.05,true);
        drawCircle(g2,end,0.05,true);
    }

    protected void drawText(Graphics2D g2,float leftx,float lefty,double w,TextLayout tl) {
        float advance = tl.getAdvance();
        if (w < advance)
            w = advance;
        float lx = (float) (leftx + (w - advance) / 2.0F);
        tl.draw(g2,lx,lefty + tl.getAscent());
    }

    protected void drawConvexity(Graphics2D g2,Convexity convexity,boolean filling) {
        Vector2D[] vertices = convexity.getVertices();
        drawPoly(g2,vertices,filling);
        //
        if (convexity.isShowVer())
        {
            TextLayout tl;
            Font font = g2.getFont();
            FontRenderContext frc = g2.getFontRenderContext();
            for (Vector2D v : vertices)
            {
                this.drawCircle(g2,v,0.05,true);
                tl = new TextLayout("[%.2f,%.2f]".formatted(
                        v.getX(),v.getY()
                ),font,frc);
                this.drawText(g2,tl,v);
            }
        }
    }

    protected <T extends Curve> void drawCurve(Graphics2D g2,T t) {
        Matrix3x3f vt = getViewportTransform();
        GeneralPath path = new GeneralPath();
        if (t instanceof QuadCurve arc)
        {
            Vector2D startPoint = arc.getStartPoint();
            startPoint = vt.mul(startPoint);
            path.moveTo(startPoint.x,startPoint.y);

            Vector2D controlPoint = arc.getControlPoint01();
            controlPoint = vt.mul(controlPoint);

            Vector2D endPoint = arc.getEndPoint();
            endPoint = vt.mul(endPoint);

            path.quadTo(controlPoint.x,controlPoint.y,endPoint.x,endPoint.y);
        } else if (t instanceof CubicCurve cc) {
            Vector2D startPoint = cc.getStartPoint();
            startPoint = vt.mul(startPoint);
            path.moveTo(startPoint.x,startPoint.y);

            Vector2D controlPoint = cc.getControlPoint01();
            controlPoint = vt.mul(controlPoint);

            Vector2D controlPoint2 = cc.getControlPoint2();
            controlPoint2 = vt.mul(controlPoint2);

            Vector2D endPoint = cc.getEndPoint();
            endPoint = vt.mul(endPoint);

            path.curveTo(controlPoint.x,controlPoint.y,controlPoint2.x,controlPoint2.y,
                    endPoint.x,endPoint.y);
        }

        g2.fill(path);
    }


    public void drawPath(Graphics2D g2, Path path) {
        Matrix3x3f vt = getViewportTransform();
        g2.fill(path.getGp(vt));
    }

    //**********************************************************************//
    /* ******************          重置状态         *********************** */
    //**********************************************************************//

    @Override
    protected void reset() {
        super.reset();
        mouseDelta = new Vector2D();
        axis.createAxis(getViewportTransform(),c,wordWidth);
        resetPos();
        //TODO
    }

    private void resetPos() {
        minS = new Vector2D(-wordWidth / 2.0,-wordHeight / 2.0);
        maxS = new Vector2D(wordWidth / 2.0,wordHeight / 2.0);
    }

    //重置视图
    protected void resetView() {
        viewMat = Matrix3x3f.identity();
    }

    //**********************************************************************//
    /* ******************          重置窗口         *********************** */
    //**********************************************************************//

    @Override
    protected void handleResizeEvent(ComponentEvent e) {
        super.handleResizeEvent(e);
        axis.createAxis(getViewportTransform(),c,wordWidth);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int wheelRotation = e.getWheelRotation();
        if (wheelRotation == -1)
        {
            wordWidth--;
            wordHeight--;
            axis.createAxis(getViewportTransform(),c,wordWidth);
        }

        if (wheelRotation == 1) {
            wordWidth++;
            wordHeight++;
            axis.createAxis(getViewportTransform(),c,wordWidth);
        }

        if (wordWidth <= 2)
            wordWidth = wordHeight = 2;

        if (wordWidth == WIDTH || wordHeight == HEIGHT)
        {
            wordWidth = WIDTH;
            wordHeight = HEIGHT;
        }
        scaleX = WIDTH / wordWidth;
        scaleY = HEIGHT / wordHeight;
    }
    //**********************************************************************//
    /* ******************          碰撞测试         *********************** */
    //**********************************************************************//


    protected boolean pointInCircle(Vector2D pos,Circle circle) {
        return this.pointInCircle(pos,circle.center.add(circle.offset),circle.r);
    }

    protected boolean pointInCircle(Vector2D pos,Vector2D c,double r)
    {
        Vector2D v = pos.sub(c);
        return v.lenSqr() < Math.pow(r,2);
    }

    protected boolean pointInAABB(Vector2D pos,Vector2D min,Vector2D max)
    {
        return pos.getX() >= min.getX() && pos.getX() <= max.getX()
                && pos.getY() >= min.getY() && pos.getY() <= max.getY();
    }

    protected boolean pointInEllipse(Vector2D pos,Vector2D c,double ra,double rb)
    {
        double x = c.getX() / ra;
        double y = c.getY() / rb;
        Vector2D v = new Vector2D(x,y);
        double x1 = pos.getX() / ra;
        double y1 = pos.getY() / rb;
        Vector2D v1 = new Vector2D(x1,y1);
        v1 = v1.sub(v);
        return v1.lenSqr() < 1;
    }

    protected boolean pointInPoly(Vector2D pos,List<Vector2D> poly) {
        return this.pointInPoly(pos,poly.toArray(Vector2D[]::new));
    }

    protected <T extends Polygon> boolean pointInPoly(Vector2D pos, T t) {
        return t.containsPoint(pos);
    }

    protected boolean pointInPoly(Vector2D pos,Vector2D[] poly)
    {
        if (poly == null || poly.length <= 2)
            return false;
        int inside = 0;
        Vector2D s = poly[poly.length - 1];
        boolean start = pos.getY() > s.getY();
        for (Vector2D e : poly) {
            boolean end = pos.getY() > e.getY();
            if (start != end) {
                //计算
                double k = (e.getY() - s.getY()) / (e.getX() - s.getY());
                double insertX = s.getX() + (pos.getY() - s.getY()) / k;
                if (insertX > pos.getY())
                    inside++;
            }
            start = end;
            s = e;
        }
        return inside % 2 != 0;
    }

    protected boolean lineInsertLine(Vector2D start01,Vector2D end01,
                                     Vector2D start02,Vector2D end02)
    {
        //使用二维增广矩阵
        //判断交点
        double dy01 = end01.getY() - start01.getY();
        double dx01 = end01.getX() - start01.getX();

        double dy02 = end02.getY() - start02.getY();
        double dx02 = end02.getX() - start02.getX();

        double d = (-dy01 * dx02) - (dx01 * -dy02);
        return d != 0;
    }

    protected Vector2D lineInsertionPos(Vector2D start01,Vector2D end01,
                                      Vector2D start02,Vector2D end02)
    {
        boolean inserts = this.lineInsertLine(start01, end01, start02, end02);
        if (inserts)
        {
            //使用克莱姆法则计算

        }
        //无交点
        return null;
    }

    protected boolean pointInOrientedRectangle(Vector2D pos, OrientedRectangle r) {
        Vector2D center = r.getCenter();
        Vector2D min = new Vector2D();
        Vector2D max = r.getHalfExtend().mul(2);
        double rot = r.getRot();
        Matrix3x3f rotate = Matrix3x3f.rotate(-rot);
        Vector2D lp = pos.sub(center);
        lp = rotate.mul(lp);
        lp = lp.add(r.getHalfExtend());
        return pointInAABB(lp,min,max);
    }

    //**********************************************************************//
    /* ******************          图像测试         *********************** */
    //**********************************************************************//

    //创建测试图像
    protected BufferedImage createBufferedImage(int W,int H) {
        return this.createBufferedImage(W,H,BufferedImage.TYPE_INT_RGB);
    }

    protected BufferedImage createBufferedImage(int W,int H,int transparent) {
        if(transparent < BufferedImage.TYPE_CUSTOM || transparent > BufferedImage.TYPE_BYTE_INDEXED)
            throw new IllegalArgumentException("非法参数异常");
        var gc = getGc();
        return gc.createCompatibleImage(W,H,transparent);
    }

    protected void drawSplitImage(Graphics2D g2, double splitX, Image west, Image east) {
        int height = c.getHeight();
        int width = c.getWidth();
        if (splitX != 0 && west != null)
        {
            Rectangle2D clip = new Rectangle2D.Double(
                    0,0, splitX,height
            );
            g2.setClip(clip);
        }
        g2.drawImage(west,0,0,null);
        if (splitX == 0 || east == null) return;
        Rectangle2D secondClip = new Rectangle2D.Double(splitX, 0, width, height);
        g2.setClip(secondClip);
        g2.drawImage(east,0,0,null);
        g2.setClip(null);
        Line2D l = new Line2D.Double(splitX,0, splitX,height);
        g2.setColor(Color.lightGray);
        g2.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER,1,
                new float[]{3,5,3},2));
        g2.draw(l);
    }

    protected VolatileImage createViolateImage(int w, int h) {
       return this.createViolateImage(w,h,Transparency.OPAQUE);
    }

    protected  VolatileImage createViolateImage(int w,int h,int transparent) {
        if (transparent < Transparency.OPAQUE || transparent > Transparency.TRANSLUCENT)
            throw new IllegalArgumentException("非法参数异常");
       var gc =  getGc();
       return gc.createCompatibleVolatileImage(w,h,transparent);
    }

    private GraphicsConfiguration getGc() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice sd = ge.getDefaultScreenDevice();
        return sd.getDefaultConfiguration();
    };

}
