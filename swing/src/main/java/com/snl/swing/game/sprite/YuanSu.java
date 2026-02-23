package com.snl.swing.game.sprite;

public final class YuanSu {

    public YuanSu() {
    }

    public static final byte FIRE = 1;
    public static final byte WATER = 1 << 1;
    public static final byte EARTH = 1 << 2;
    public static final byte WIND = 1 << 3;

    /**
     * 火与土元素
     */
    public static final byte FIRE_EARTH = (byte) (YuanSu.FIRE | YuanSu.EARTH); //火与土
    /**
     * 火与风
     */
    public static final byte FIRE_WIND = (byte) (YuanSu.FIRE | YuanSu.WIND); //火与凤
    /**
     * 水与土
     */
    public static final byte WATER_EARTH = (byte) (YuanSu.WATER | YuanSu.EARTH); //水与土
    /**
     * 水与风
     */
    public static final byte WATER_WIND = (byte) (YuanSu.WATER | YuanSu.WIND); // 水与风
    /**
     * 土与风
     */
    public static final byte EARTH_WIND = (byte) (YuanSu.EARTH | YuanSu.WIND);//土与风

    public static boolean containsElement(byte current,byte base) {
        return (current & base) == base;
    }
}
