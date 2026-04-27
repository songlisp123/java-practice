package com.snl.swing.game2d.frame;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class WindowFramework extends GameFramework {

    private Canvas canvas;

    @Override
    protected void createFramework() {
        canvas = new Canvas();
        canvas.setBackground(appBackground);
        canvas.setIgnoreRepaint(true);
        getContentPane().add(canvas);
        setLocationByPlatform(true);
        if (appMaintainRatio) {
            getContentPane().setBackground(appBorder);
            setSize(appWidth,appHeight);
            setLayout(null);
            getContentPane().addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    onComponentResized(e);
                }
            });
        }else {
            //如果不维持比率
            canvas.setSize(appWidth,appHeight);
            pack();
        }
        setTitle(appTitle);
        //设置输入
        setupInput(canvas);
        //设置可见性
        setVisible( true);
        //设置双缓冲
        createBufferStrategy(canvas);
        //h获取焦点
        canvas.requestFocus();
    }

    protected void onComponentResized( ComponentEvent e ) {
        Dimension size = getContentPane().getSize();
        setupViewport( size.width, size.height );
        canvas.setLocation( vx, vy );
        canvas.setSize( vw, vh );
    }

    @Override
    protected void renderFrame(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.clearRect(0,0,getScreenWidth(),getScreenHeight());
        render(g);
    }

    @Override
    public int getScreenWidth() {
        return canvas.getWidth();
    }

    @Override
    public int getScreenHeight() {
        return canvas.getHeight();
    }
}
