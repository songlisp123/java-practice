package com.snl.swing.tank;

import com.snl.swing.game.math.Matrix3x3f;
import com.snl.swing.game.math.Vector2D;

import java.awt.*;


public abstract class Component implements DrawAble {

    Matrix3x3f transFrom;
    Matrix3x3f rotateForm;
    Matrix3x3f scaleForm;


    Matrix3x3f parentForm;

    Vector2D[] outlines,copy;


    //相较于局部坐标系
    Vector2D rotateCenter,scaleCenter;

    Paint paint;

    public Component() {
        transFrom = Matrix3x3f.identity();
        rotateForm = Matrix3x3f.identity();
        scaleForm = Matrix3x3f.identity();

        parentForm = Matrix3x3f.identity();
        rotateCenter = new Vector2D();
        scaleCenter = new Vector2D();
    }

    public void setTransFrom(Matrix3x3f transFrom) {
        this.transFrom = transFrom;
    }

    public void setRotateForm(Matrix3x3f rotateForm) {
        this.rotateForm = rotateForm;
    }

    public void setScaleForm(Matrix3x3f scaleForm) {
        this.scaleForm = scaleForm;
    }

    public void setOutlines(Vector2D[] outlines) {
        this.outlines = outlines;
    }

    public Matrix3x3f localModel() {
        return transFrom.mul(
                Matrix3x3f.translate(rotateCenter).mul(rotateForm).mul(Matrix3x3f.translate(rotateCenter.inv()))
        )
                .mul(Matrix3x3f.translate(scaleCenter).mul(scaleForm).mul(Matrix3x3f.translate(scaleCenter.inv())));
    }

    public Matrix3x3f modelToWorld() {
        return parentForm.mul(localModel());
    }


    public Vector2D getRotateCenter() {
        return rotateCenter;
    }

    public void setRotateCenter(Vector2D rotateCenter) {
        this.rotateCenter = rotateCenter;
    }

    public Vector2D getScaleCenter() {
        return scaleCenter;
    }

    public void setScaleCenter(Vector2D scaleCenter) {
        this.scaleCenter = scaleCenter;
    }

    protected double getW() {
        //
        return outlines[1].x * 2;
     }
    protected double getH() {
        return  outlines[2].y * 2;
    }

    public int verticesCount() {
        return outlines.length;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }
}
