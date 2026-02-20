package com.snl.swing.game.components;

public class CollideObj {
    //包围矩形
    protected double leftX,leftY;
    protected double totalW,totalH;

    public CollideObj(double leftX, double leftY, double totalW, double totalH) {
        this.leftX = leftX;
        this.leftY = leftY;
        this.totalW = totalW;
        this.totalH = totalH;
    }

    public double getLeftX() {
        return leftX;
    }

    public void setLeftX(double leftX) {
        this.leftX = leftX;
    }

    public double getLeftY() {
        return leftY;
    }

    public void setLeftY(double leftY) {
        this.leftY = leftY;
    }

    public double getTotalW() {
        return totalW;
    }

    public void setTotalW(double totalW) {
        this.totalW = totalW;
    }

    public double getTotalH() {
        return totalH;
    }

    public void setTotalH(double totalH) {
        this.totalH = totalH;
    }
}
