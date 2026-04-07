package com.snl.swing.practice01.CONSTANTS;

import java.awt.*;

public class GameConstants {

    public static final double GRAVITY = 9.98;
    public static final double FRACTOR = 0.25;

    public static final int Weight = 800;
    public static final int Height = 600;
    public static final int MAPHeight = 1200;
    public static final int MAPWIDTH = 800;

    public static final int squreWidth = 20;
    public static final int squreHeight = 20;

    public static final int COLUMNS = Weight / squreWidth;
    public static final int ROWS = Height / squreHeight;

    public static final int COIN_WEIGHT = 10;
    public static final int COIN_HEIGHT = 10;

    public static final char WALL = '.';
    public static final char NOTHING = 'x';
    public static final char PLAYER = 'p';
    public static final char ENMRY = 'e';
    public static final char Left_RIGHT_ENERY = 'r';
    public static final char COIN = 'c';
    public static final char DOOR = 'd';
    public static final char ADVANCE_ENERY = 'a';
    public static final char WATER = 'w';
    public static final char GRASS = 'g';
    public static final char StoneChar = 's';
    public static final char MUTOU = 'm';
    public static final char LEVEL = 'l';

    public static final int PLAYERLIFES = 3;
    public static final int GAINT = 5;


    public static final int pistolKill = 56;
    public static final double PISTOL_ORIGIN_SHOOT_SPEED = 3.5;
    public static final double PISTOL_ORIGIN_HEARING_RANGE = 36.7;
    public static final double PISTOL_ORIGIN_KILL_DAMAGE = 10.5;
    public static final int PISTOL_ORIGIN_RECOIL = 200;


    /*            颜色               */
    public static final Color tuRang02 = new Color(255,200,0,150);
    public static final Color tuRang = new Color(255,0,255,150);
    public static final Color Water = new Color(0,255,255,150);
    public static final Color Water02 = new Color(220, 30, 30,156);
    public static final Color Grass = new Color(0,255,0,180);
    public static final Color Grass02 = new Color(255,0,0,180);
    public static final Color Stone = new Color(205,205,205,180);
    public static final Color MUCHAI = new Color(212, 169, 21, 255);
}
