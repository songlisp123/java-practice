package com.snl.test.java2D.font;

import com.snl.test.java2D.coords.DiKaErPlus;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GunDongFonts extends DiKaErPlus {

    final  List<Font> fonts = new ArrayList<>();
    boolean kangjuchi;
    Font f = new Font("隶书",Font.PLAIN,20);
    int index;
    List<Font> v = new ArrayList<>();

    int strH,nStrs,fi;
    float fSize = 25;
    double count;
    double animationTime;
    boolean moving;

    public GunDongFonts() throws HeadlessException {
        getAllFonts();
        drawAxis = false;
        animationTime = 3;
        moving = true;
    }

    private void getAllFonts() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] allFonts = ge.getAllFonts();
        for (Font font : allFonts)
        {
            if (font.canDisplayUpTo(font.getName()) == -1)
                fonts.add(font);
        }
    }

    @Override
    protected void gameInitial() {
        super.gameInitial();
        v.clear();
        Font f1 = fonts.getFirst().deriveFont(fSize);
        FontMetrics fm = getFontMetrics(f1);
        strH = fm.getAscent() + fm.getDescent() + fm.getLeading();
        nStrs = c.getHeight() / strH + 1;
        fi = 0;
    }

    @Override
    protected void processInput(double delta) {
        super.processInput(delta);
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_B))
        {
            kangjuchi = !kangjuchi;
        }
        if (keyBoardEvent.keyDownOnce(KeyEvent.VK_SPACE))
            moving = !moving;
    }

    @Override
    protected void updateSprite(double delta) {
        super.updateSprite(delta);
        if (moving)
            count += animationTime * delta;
        if (count > 0.5) {
            count = 0;
            if (fi < fonts.size()) {
                v.add(fonts.get(fi).deriveFont(fSize));
            }
            if (v.size() == nStrs && !v.isEmpty() || fi > fonts.size()) {
                v.removeFirst();
            }
            fi = v.isEmpty()? 0 :++fi;
        }

    }


    @Override
    protected void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.draw(g);
        if (kangjuchi)
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(f);
        g2.setColor(Color.WHITE);
        animation(g2,0);
        g2.dispose();
    }

    protected void animation(Graphics2D g2, double delta) {
        int h = c.getHeight();
        int w = c.getWidth();
        int yy = (fi >= fonts.size()) ? 0 : h - v.size() * strH - strH / 2;

        FontRenderContext frc = g2.getFontRenderContext();
        for (Font font : v) {
            TextLayout tl = new TextLayout(font.getName(), font, frc);
            float advance = tl.getAdvance();
            g2.setFont(font);
            g2.drawString(font.getName(), (int) (w / 2.0 - advance / 2), yy += strH);
        }
    }

    private void drawString(Graphics2D g2, List<Font> fonts) {
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        FontRenderContext frc = g2.getFontRenderContext();
        float ascent,advance;
        float h = 0;
        float leftX;
        int width = c.getWidth();
        int height = c.getHeight();
        float top,bottom;
        top = 20f;
        bottom = 40F;
        ArrayList<Font> temp = new ArrayList<>(fonts);
        for (int i = index;i<temp.size();i++) {
            Font font = temp.get(i);
            font = font.deriveFont(30F);
            TextLayout tl = new TextLayout(font.getFontName(Locale.CHINA), font, frc);
            //获取上行
            ascent = tl.getAscent();
            //获取全部长
            advance = tl.getAdvance();
            //获取高
            h = (i == 0) ? top + ascent : h + tl.getAscent() + tl.getDescent() + tl.getLeading();
            leftX = (width - advance) / 2.0F;
            tl.draw(g2,leftX,h);
            if (h > height - bottom) {
                index++;
            }
        }
    }

    public static void main(String[] args) {
        launchGame(new GunDongFonts());
    }
}
