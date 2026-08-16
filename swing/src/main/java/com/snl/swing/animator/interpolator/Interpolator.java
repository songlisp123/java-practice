package com.snl.swing.animator.interpolator;

public interface Interpolator {
    /**
     * 插值操作
     * @param fraction 动画播放百分比，<b>注意：动画播放与现实时间并不吻合，牢记这一点</b>
     * @return 插值后的值，通常这是一个{@code 双精度浮点数}
     */
    double interpolate(double fraction);
}
