package com.snl.swing.spline;

import com.snl.swing.game.gameFrame.DiKaErPlus;

import javax.swing.*;
import java.awt.*;

public class TestEquation extends JFrame {

     static EquationDisplay display;

     static AbstractEquation lineEquation,e2,e3;


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600,500);
    }

    private static void createUi() {
        JFrame f = new TestEquation();
        f.setLocationRelativeTo(null);
        f.setResizable(false);
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
        f.setSize(600,500);
        f.setVisible(true);

        display = new EquationDisplay(
                0,0,-1,1,-1,1,10,10,10,10);
        lineEquation = new LineEquation(1,-1,3);
        display.addEquation(lineEquation,Color.RED);

        e2 = new SinEquation();
        display.addEquation(e2,Color.BLUE);

        e3 = new QuEquation(1,0,0);
        display.addEquation(e3,Color.YELLOW);
        f.add(display,BorderLayout.CENTER);

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(TestEquation::createUi);
    }

}
