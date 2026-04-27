package com.snl.swing.game2d.completegame.state;

import com.snl.swing.game2d.completegame.admin.Acme;
import com.snl.swing.game2d.completegame.admin.HighScoreMgr;
import com.snl.swing.game2d.completegame.object.Asteroid;
import com.snl.swing.game2d.completegame.object.AsteroidFactory;
import com.snl.swing.game2d.input.KeyBoardEvent;
import com.snl.swing.game2d.tool.Matrix3x3f;
import com.snl.swing.game2d.util.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Vector;

public abstract class AttractState extends State {
    private List<Asteroid> asteroids;
    private double time;
    private Sprite background;
    private AsteroidFactory factory;
    protected Acme acme;
    protected KeyBoardEvent keys;
    protected HighScoreMgr highScoreMgr;

    public AttractState() {

    }

    public AttractState(List<Asteroid> asteroids) {
        this.asteroids = asteroids;
    }

    @Override
    public void enter() {
        highScoreMgr = (HighScoreMgr) controller.getAttribute("score");
        keys = (KeyBoardEvent) controller.getAttribute("keys");
        background = (Sprite) controller.getAttribute("background");
        factory = (AsteroidFactory) controller.getAttribute("factory");
        acme = (Acme) controller.getAttribute("ACME");
        if (asteroids == null) {
            asteroids = new Vector<>();
            asteroids.add(factory.getLargeAsteroid());
            asteroids.add(factory.getMediumAsteroid());
            asteroids.add(factory.getSmallAsteroid());
        }
        time = 0.0f;
    }

    @Override
    public void updateObjects(double delta) {
        time += delta;
        if (shouldChangeState()) {
            AttractState state = getState();
            state.setAsteroids(asteroids);
            getController().setState(state);
            return;
        }
        for (Asteroid a : asteroids) {
            a.update(delta);
        }
    }

    protected boolean shouldChangeState() {
        return time > getWaitTime();
    }

    protected float getWaitTime() {
        return 5.0f;
    }

    private void setAsteroids(List<Asteroid> asteroids) {
        this.asteroids = asteroids;
    }

    protected abstract AttractState getState();

    public List<Asteroid> getAsteroids() {
        return asteroids;
    }

    @Override
    public void processInput(double delta) {
        if (keys.keyDownOnce(KeyEvent.VK_ESCAPE)) {
            app.shutDownGame();
        }
        if (keys.keyDownOnce(KeyEvent.VK_SPACE)) {
            GameState state = new GameState();
            state.setLevel(1);
            state.setLives(2);
            getController().setState(new LevelStarting(state));
        }
    }

    @Override
    public void render(Graphics2D g, Matrix3x3f view) {
        background.render(g, view);
        for (Asteroid a : asteroids) {
            a.draw(g, view);
        }
    }
}
