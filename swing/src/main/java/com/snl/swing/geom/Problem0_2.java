package com.snl.swing.geom;

import java.awt.*;

public class Problem0_2 extends ProblemSolver {

    @Override
    protected void gameInitial() {
        duration = 5000L;
        super.gameInitial();
        sb.append("练习 3.5：创建几个简单的模型，例如三棱柱、四面体，以及一个 1×2×3 的长方体，并在渲染程序中进行实验");
        super.openTextPanel();
    }

    @Override
    void forwardUp() {

    }

    @Override
    void backUp() {

    }

    @Override
    void drawContent(Graphics2D g2) {

    }

    public static void main(String[] args) {
        launchGame(new Problem0_2());
    }
}
