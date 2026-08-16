//package com.snl.swing.game.test;
//
//import com.snl.swing.game.gameFrame.DiKaErPlus;
//import com.snl.swing.game.math.*;
//
//import java.awt.*;
//import java.awt.event.MouseEvent;
//import java.util.ArrayList;
//import java.util.List;
//
//public class TestTriangle extends DiKaErPlus {
//    Triangle triangle;
//    boolean moving,clicked,drag,insert;
//
//    Triangle triangle02;
//    Vector2D N;
//    double c;
//    Vector2D Q,C;
//
//    Line line;
//    Vector2D rp;
//    Triangle triangle03,triangle04;
//    AABB treeStem;
//
//    Tree tr,tr10,tr30,tr50,tr70,sc;
//
//    Circle circle,out;
//
//    public TestTriangle() throws HeadlessException {
//        wordWidth = wordHeight = 9;
//    }
//
//    @Override
//    protected void gameInitial() {
//        super.gameInitial();
//        triangle = new Triangle(
//                new Vector2D(0,0),new Vector2D(0,3),new Vector2D(4,0)
//        );
//
//
//
//        triangle02 = new Triangle(
//                new Vector2D(-2,2),new Vector2D(0,4),new Vector2D(2,2)
//        );
//
//
//
//        triangle03 = new Triangle(
//                new Vector2D(-0.25,-0.25),new Vector2D(0,0),new Vector2D(0.25,-0.25)
//        );
//
//        triangle04 = new Triangle(
//                new Vector2D(-0.25,-.5),new Vector2D(0,-0.25),new Vector2D(0.25,-.5)
//        );
//
//        treeStem = new AABB(
//                new Vector2D(-0.05,-.75),new Vector2D(0.05,-.5)
//        );
//
//        triangle.setShowVer(true);
//        //垂线
//        triangle02.setShowVer(true);
//        Vector2D[] vertices = triangle.getVertices();
//        Vector2D v1 = vertices[0];
//        Vector2D v2 = vertices[1];
//        Vector2D v3 = vertices[2];
//        //获取p2、p3线段
//        N = v3.sub(v2).norm().prep();
//        c = -N.dot(v2);
//        /* l : N * p + c = 0(其中p是直线上任意点)  */
//        //设置垂足为Q点
//        double t = N.dot(v1) + c;
//        Q = v1.sub(N.scale(t));
//
//        //寻找v1,v3的中心点
//        Vector2D d = v3.sub(v1);
//        double len = d.len();
//        d = d.norm();
//        C = v1.add(d.scale(len / 2.0));
//
//        //获取中垂线
//        Vector2D prep = v3.sub(v1).prep();
//        line = new Line(
//                C,prep,Line.XIANSHI
//        );
//        double area = triangle.getArea();
//        System.out.println("area = " + area);
//        //
//        tr = new Tree(treeStem,triangle03,triangle04);
//        tr10 = tr.getTranslated(new Vector2D(2,2));
//        tr30 = tr.getRotated(Math.PI / 2,0,-2);
//        tr50 = tr.getRotated(-Math.PI / 2,0,-2);
//        tr70 = tr.getRotated(Math.PI,0,-2);
//
//        sc = tr.getScaled(3,1.2);
//    }
//
//    @Override
//    protected void processInput(double delta) {
//        super.processInput(delta);
//        clicked = mouseInputEvent.mouseButtonDownOnce(MouseEvent.BUTTON1);
//        drag = mouseInputEvent.mouseButtonDown(MouseEvent.BUTTON1);
//    }
//
//    @Override
//    protected void updateSprite(double delta) {
//        super.updateSprite(delta);
//        Vector2D mouse = getMousePointInVector();
//        if (clicked && triangle.containsPoint(mouse))
//            moving = true;
//
//        moving = moving && drag;
//        if (moving) {
//            Matrix3x3f re = getReverseScaleViewPortMat();
//            Vector2D v = re.mul(mouseDelta);
//            triangle.move(v);
//        }
//        Vector2D[] vertices = triangle.getVertices();
//        Vector2D v1 = vertices[0];
//        Vector2D v2 = vertices[1];
//        Vector2D v3 = vertices[2];
//        //获取p2、p3线段
//        N = v3.sub(v2).norm().prep();
//        c = -N.dot(v2);
//        /* l : N * p + c = 0(其中p是直线上任意点)  */
//        //设置垂足为Q点
//        double t = N.dot(v1) + c;
//        Q = v1.sub(N.scale(t));
//        insert = triangle.collideOtherConvexity(triangle02);
//
//        //寻找v1,v3的中心点
//        Vector2D d = v3.sub(v1);
//        double len = d.len();
//        d = d.norm();
//        C = v1.add(d.scale(len / 2.0));
//
//        Vector2D prep = v3.sub(v1).prep();
//        line = new Line(
//                C,prep,Line.XIANSHI
//        );
//        rp = triangle.pickedRandomPoint(0,1);
//        circle = triangle.getInsideCircle();
//        out = triangle.getOutSideCircle();
//    }
//
//    @Override
//    protected void draw(Graphics g) {
//        Graphics2D g2 = (Graphics2D) g.create();
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                RenderingHints.VALUE_ANTIALIAS_ON);
//        super.draw(g);
//        if (insert)
//            g2.setColor(Color.red);
//        else
//            g2.setColor(Color.green);
//        drawConvexity(g2,triangle,false);
//        drawConvexity(g2,triangle02,false);
////        Circle insideCircle = triangle.getInsideCircle();
////        drawCircle(g2,insideCircle.center,insideCircle.r,true);
//        drawLine(g2,triangle.getVertices()[0],Q);
//        drawLine(g2,line);
//        drawCircle(g2,Q,.1,true);
//        drawCircle(g2,C,.1,true);
//        drawLine(g2,triangle.getVertices()[1],C );
//        drawCircle(g2,rp,.2,true);
//        drawCircle(g2,circle,false);
//        drawTree(g2,tr);
//        drawTree(g2, tr10);
//        drawTree(g2,tr30);
//        drawTree(g2,tr50);
//        drawTree(g2,tr70);
//        drawTree(g2,sc);
//        drawCircle(g2,out,false);
//        g2.dispose();
//    }
//
//
//    private void drawTree(Graphics2D g2, Tree tr) {
//        for (Convexity shape : tr.getShapes()) {
//            drawConvexity(g2,shape,true);
//        }
//
//    }
//
//    @Override
//    protected void reset() {
//        super.reset();
//        triangle.reset();
//    }
//
//    public static void main(String[] args) {
//        launchGame(new TestTriangle());
//    }
//
//    class Tree {
//        Convexity aabb;
//        Convexity triangle,triangle02;
//
//        public Tree(Convexity aabb, Convexity triangle, Convexity triangle02) {
//            this.aabb = aabb;
//            this.triangle = triangle;
//            this.triangle02 = triangle02;
//        }
//
//        public Convexity[] getShapes() {
//            List<Convexity> shapes = new ArrayList<>();
//            shapes.add(aabb);
//            shapes.add(triangle);
//            shapes.add(triangle02);
//            return shapes.toArray(Convexity[]::new);
//        }
//
//        public Tree getTranslated(Vector2D v) {
//            Convexity translated = aabb.getTranslated(v);
//            Convexity translated1 = triangle.getTranslated(v);
//            Convexity translated2 = triangle02.getTranslated(v);
//            return new Tree(
//                    translated,translated1,translated2
//            );
//        }
//
//        public Tree getRotated(double rot,double x,double y) {
//            Convexity rotateInstance = aabb.getRotateInstance(rot,x,y);
//            Convexity rotateInstance1 = triangle.getRotateInstance(rot,x,y);
//            Convexity rotateInstance2 = triangle02.getRotateInstance(rot,x,y);
//            return new Tree(
//                    rotateInstance,rotateInstance1,rotateInstance2
//            );
//        }
//
//        public Tree getScaled(double sx,double sy) {
//            Convexity scaled = aabb.getScaled(sx, sy);
//            Convexity scaled1 = triangle.getScaled(sx, sy);
//            Convexity scaled2 = triangle02.getScaled(sx, sy);
//            return new Tree(
//                    scaled,scaled1,scaled2
//            );
//        }
//    }
//}
