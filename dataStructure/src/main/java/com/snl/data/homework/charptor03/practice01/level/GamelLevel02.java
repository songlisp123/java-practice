package com.snl.data.homework.charptor03.practice01.level;

import com.snl.data.homework.charptor03.practice01.Music;
import com.snl.data.homework.charptor03.practice01.entity.Door;
import com.snl.data.homework.charptor03.practice01.entity.GroupImplement;
import com.snl.data.homework.charptor03.practice01.entity.Sprite;
import com.snl.data.homework.charptor03.practice01.entity.enmry.Enemy;
import com.snl.data.homework.charptor03.practice01.entity.goods.Coin;
import com.snl.data.homework.charptor03.practice01.entity.player.Player;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;

public class GamelLevel02<T extends Sprite> implements Level<T> {

    private Player player;
    private GroupImplement<Enemy> enmries;
    private GroupImplement<Coin> coins;
    private GroupImplement<Coin> eated;

    private GroupImplement<T> destoryedSprite;

    private Door door;
    private boolean showDoor;

    public GamelLevel02(Player player) {
        this.player = player;
        initData();
    }


    private void initData() {
        enmries = new GroupImplement<>();
        coins = new GroupImplement<>();
        eated = new GroupImplement<>();
        door = new Door(400,400,25,25);
        destoryedSprite = new GroupImplement<>();
        showDoor = false;
        //先硬编码
        //填充敌人
        fillEnmries();
        //填充硬币
        fillCoins();
    }

    private void fillEnmries() {
        Enemy t;
        t =  new Enemy(300,40,20,20);
        enmries.add(t);

        t =  new Enemy(100,100,20,20);
        enmries.add(t);

        t =  new Enemy(150,200,20,20);
        enmries.add(t);

        t =  new Enemy(360,210,20,20);
        enmries.add(t);

        t =  new Enemy(400,36,20,20);
        enmries.add(t);
    }

    private void fillCoins() {
        Coin t;
        t = new Coin(125,300,10,10);
        coins.add(t);

        t = new Coin(360,270,10,10);
        coins.add(t);

        t = new Coin(321,89,10,10);
        coins.add(t);
    }

    @Override
    public void reset() {
        //发生碰撞该怎么办？
        coins.addAll(eated);
        enmries.addAll(destoryedSprite);
        player.reset();
        enmries.reset();
        coins.reset();
        destoryedSprite.reset();
        if (showDoor)
            showDoor = false;
    }

    @Override
    public void render(Graphics g) {
        player.paint(g);
        enmries.render(g);
        coins.render(g);
        if (showDoor)
            door.paint(g);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void update(double delta, InputState state,int weight,int height) {
        player.update(delta,state,enmries,destoryedSprite);
        enmries.update(delta);
        coins.update(delta);
        if (player.isTouchWall(weight,height))
            player.handleTouchWall(weight,height);
        //判断元素是否消失
        eatCoin();
        //玩家与物体相撞逻辑
        //检查是否成功
        checkEmpty();
    }

    /**
     * 判断是否与敌人相撞
     * @return 是装机，否则我不是
     */
    public boolean isCrash() {
        return (player.isCrash(enmries.getData()));
    }

    /**
     * 吃硬币小程序
     */
    private Coin eatCoin() {
        Coin eat = (Coin) player.eat(coins.getData());
        if (eat == null)
            //如果没有吃到硬币
            return null;
        Music.beep();
        eated.add(eat);
        return eat;
    }

    @Override
    public Level<T> next() {
        //TODO
        return null;
    }

    private void checkEmpty() {
        if (coins.isEmpty())
            showDoor = true;
    }

    public boolean completed() {
        if (showDoor)
            //吃完硬币
            return player.isCrash(door);
        return false;
    }
}
