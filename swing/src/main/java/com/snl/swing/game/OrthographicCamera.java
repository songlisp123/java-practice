package com.snl.swing.game;

import com.snl.swing.game.math.AABB;
import com.snl.swing.game.math.Matrix4x4f;
import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game.math.Vector3D;

public class OrthographicCamera {

    private Vector3D position;
    private Vector3D direction;
    //摄像机“上方”,在《计算机视觉中》，这个初始化为j向量
    private Vector3D viewUp;
    //摄像机侧方
    private Vector3D vSide;

    //近裁剪面
    public float near = -1;
    //远裁剪面
    public float far = 999;

    //视口宽
    public double l,r;
    //视口高
    public double b,t;

    //摄像机矩阵
    public Matrix4x4f cameraMat;
    //视图矩阵
    public Matrix4x4f viewMat;
    //投影矩阵
    public Matrix4x4f projectionMat;

    public OrthographicCamera() {
        this(0);
    }

    public OrthographicCamera(double d) {
        r = t = 1;
        l = b = -1;
        direction = Vector3D.direction(0,0,-1); //默认看向-z轴
        position = Vector3D.point(0,0,d);
        viewUp = Vector3D.J;//默认j轴
        vSide = direction.crossDot(viewUp);//默认i轴
        resetViewMat();
        projection();
    }

    public void resetViewMat() {
        if (cameraMat == null) {
            cameraMat = new Matrix4x4f();
        }
        cameraMat.setColumn(0,vSide);
        cameraMat.setColumn(1,viewUp);
        cameraMat.setColumn(2,direction);
        cameraMat.setColumn(3,position);

        viewMat = cameraMat.inverse();
    }

    public void projection() {
        if (projectionMat == null) {
            projectionMat = new Matrix4x4f();
        }
        projectionMat.set(2  / (r - l),0,0);
        projectionMat.set(2  /(t - b),1,1);
        projectionMat.set(-2 / (far - near),2,2);
        projectionMat.set(-(r + l) / (r - l),0,3);
        projectionMat.set(-(t + b) / (t - b),1,3);
        projectionMat.set(-(far + near) /(far - near),2,3);
        projectionMat.set(1,3,3);
    }

    public Vector3D projectionToNDCSpace(Vector3D worldPosition) {
        //转交换为视图坐标
        Vector3D view = viewMat.mul(worldPosition);
        //视图坐标投影到摄像机视口宽度
        view = projectionMat.mul(view);
        view.perspectiveDivide();
        return view;
    }

    public Vector2D projectionToScreen(Vector3D world, double screenWidth, double screenHeight, double screenX, double screenY) {
        Vector3D ndcSpace = projectionToNDCSpace(world);
        Vector2D v2 = new Vector2D();
        v2.x = ndcSpace.x * screenWidth / 2.0 + screenWidth / 2.0 + screenX;
        v2.y = - ndcSpace.y * screenHeight / 2.0 + screenHeight / 2.0 + screenY;
        v2.w = 1;

        return v2;
    }

    public Matrix4x4f getViewMat() {
        return viewMat;
    }

    public Matrix4x4f getProjectionMat() {
        return projectionMat;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public void setL(double l) {
        this.l = l;
    }

    public void setR(double r) {
        this.r = r;
    }

    public void setB(double b) {
        this.b = b;
    }

    public void setT(double t) {
        this.t = t;
    }

    public void translatedX(float cord) {
        position.x += cord;
    }

    public void translatedY(float cord) {
        position.y += cord;
    }

    public double getL() {
        return l;
    }

    public double getR() {
        return r;
    }

    public double getB() {
        return b;
    }

    public double getT() {
        return t;
    }

    public AABB getViewBoundingBox() {
        double halfW = (r - l) / 2;
        double halfH = (t - b) / 2;
        return new AABB(new Vector2D(position.x - halfW,position.y - halfH),new Vector2D(position.x + halfW,position.y + halfH));
    }

    public void setNear(float near) {
        this.near = near;
    }

    public void setFar(float far) {
        this.far = far;
    }


    public Vector3D getPosition() {
        return position;
    }

    public Vector3D getDirection() {
        return direction;
    }

    public Vector3D getViewUp() {
        return viewUp;
    }

    public Vector3D getvSide() {
        return vSide;
    }

    public void setDirection(Vector3D direction) {
        this.direction = direction;
    }

    public void setvSide(Vector3D vSide) {
        this.vSide = vSide;
    }

    public void setViewUp(Vector3D viewUp) {
        this.viewUp = viewUp;
    }

    public Matrix4x4f getCameraMat() {
        return cameraMat;
    }

    public void update() {
        resetViewMat();
        projection();
    }
}
