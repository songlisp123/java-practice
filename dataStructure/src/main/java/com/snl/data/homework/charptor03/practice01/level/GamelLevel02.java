package com.snl.data.homework.charptor03.practice01.level;

import com.snl.data.homework.charptor03.practice01.CONSTANTS.GameConstants;
import com.snl.data.homework.charptor03.practice01.entity.Sprite;
import com.snl.data.homework.charptor03.practice01.entity.enmry.Enemy;
import com.snl.data.homework.charptor03.practice01.entity.goods.Coin;
import com.snl.data.homework.charptor03.practice01.entity.player.Player;

public class GamelLevel02<T extends Sprite> extends AbstractLevel<T> {

    public GamelLevel02(Player player) {
        super(player);
        fillSprite();
    }

    private void fillEnmries() {
        Enemy t;
        var enmries = getEnmries();
        t =  new Enemy(300,40,
                GameConstants.squreWidth,GameConstants.squreHeight);
        enmries.add(t);

        t =  new Enemy(100,100,
                GameConstants.squreWidth,GameConstants.squreHeight);
        enmries.add(t);

        t =  new Enemy(150,200,
                GameConstants.squreWidth,GameConstants.squreHeight);
        enmries.add(t);

        t =  new Enemy(360,210,
                GameConstants.squreWidth,GameConstants.squreHeight);
        enmries.add(t);

        t =  new Enemy(400,36,
                GameConstants.squreWidth,GameConstants.squreHeight);
        enmries.add(t);
    }

    private void fillCoins() {
        Coin t;
        var coins = getCoins();
        t = new Coin(125,300,
                GameConstants.COIN_WEIGHT,GameConstants.COIN_HEIGHT);
        coins.add(t);

        t = new Coin(360,270,
                GameConstants.COIN_WEIGHT,GameConstants.COIN_HEIGHT);
        coins.add(t);

        t = new Coin(321,89,
                GameConstants.COIN_WEIGHT,GameConstants.COIN_HEIGHT);
        coins.add(t);
    }

    @Override
    public void fillSprite() {
        fillEnmries();
        fillCoins();
    }
}
