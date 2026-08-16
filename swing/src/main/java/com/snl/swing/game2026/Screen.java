package com.snl.swing.game2026;

import com.snl.swing.game2026.util.Dispose;

//屏幕应该具有以下属性：
/*
1、展示
2、渲染
3.重心设置大小
4.暂停
5.恢复
6.隐藏（什么意思？？？）
7.释放资源
 */
public interface Screen extends Dispose {

    /**
     * 在官方文档中，当此{@code screen}成为{@code game}的显示窗口时调用
     */
    void show();

    /**
     *
     * @param delta 距离上一次渲染后经过的时间间隔
     */
    void render(float delta);

    //重设置大小
    void resize(int wight,int height);
    //当应用程序停止的时候，调用
    void pause();
    //当应用程序恢复的时候调用此方法
    void resume();
    //当此屏幕不再是该游戏的窗口
    void hide();
}
