package com.snl.swing.tank;

public interface RotateTransForm {


    /*
    以局部坐标系为中心的当前旋转的角度
     */
    double getRot();

    /**
     * 获取上一次旋转的角度
     * @return 上一次旋转的角度
     */
    double getLastRot();


    /**
     * 获取角速度，单位是弧度/秒
     * @return 角速度
     */
    double getAnglerSpeed();


    /**
     * 设置角速度
     * @param angleSpeed 新的角速度
     */
    void setAnglerSpeed(double angleSpeed);

    void setRot(double rot);
}
