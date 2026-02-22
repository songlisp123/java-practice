package com.snl.swing.game.anime;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;

public class GunDongEffect implements Part {


    private static final String[] members = {
            "Brian Lichtenwalter", "Jeannette Hung",
            "Thanh Nguyen", "Jim Graham", "Jerry Evans",
            "John Raley", "Michael Peirce", "Robert Kim",
            "Jennifer Ball", "Deborah Adair", "Paul Charlton",
            "Dmitry Feld", "Gregory Stone", "Richard Blanchard",
            "Link Perry", "Phil Race", "Vincent Hardy",
            "Parry Kejriwal", "Doug Felt", "Rekha Rangarajan",
            "Paula Patel", "Michael Bundschuh", "Joe Warzecha",
            "Joey Beheler", "Aastha Bhardwaj", "Daniel Rice",
            "Chris Campbell", "Shinsuke Fukuda", "Dmitri Trembovetski",
            "Chet Haase", "Jennifer Godinez", "Nicholas Talian",
            "Raul Vera", "Ankit Patel", "Ilya Bagrak",
            "Praveen Mohan", "Rakesh Menon"
    };
    private static final Font font = new Font(Font.SERIF, Font.PLAIN, 26);
    private final FontMetrics fm;
    private int beginning, ending;
    private int nStrs, strH, index, yh, height;
    private java.util.List<String> v = new ArrayList<String>();
    private List<String> cast =
            new ArrayList<String>(members.length + 3);
    private int counter, cntMod;
    private GradientPaint gp;

    public GunDongEffect(int beg, int end, Component surf) {
        this.beginning = beg;
        this.ending = end;
        fm = surf.getFontMetrics(font);
        cast.add("CONTRIBUTORS");
        cast.add(" ");
        cast.addAll(Arrays.asList(members));
        cast.add(" ");
        cast.add(" ");
        cntMod = (ending - beginning) / cast.size() - 1;
        reset(surf.getWidth(),surf.getHeight());
    }

    @Override
    public void reset(int w, int h) {
        v.clear();
        strH = (fm.getAscent() + fm.getDescent());
        nStrs = (h - 40) / strH + 1;
        height = strH * (nStrs - 1) + 48;
        index = 0;
        gp = new GradientPaint(0, h / 2, WHITE, 0, h + 20, BLACK);
        counter = 0;
    }

    @Override
    public void step(int w, int h) {
        if (counter++ % cntMod == 0) {
            if (index < cast.size()) {
                v.add(cast.get(index));
            }
            if ((v.size() == nStrs || index >= cast.size()) && !v.
                    isEmpty()) {
                v.remove(0);
            }
            ++index;
        }
    }

    @Override
    public void render(int w, int h, Graphics2D g2) {
        g2.setPaint(gp);
        g2.setFont(font);
        double remainder = counter % cntMod;
        double incr = 1.0 - remainder / cntMod;
        incr = incr == 1.0 ? 0 : incr;
        int y = (int) (incr * strH);

        if (index >= cast.size()) {
            y = yh + y;
        } else {
            y = yh = height - v.size() * strH + y;
        }
        for (String s : v) {
            g2.drawString(s, w / 2 - fm.stringWidth(s) / 2, y += strH);
        }
    }

    @Override
    public int getEnd() {
        return ending;
    }

    @Override
    public int getStart() {
        return beginning;
    }
}
