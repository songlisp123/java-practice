package com.snl.swing.game2026.test;

import com.snl.swing.game.gameFrame.DiKaErPlus;
import com.snl.swing.game2026.Application;
import com.snl.swing.game2026.ApplicationListener;

import java.awt.event.KeyEvent;

public class SwingApplication implements Application {

    private ApplicationListener applicationListener;
    private Thread gameThread;
    private boolean running;

    public SwingApplication() {

        this.applicationListener = new SwingFrame();
        running = true;
        gameThread = new Thread(game(),"游戏线程");
        gameThread.start();
    }

    public SwingApplication(ApplicationListener applicationListener) {
        this.applicationListener = applicationListener;

        running = true;
        gameThread = new Thread(game(),"游戏线程");
        gameThread.start();
    }

    @Override
    public ApplicationListener getApplicationListener() {
        return applicationListener;
    }

    @Override
    public void exit() {
        running = false;
        Thread thread = Thread.currentThread();
        try {
                Thread g = gameThread;
                g.join();
                gameThread = null;
            this.applicationListener.dispose();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Runnable  game() {
        return () -> {
            applicationListener.create();
            while (running) {
                gameLoop();
                sleep();
            }
        };
    }

    private void sleep() {
        try {
            Thread.sleep(16);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private void gameLoop() {

    }

    public void setApplicationListener(ApplicationListener applicationListener) {
        if (applicationListener == null)
            throw new IllegalArgumentException("参数不能为null");
        this.applicationListener = applicationListener;
    }

   class SwingFrame extends DiKaErPlus implements ApplicationListener {
        @Override
        public void create() {
            launchGame(this);
        }

        @Override
        public void resize(int wight, int height) {

        }

        @Override
        public void render() {

        }

        @Override
        public void pause() {

        }

        @Override
        public void resume() {

        }

        @Override
        public void dispose() {
            super.running = false;
            setVisible(false);
        }

       @Override
       protected void processInput(double delta) {
           super.processInput(delta);
           if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
               SwingApplication.this.exit();
       }
   }
}
