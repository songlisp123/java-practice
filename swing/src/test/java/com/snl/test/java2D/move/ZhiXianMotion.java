package com.snl.test.java2D.move;

import com.snl.test.java2D.coords.DiKaErPlus;
import java.awt.*;

public class ZhiXianMotion extends DiKaErPlus {

    int moveMode;

    public ZhiXianMotion() throws HeadlessException {
        appSleep = 12;
        appFont = new Font("宋体",Font.BOLD,15);
    }

    public static void main(String[] args) {
        launchGame(new ZhiXianMotion());
    }
}
