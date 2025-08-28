package wormsubject;

import javax.sound.sampled.Line;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

public class SimpleFrame  extends JFrame {
    protected int WIDTH;
    protected int HEIGHT;

    public SimpleFrame(int width, int height) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = d.width;
        int screenHeight = d.height;
        //初始化窗口大小
        this.WIDTH = width;
        this.HEIGHT = height;
        int x = (screenWidth - WIDTH) / 2;
        int y = (screenHeight - HEIGHT) / 2;
        setBounds(x, y, WIDTH, HEIGHT);


        setTitle("贪吃蛇");

        //目前已经配置好了界面和颜色
    }
}
