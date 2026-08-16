//package com.snl.swing.tank;
//
//import com.snl.swing.game.gameFrame.DiKaErPlus;
//import com.snl.swing.game.math.Matrix3x3f;
//import com.snl.swing.game.math.Vector2D;
//import com.snl.swing.game.utils.Generator;
//import com.snl.swing.game2026.dataStructure.Array;
//import org.jdesktop.animation.timing.Animator;
//import org.jdesktop.animation.timing.TimingTarget;
//
//import java.awt.*;
//
//public class Tank implements TimingTarget {
//
//    Base base;
//    PaoTai pt;
//    Gun gun;
//    Wheel wheel;
//
//    //一些字段变量
//    double baseRot,baseTheta,lastBaseRot;
//    double bodyRot,bodyTheta,lastBodyRot;
//    double wheelRot,wheelTheta,lastWheelRot;
//    double moveSpeed;
//
//    double scaleX,scaleY,lastScalex,lastScaleY;
//    double bulletSpeed;
//
//    boolean firing,forwarding,backwarding;
//
//    Animator shootingAnimator;
//
//    Bullet bullet;
//    Array<Article> articles;
//    boolean leftTurning,rightTurning;
//    double dx,dx02,initial,initial02;
//
//
//    public Tank() {
//        base = new Base();
//        pt = new PaoTai(1);
//        gun = new Gun();
//        wheel = new Wheel();
//
//        //初始化变量
//        lastBaseRot = baseRot = bodyRot = 0;
//        baseTheta = bodyTheta =  Math.PI / 4;
//        wheelRot = 0;
//        wheelTheta = Math.PI / 4;
//        moveSpeed = 1.25;
//
//        scaleX = scaleY = 1;
//
//        bulletSpeed = 3.0;
//
//        //创建关系
//        base.parentForm = Matrix3x3f.identity();
//        createRelations();
//
//        //初始化粒子
//        articles = new Array<>(50);
//        dx = 0;
//        initial = 0.2;
//        initial02 = -0.2;
//    }
//
//    private void createRelations() {
//        createBodyToBaseRelation();
//        createBodyToGunRelation();
//        wheel.parentForm = base.modelToWorld().mul(Matrix3x3f.translate(1,0.8));
//    }
//
//    private void createBodyToGunRelation() {
//        gun.parentForm = pt.modelToWorld();
//    }
//
//    private void createBodyToBaseRelation() {
//        pt.parentForm = base.transFrom.mul(base.scaleForm);
//    }
//
//
//    public void rotateBaseClockWise(double delta) {
//        lastBaseRot = baseRot;
//        baseRot += delta * baseTheta;
//    }
//
//    public void  rotateBaseOnClock(double delta) {
//        lastBaseRot = baseRot;
//        baseRot -= delta * baseTheta;
//    }
//
//    public void  rotatePtClockWise(double delta) {
//        lastBodyRot = bodyRot;
//        bodyRot += delta * bodyTheta;
//    }
//
//    public void  rotatePtOnClock(double delta) {
//        lastBodyRot = bodyRot;
//        bodyRot -= delta * bodyTheta;
//    }
//
//    public void  rotateWheelClockWise() {
//        lastWheelRot = wheelRot;
//        wheelRot = wheelTheta;
//    }
//
//    public void  rotateWheelClock() {
//        lastWheelRot = wheelRot;
//        wheelRot = -wheelTheta;
//    }
//
//    public void forward(double delta) {
//        double d = delta * moveSpeed;
//        Vector2D moved = base.rotateForm.mul(new Vector2D(d, 0));
//        Vector2D c2 = base.transFrom.getColumn(2);
//        c2 = c2.add(moved);
//        base.transFrom.setColumn(2,c2);
//
//        dx += delta * 0.5;
//        dx02 += delta * 0.5;
//    }
//
//    public void backward(double delta) {
//        double d = delta * moveSpeed;
//        Vector2D moved = base.rotateForm.mul(new Vector2D(-d, 0));
//        Vector2D c2 = base.transFrom.getColumn(2);
//        c2 = c2.add(moved);
//        base.transFrom.setColumn(2,c2);
//
//        dx -= delta * 0.5;
//        dx02 -= delta * 0.5;
//    }
//
//    public void update(double delta) {
//        if (lastBaseRot != baseRot) {
//            //更新
//            base.rotateForm = Matrix3x3f.rotate(lastBaseRot);
//        }
//
//        if (lastBodyRot != bodyRot) {
//            pt.rotateForm = Matrix3x3f.rotate(lastBodyRot);
//        }
//
//        if ((lastScaleY != scaleY) || (lastScalex != scaleX)) {
//            base.scaleForm = Matrix3x3f.scale(scaleX,scaleY);
//        }
//        wheel.rotateForm = Matrix3x3f.rotate(wheelRot);
//        wheel.wheelPattern.parentForm = wheel.modelToWorld().mul(Matrix3x3f.translate(0.2,0));
//        forwarding = forwarding && !backwarding;
//        if (forwarding)
//            forward(delta);
//        if (backwarding)
//            backward(delta);
//
//        createRelations();
//        Matrix3x3f gunToWorld = gun.modelToWorld();
//
//        if (firing) {
//            createBullet(gunToWorld);
//            fillArticles(gunToWorld);
//            showAnimator();
//        }
//        if (bullet != null)
//            bullet.update(delta);
//
//        if (articles != null && !articles.isEmpty())  {
//            for (Article a : articles) {
//                if (a.isDead())
//                {
//                    articles.remove(a,true);
//                    continue;
//                }
//                a.update(delta);
//            }
//        }
//
//    }
//
//    private void fillArticles(Matrix3x3f parentTransfoem) {
//        Article article;
//        for (int i = 0;i < 1000; i ++) {
//            //TODO
//            Matrix3x3f articleTransform = parentTransfoem.mul(Matrix3x3f.translate(
//                    Generator.generateDouble(-0.25,0.25), Generator.generateDouble(0.75,1)));
//            Vector2D position = articleTransform.mul(Vector2D.originPoint);
//            Vector2D direction = Matrix3x3f.rotate(Generator.generateDouble(- Math.PI / 4,Math.PI / 4)).mul(
//                    articleTransform.getColumn(1)
//            );
//            article = new Article(position,direction,Color.cyan,4.5);
//            article.alpha = Generator.generateFloat(0.0f,1.0f);
//            article.speed = Generator.generateDouble(1,3);
//            articles.add(article);
//        }
//    }
//
//    private void showAnimator() {
//        if (shootingAnimator == null)
//            createAnimator();
//        if (shootingAnimator.isRunning())
//        {
//            shootingAnimator.stop();
//        }
//        shootingAnimator.start();
//    }
//
//    private void createAnimator() {
//        shootingAnimator = new Animator(200,this);
//        shootingAnimator.setRepeatBehavior(Animator.RepeatBehavior.REVERSE);
//        shootingAnimator.setEndBehavior(Animator.EndBehavior.HOLD);
//        shootingAnimator.setRepeatCount(1);
//    }
//
//    private void createBullet(Matrix3x3f parentTransForm) {
//        //炮口世界坐标
//         Vector2D position =
//                parentTransForm.mul(new Vector2D(0,1));
//        //炮管方向
//        Vector2D end =
//                parentTransForm.mul(new Vector2D(0,1));
//
//        Vector2D start =
//                parentTransForm.mul(new Vector2D(0,0));
//
//        Vector2D direction =
//                end.sub(start).norm();
//
//        bullet = Bullet.createBullet(position,direction,bulletSpeed);
//    }
//
//    public void setForwarding(boolean forwarding) {
//        this.forwarding = forwarding;
//    }
//
//    public void setBackwarding(boolean backwarding) {
//        this.backwarding = backwarding;
//    }
//
//    public void setFiring(boolean firing) {
//        this.firing = firing;
//    }
//
//    public boolean isForwarding() {
//        return forwarding;
//    }
//
//    public boolean isBackwarding() {
//        return backwarding;
//    }
//
//    public boolean isFiring() {
//        return firing;
//    }
//
//    public void scale(double delta) {
//        lastScalex = scaleX;
//        lastScaleY = scaleY;
//        scaleX += delta;
//        scaleY += delta;
//
//    }
//
//    public void  deScale(double delta) {
//        lastScalex = scaleX;
//        lastScaleY = scaleY;
//        scaleY -= delta;
//        scaleX -= delta;
//    }
//
//    public void  draw(Graphics2D g2, DiKaErPlus gameFrame) {
//        Vector2D[] temp = new Vector2D[base.outlines.length];
//        for (int  i = 0;i<temp.length; i++) {
//            temp[i] = base.modelToWorld().mul(base.outlines[i]);
//        }
//        gameFrame.drawPoly(g2,temp,true);
//
//        Color color = g2.getColor();
//        g2.setColor(Color.green);
//        gameFrame.drawCircle(g2,pt.modelToWorld().mul(Vector2D.originPoint),pt.r  * scaleX,true);
//
//
//        g2.setColor(Color.PINK);
//        for (int  i = 0;i<temp.length;i++) {
//            Vector2D v = gun.outlines[i];
//            temp[i] = gun.modelToWorld().mul(v);
//        }
//        gameFrame.drawPoly(g2,temp, true);
//
//
//        for (int  i = 0;i<temp.length;i++) {
//            Vector2D v = wheel.outlines[i];
//            temp[i] = wheel.modelToWorld().mul(v);
//        }
//        g2.setColor(Color.gray);
//        gameFrame.drawPoly(g2,temp, true);
//
//        //镜像
//        Matrix3x3f baseToWorld = base.modelToWorld();
//        Vector2D e1 = baseToWorld.getColumn(0);
//        Vector2D e2 = baseToWorld.getColumn(1);
//        Vector2D center = baseToWorld.mul(Vector2D.originPoint);
//        Vector2D wheelCenter = wheel.modelToWorld().mul(Vector2D.originPoint);
//        Vector2D cToWheel = wheelCenter.sub(center);
//        double d =  cToWheel.dot(e2);
//        double ax =  Math.sqrt(cToWheel.lenSqr() - d * d);
//        Vector2D distance2 = e2.scale(-2 * d);
//        Vector2D distance1 = e1.scale(-2 * ax);
//        Vector2D[] t = new Vector2D[temp.length];
//        for (int i = 0;i < temp.length ; i ++) {
//            t[i] = temp[i].add(distance2);
//        }
//
//
//        gameFrame.drawPoly(g2,t,true);
//        for (int i = 0;i < temp.length ; i ++) {
//            t[i] = temp[i].add(distance1);
//        }
//
//        gameFrame.drawPoly(g2,t,true);
//
//        for (int i = 0;i < temp.length ; i ++) {
//            t[i] = temp[i].add(distance1.add(distance2));
//        }
//
//        gameFrame.drawPoly(g2,t,true);
//
//
//
//        double newPotion = initial + dx;
//        if (newPotion >= 0.45 - 0.1) {
//            //复制
//            double extent = newPotion + 0.1 - 0.45;
//            if (extent >= 0.2) {
//                initial = -0.35;
//                dx = 0;
//            }else {
//                Vector2D[ ] wheelPatternCopy = new Vector2D[wheel.wheelPattern.outlines.length];
//                double halfExtent = extent / 2;
//                wheelPatternCopy[0] = new Vector2D(-halfExtent,-0.25);
//                wheelPatternCopy[1] = new Vector2D(halfExtent,-0.25);
//                wheelPatternCopy[2] = new Vector2D(halfExtent,0.25);
//                wheelPatternCopy[3] = new Vector2D(-halfExtent,0.25);
//                wheel.wheelPattern.parentForm = wheel.modelToWorld().mul(Matrix3x3f.translate(-0.45 + halfExtent,0));
//                for (int i = 0;i < temp.length ; i ++) {
//                    temp[i] = wheel.wheelPattern.modelToWorld().mul(
//                            wheelPatternCopy[i]
//                    );
//                }
//
//                g2.setColor(Color.WHITE);
//                gameFrame.drawPoly(g2,temp,true);
//
//                //TODO
//                halfExtent = (.2 - extent) / 2;
//                wheelPatternCopy[0] = new Vector2D(-halfExtent,-0.25);
//                wheelPatternCopy[1] = new Vector2D(halfExtent,-0.25);
//                wheelPatternCopy[2] = new Vector2D(halfExtent,0.25);
//                wheelPatternCopy[3] = new Vector2D(-halfExtent,0.25);
//                wheel.wheelPattern.parentForm = wheel.modelToWorld().mul(Matrix3x3f.translate(0.45 - halfExtent,0));
//                for (int i = 0;i < temp.length ; i ++) {
//                    temp[i] = wheel.wheelPattern.modelToWorld().mul(
//                            wheelPatternCopy[i]
//                    );
//                }
//
//                g2.setColor(Color.WHITE);
//                gameFrame.drawPoly(g2,temp,true);
//            }
//        }else {
//
//            wheel.wheelPattern.parentForm = wheel.modelToWorld().mul(Matrix3x3f.translate(initial + dx, 0));
//            for (int i = 0; i < temp.length; i++) {
//                temp[i] = wheel.wheelPattern.modelToWorld().mul(
//                        wheel.wheelPattern.outlines[i]
//                );
//            }
//
//            g2.setColor(Color.WHITE);
//            gameFrame.drawPoly(g2, temp, true);
//
//        }
//
//
//        newPotion = initial02 + dx02;
//        if (newPotion >= 0.45 - 0.1) {
//            //复制
//            double extent = newPotion + 0.1 - 0.45;
//            if (extent >= .2) {
//                initial02 = -0.35;
//                dx02 = 0;
//            }else {
//                Vector2D[ ] wheelPatternCopy = new Vector2D[wheel.wheelPattern.outlines.length];
//                double halfExtent = extent / 2;
//                wheelPatternCopy[0] = new Vector2D(-halfExtent,-0.25);
//                wheelPatternCopy[1] = new Vector2D(halfExtent,-0.25);
//                wheelPatternCopy[2] = new Vector2D(halfExtent,0.25);
//                wheelPatternCopy[3] = new Vector2D(-halfExtent,0.25);
//                wheel.wheelPattern.parentForm = wheel.modelToWorld().mul(Matrix3x3f.translate(-0.45 + halfExtent,0));
//                for (int i = 0;i < temp.length ; i ++) {
//                    temp[i] = wheel.wheelPattern.modelToWorld().mul(
//                            wheelPatternCopy[i]
//                    );
//                }
//
//                g2.setColor(Color.WHITE);
//                gameFrame.drawPoly(g2,temp,true);
//
//                //TODO
//                halfExtent = (.2 - extent) / 2;
//                wheelPatternCopy[0] = new Vector2D(-halfExtent,-0.25);
//                wheelPatternCopy[1] = new Vector2D(halfExtent,-0.25);
//                wheelPatternCopy[2] = new Vector2D(halfExtent,0.25);
//                wheelPatternCopy[3] = new Vector2D(-halfExtent,0.25);
//                wheel.wheelPattern.parentForm = wheel.modelToWorld().mul(Matrix3x3f.translate(0.45 - halfExtent,0));
//                for (int i = 0;i < temp.length ; i ++) {
//                    temp[i] = wheel.wheelPattern.modelToWorld().mul(
//                            wheelPatternCopy[i]
//                    );
//                }
//
//                g2.setColor(Color.WHITE);
//                gameFrame.drawPoly(g2,temp,true);
//            }
//        }
//
//        else {
//
//            wheel.wheelPattern.parentForm = wheel.modelToWorld().mul(Matrix3x3f.translate(initial02 + dx02, 0));
//            for (int i = 0; i < temp.length; i++) {
//                temp[i] = wheel.wheelPattern.modelToWorld().mul(
//                        wheel.wheelPattern.outlines[i]
//                );
//            }
//            gameFrame.drawPoly(g2, temp, true);
//        }
//
//        if (bullet != null) {
//            gameFrame.drawCircle(g2, bullet.position, 0.1, true);
//        }
//
//        g2.setColor(Color.red);
//        if (articles != null && !articles.isEmpty())
//        {
//            for (Article a : articles) {
//                a.draw(g2,gameFrame);
//            }
//        }
//    }
//
//    @Override
//    public void timingEvent(float v) {
//        for (int i = 0;i<1000;i++) {
//            Article article = articles.get(i);
//            article.alpha = 1 - v;
//            if (article.alpha <= 0)
//                article.setDead(true);
//        }
//    }
//
//    @Override
//    public void begin() {
//
//    }
//
//    @Override
//    public void end() {
//
//    }
//
//    @Override
//    public void repeat() {
//
//    }
//
//
//    public boolean isLeftTurning() {
//        return leftTurning;
//    }
//
//    public void setLeftTurning(boolean leftTurning) {
//        this.leftTurning = leftTurning;
//    }
//
//    public boolean isRightTurning() {
//        return rightTurning;
//    }
//
//    public void setRightTurning(boolean rightTurning) {
//        this.rightTurning = rightTurning;
//    }
//
//    class Base extends Component{
//
//
//        public Base() {
//            super();
//        }
//    }
//
//    class PaoTai extends Component {
//        double r;
//
//        public PaoTai(double r) {
//            super();
//            this.r = r;
//        }
//    }
//
//
//    class Gun extends Component {
//        public Gun() {
//            super();
//        }
//    }
//
//
//    class Wheel extends Component {
//
//        WheelPattern wheelPattern;
//        public Wheel() {
//            super();
//            wheelPattern = new WheelPattern();
//        }
//
//        public void setWheelPattern(Vector2D[] pattern) {
//            this.wheelPattern.setOutlines(pattern);
//        }
//    }
//
//
//    class WheelPattern extends Component {
//        public WheelPattern() {
//            super();
//        }
//    }
//}
