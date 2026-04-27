package com.snl.swing.game2d.completegame.state;

import com.snl.swing.game2d.CompleteGame;
import com.snl.swing.game2d.tool.Matrix3x3f;

import java.awt.*;

public class State {
    protected StateController controller;
    protected CompleteGame app;

    public void setController(StateController controller) {
        this.controller = controller;
        app = (CompleteGame) controller.getAttribute("app");
    }

    protected StateController getController() {
        return controller;
    }

    public void enter() {

    }

    public void processInput(double delta) {
    }

    public void updateObjects(double delta) {
    }

    public void render(Graphics2D g, Matrix3x3f view) {
    }

    public void exit() {

    }
}
