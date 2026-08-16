package com.snl.swing.game;

import com.snl.swing.game.math.Epsilon;
import com.snl.swing.game.math.Matrix4x4f;
import com.snl.swing.game.math.Vector2D;
import com.snl.swing.game.math.Vector3D;

import java.awt.*;

public class Camera {

    //世界坐标位置
    private Vector3D position;
    //视图方向
    private Vector3D direction;
    //摄像机“上方”,在《计算机视觉中》，这个初始化为j向量
    private Vector3D viewUp;
    //摄像机侧方
    private Vector3D vSide;

    //近裁剪面
    public float near = 1;
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

    private Vector3D tempV = Vector3D.point(0,0,0);

    public Camera() {
        reset();
    }

    public void reset() {
        r = t = 1;
        l = b = -1;
        near = 1;
        far = 999;
        position = Vector3D.point(0,0,0);
        direction = Vector3D.direction(0,0,-1);//默认看向-z轴
        viewUp = Vector3D.direction(0,1,0);//默认j轴
        vSide = viewUp.crossDot(direction);
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


    public void lookAt(double x,double y,double z) {
        tempV.x = x;
        tempV.y = y;
        tempV.z = z;
        Vector3D d = tempV.subtract(position).norm();
        //如过我们观测的点与相机的位置重合，这是未定义的
        if (!d.isZero()){
            //现在我们需要判断up向量是否与视图向量垂直
            double dot = viewUp.dot(d);
            if (Math.abs(dot - 1) < Epsilon.PRECISION) {
                //cos = 1,共线,从几何上看，摄像机绕x轴逆时针旋转90度操作,此时，up
                //参数 = -direction
                viewUp.x = -direction.x;
                viewUp.y = -direction.y;
                viewUp.z = -direction.z;
            } else if (Math.abs(dot + 1) < Epsilon.PRECISION) {
                //cos = -1,反向，几何上看，这是一个顺时针绕x轴旋转90度操作此时，up
                //                //参数 = direction
                viewUp.x = direction.x;
                viewUp.y = direction.y;
                viewUp.z = direction.z;
            }
            
            //设置方向向量
            direction.x = d.x;
            direction. y= d.y;
            direction.z = d.z;
            //归一化向量
            normalizeUp();
            //视图矩阵
            resetViewMat();
        }
    }

    private void normalizeUp() {
        //首先计算出侧向量
        vSide = direction.crossDot(viewUp).norm();
        viewUp = vSide.crossDot(direction).norm();
    }

    public void lookAt(Vector3D target) {
        this.lookAt(target.x,target.y,target.z);
    }

    public void projection() {
        if (projectionMat == null) {
            projectionMat = new Matrix4x4f();
        }
        projectionMat.set(2 * near / (r - l),0,0);
        projectionMat.set(2 *   near /(t - b),1,1);
        projectionMat.set((r + l) / (r - l),0,2);
        projectionMat.set((t + b) / (t - b),1,2);
        projectionMat.set((near + far) / (near - far),2,2);
        projectionMat.set(-1,3,2);

        projectionMat.set(2 * near * far / (near - far),2,3);
        projectionMat.set(0,3,3);
    }


    public Vector3D projectionToNDCSpace(Vector3D worldPosition) {
        //转交换为视图坐标
        Vector3D view = viewMat.mul(worldPosition);
        //视图坐标投影到摄像机视口宽度
        view = projectionMat.mul(view);
        view.perspectiveDivide();
        return view;
    }

    public Vector2D projectionToScreen(Vector3D world,double screenWidth,double screenHeight,double screenX,double screenY) {
        Vector3D ndcSpace = projectionToNDCSpace(world);
        Vector2D v2 = new Vector2D();
        v2.x = ndcSpace.x * screenWidth / 2.0 + screenWidth / 2.0 + screenX;
        v2.y = - ndcSpace.y * screenHeight / 2.0 + screenHeight / 2.0 + screenY;
        v2.w = 1;
        return v2;
    }

    public Vector2D projectionToCanvas(Vector3D world , Component c) {
        return projectionToScreen(world,c.getWidth() / 2,c.getHeight() / 2,50,50);
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
}
