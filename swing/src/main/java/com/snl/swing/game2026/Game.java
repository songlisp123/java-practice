package com.snl.swing.game2026;

public abstract class Game implements ApplicationListener {
    //游戏在屏幕上运行
    private Screen screen;


    @Override
    public void resize(int wight, int height) {
        if (screen != null)screen.resize(wight, height);
    }

    @Override
    public void render() {
        if (screen != null) screen.render(0.16f);
    }

    @Override
    public void pause() {
        if (screen != null) screen.pause();
    }

    @Override
    public void resume() {
        if (screen != null) screen.resume();
    }

    @Override
    public void dispose() {
        //为什么调用的是hide方法？？
        if (screen != null) screen.dispose();
    }

    /**
     * 对于即将要换的屏幕来说，调用其{@code hide}方法
     * 对于新的屏幕，调用其{@code show}方法
     * @param screen 新的屏幕
     */
    public void setScreen(Screen screen) {
        if (this.screen != null) this.screen.hide();
        this.screen = screen;
        if (this.screen != null)
        {
            this.screen.show();
        }
    }

    public Screen getScreen() {
        return screen;
    }
}
