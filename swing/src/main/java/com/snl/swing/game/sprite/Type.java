package com.snl.swing.game.sprite;

public enum Type {

    DU(1,"毒"),
    HUIFU(2,"恢复"),
    BAOJI(3,"暴击"),
    SHUDU(4,"速度"),
    ;
    private final int ID;
    private final String introduction;

    Type(int id, String s) {
        this.ID = id;
        this.introduction = s;
    }
}
