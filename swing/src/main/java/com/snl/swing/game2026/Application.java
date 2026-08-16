package com.snl.swing.game2026;

import javax.swing.*;

/**
 * 在作者的文档中，这类似于{@code swing}中的{@link  JFrame}
 */
public interface Application {
    //程序应该包含一个应用程序监听器,你的游戏框架应该实现这个接口
    ApplicationListener getApplicationListener();
    //退出应用程序
    void exit();
}
