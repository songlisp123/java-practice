package com.snl.data.homework.charptor03.practice01.level;

import com.snl.data.homework.charptor03.practice01.CONSTANTS.GameConstants;
import com.snl.data.homework.charptor03.practice01.entity.Door;
import com.snl.data.homework.charptor03.practice01.entity.Sprite;
import com.snl.data.homework.charptor03.practice01.entity.enmry.AdvancedEnemy;
import com.snl.data.homework.charptor03.practice01.entity.enmry.Enemy;
import com.snl.data.homework.charptor03.practice01.entity.goods.Coin;
import com.snl.data.homework.charptor03.practice01.entity.player.Player;
import com.snl.data.homework.charptor03.practice01.entity.wall.Grass;
import com.snl.data.homework.charptor03.practice01.entity.wall.Level;
import com.snl.data.homework.charptor03.practice01.entity.wall.TreeStem;
import com.snl.data.homework.charptor03.practice01.entity.wall.Wall;
import com.snl.data.homework.charptor03.practice01.entity.wall.Water;
import com.snl.data.homework.charptor03.practice01.level.map.JumpLevelMap;

import java.awt.*;

public class levelMapImplement<T extends Sprite> extends AbstractLevel<T> {


    public levelMapImplement(int index) {
        super();
        fillSprite(index);
    }

    @Override
    public void fillSprite(int index) {
        var enmries = getEnmries();
        var coins = getCoins();
        var walls = getWalls();
        Enemy enemy;
        Coin coin;
        Wall wall;
        int i,j;
        Character[][] level;
        level = JumpLevelMap.getLevel(index);
        int rows = level.length;
        System.out.println("rows = " + rows);
        int columns = level[0].length;
        System.out.println("columns = " + columns);
        for (j=0;j<rows;j++) {
            //行,y坐标
            for (i = 0;i<columns;i++) {
                //x坐标
                char c = level[j][i];
                switch (c) {

                    case GameConstants.COIN -> {
                        coin = new Coin(i * 20.0,j * 20.0,
                                GameConstants.COIN_WEIGHT,GameConstants.COIN_HEIGHT);
                        coins.add(coin);
                    }

                    case GameConstants.ENMRY -> {
                        enemy = new Enemy(i * 20.0,j * 20.0,
                                20,20);
                        enmries.add(enemy);
                    }

                    case GameConstants.PLAYER -> {
                        if (getPlayer() == null) {
                            System.out.println("初始化玩家");
                            var player = new Player(i * 20.0,j * 20.0,
                                    20,20);
                            setPlayer(player);
                        }else {
                            getPlayer().setyPos(i * 20.0);
                            getPlayer().setyPos(j * 20.0);
                        }
                    }

                    case GameConstants.DOOR -> {
                        var door  = new Door(i * 20.0,j * 20.0,
                                20,20);
                        setDoor(door);
                    }

                    case GameConstants.ADVANCE_ENERY -> {
                        enemy = new AdvancedEnemy(i * 20.0,j * 20.0,
                                100,100);
                        enmries.add(enemy);
                    }

                    case GameConstants.WATER -> {
                        wall = new Water(i * 20.0,j * 20.0,20,20);
                        walls.add(wall);
                    }

                    case GameConstants.GRASS -> {
                        wall = new Grass(i * 20.0,j * 20.0,20,20);
                        walls.add(wall);
                    }

                    case GameConstants.MUTOU -> {
                        wall = new TreeStem(i * 20.0,j * 20.0,20,20);
                        walls.add(wall);
                    }

                    case GameConstants.LEVEL -> {
                        wall = new Level(i * 20.0,j * 20.0,20,20);
                        walls.add(wall);
                    }
                }
            }
        }
    }
}
