package com.snl.swing.game.anime;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class DitherDissolveEffect implements Part {
    private final Component surf;
    private int beginning, ending;
    private BufferedImage bimg;
    private Graphics2D big;
    private List<Integer> list, xlist, ylist;
    private int xeNum, yeNum;    // 元素数量
    private int xcSize, ycSize;  // 块大小
    private int inc;
    private int blocksize;

    public DitherDissolveEffect(int beg, int end, int blocksize, Component surf) {
        this.beginning = beg;
        this.ending = end;
        this.blocksize = blocksize;
        this.surf = surf;
        reset(0,0);
    }

    private void createShuffledLists() {
        int width = bimg.getWidth();
        int height = bimg.getHeight();
        xlist = new ArrayList<Integer>(width);
        ylist = new ArrayList<Integer>(height);
        list = new ArrayList<Integer>(ending - beginning + 1);
        for (int i = 0; i < width; i++) {
            xlist.add(i, i);
        }
        for (int i = 0; i < height; i++) {
            ylist.add(i, i);
        }
        for (int i = 0; i < (ending - beginning + 1); i++) {
            list.add(i, i);
        }
        java.util.Collections.shuffle(xlist);
        java.util.Collections.shuffle(ylist);
        java.util.Collections.shuffle(list);
    }

    @Override
    public void reset(int w, int h) {
        bimg = null;
    }

    @Override
    public void step(int w, int h) {
        if (inc > ending) {
            bimg = null;
        }
        if (bimg == null) {
            int biw = surf.getWidth();
            int bih = surf.getHeight();
            bimg = new BufferedImage(biw, bih,
                    BufferedImage.TYPE_INT_RGB);
            createShuffledLists();
            big = bimg.createGraphics();
//            big.drawImage(surf.bimg, 0, 0, null);
            xcSize = (xlist.size() / (ending - beginning)) + 1;
            ycSize = (ylist.size() / (ending - beginning)) + 1;
            xeNum = 0;
            inc = 0;
        }
        xeNum = xcSize * list.get(inc);
        yeNum = -ycSize;
        inc++;
    }

    @Override
    public void render(int w, int h, Graphics2D g2) {
        big.setColor(Color.WHITE);

        for (int k = 0; k <= (ending - beginning); k++) {
            if ((xeNum + xcSize) > xlist.size()) {
                xeNum = 0;
            } else {
                xeNum += xcSize;
            }
            yeNum += ycSize;

            for (int i = xeNum; i < xeNum + xcSize && i < xlist.size();
                 i++) {
                for (int j = yeNum; j < yeNum + ycSize && j
                        < ylist.size(); j++) {
                    int xval = xlist.get(i);
                    int yval = ylist.get(j);
                    if (((xval % blocksize) == 0) && ((yval % blocksize)
                            == 0)) {
                        big.fillRect(xval, yval, blocksize, blocksize);
                    }
                }
            }
        }

        g2.drawImage(bimg, 0, 0, null);
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
