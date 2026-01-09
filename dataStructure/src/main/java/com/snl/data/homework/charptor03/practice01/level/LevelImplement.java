package com.snl.data.homework.charptor03.practice01.level;

import com.snl.data.homework.charptor03.practice01.CONSTANTS.GameConstants;
import com.snl.data.homework.charptor03.practice01.entity.*;
import com.snl.data.homework.charptor03.practice01.entity.enmry.Enemy;
import com.snl.data.homework.charptor03.practice01.entity.goods.Coin;
import com.snl.data.homework.charptor03.practice01.entity.player.Player;
import com.snl.data.homework.charptor03.practice01.entity.wall.Wall;

import java.awt.*;

public class LevelImplement<T extends Sprite> extends AbstractLevel<T> {

    public LevelImplement(Player player) {
        super(player);
    }

    @Override
    public void fillSprite() {
        var enmries = getEnmries();
        var coins = getCoins();
        var walls = getWalls();
        Enemy enemy;
        Coin coin;
        Wall wall;
        int i,j;
        Character[][] level_1 = JumpLevelMap.level_4;
        for (j=0;j<level_1.length;j++) {
            //行,y坐标
            for (i = 0;i<level_1[0].length;i++) {
                //x坐标
                char c = level_1[j][i];
                switch (c) {
                    case GameConstants.WALL -> {
                        wall = new Wall(i * 20.0,j * 20.0,20,20,Color.lightGray);
                        walls.add(wall);
                    }
                    case GameConstants.COIN -> {
                        coin = new Coin(i * 20.0,j * 20.0,20,20);
                        coins.add(coin);
                    }
                    case GameConstants.ENMRY -> {
                        enemy = new Enemy(i * 20.0,j * 20.0,20,20);
                        enmries.add(enemy);
                    }
                    case GameConstants.PLAYER -> {
                        var player = new Player(i * 20.0,j * 20.0,20,20);
                        setPlayer(player);
                    }
                }
            }
        }

    }

}
