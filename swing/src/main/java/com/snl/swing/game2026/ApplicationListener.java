package com.snl.swing.game2026;

import com.snl.swing.game2026.util.Dispose;

public interface ApplicationListener extends Dispose {

    //当应用程序被初次创建时候调用
    void create();

    //当重新设置窗口大小时调用此方法
    //每次游戏屏幕重新调整大小且游戏未处于暂停状态时都会调用此方法。它也在 create() 方法之后立即调用一次
    void resize(int wight,int height);

    //渲染操作
    void render();

    //暂停时的回调接口，当窗口变小或者不可见之时
    //在桌面上，当窗口最小化并在退出应用程序之前（ dispose() ）会调用此方法
    void pause();

    //当放大窗口，或者窗口重新聚焦的时候
    //在桌面上窗口取消最小化时会调用此方法。
    void resume();
}
