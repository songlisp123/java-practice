package com.snl.swing.game2d.completegame.state;

import com.snl.swing.game2d.completegame.admin.Acme;
import com.snl.swing.game2d.tool.Matrix3x3f;
import com.snl.swing.game2d.util.Sprite;
import com.snl.swing.game2d.util.Utility;

import java.awt.*;

public class LevelStarting extends State {
    //时间？？
    double time;
    //背景？？
    private Sprite background;
    //游戏状态
    private GameState state;
    private Acme acme;

    public LevelStarting(GameState state) {
        this.state = state;
    }

    @Override
    public void enter() {
        background = (Sprite) controller.getAttribute("background");
        acme = (Acme) controller.getAttribute("ACME");
        time = 0.0;
    }

    @Override
    public void updateObjects(double delta) {
        time += delta;
        if (time > 2.0) {
            //如果超过  2 秒，就转换游戏状态
            getController().setState(new LevelPlaying(state));
        }
    }

    @Override
    public void render(Graphics2D g, Matrix3x3f view) {
        super.render(g, view);
        background.render(g, view);
        acme.drawScore(g, state.getScore());
        acme.drawLives(g, view, state.getLives());
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.setColor(Color.GREEN);
        Utility.drawCenteredString(g, app.getScreenWidth(),
                app.getScreenHeight() / 3, "L E V E L " + state.getLevel());
    }
}
