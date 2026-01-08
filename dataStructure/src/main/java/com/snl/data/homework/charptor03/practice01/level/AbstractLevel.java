package com.snl.data.homework.charptor03.practice01.level;

import com.snl.data.homework.charptor03.practice01.Music;
import com.snl.data.homework.charptor03.practice01.entity.Door;
import com.snl.data.homework.charptor03.practice01.entity.Group;
import com.snl.data.homework.charptor03.practice01.entity.GroupImplement;
import com.snl.data.homework.charptor03.practice01.entity.Sprite;
import com.snl.data.homework.charptor03.practice01.entity.enmry.Enemy;
import com.snl.data.homework.charptor03.practice01.entity.goods.Coin;
import com.snl.data.homework.charptor03.practice01.entity.player.Player;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;

public abstract class AbstractLevel<T extends Sprite> implements Level<T> {

    private Player player;
    private GroupImplement<Enemy> enmries;
    private GroupImplement<Coin> coins;
    private GroupImplement<Coin> eated;
    private GroupImplement<Sprite> destoryedSprite;

    private Door door;
    private boolean showDoor;

    public AbstractLevel() {
        this(null);
    }

    public AbstractLevel(Player player) {
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
        //填充精灵
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
    public void update(double delta, InputState state, int weight, int height) {
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
     * 吃硬币小程序
     */
    private Coin eatCoin() {
        Coin eat = (Coin) player.eat(coins.getData());
        if (eat == null) {
            //如果没有吃到硬币
            return null;
        }
        Music.beep();
        eated.add(eat);
        return eat;
    }

    private void checkEmpty() {
        if (coins.isEmpty())
            showDoor = true;
    }

    /**
     * 判断是否与敌人相撞
     * @return 是装机，否则我不是
     */
    public boolean isCrash() {
        return (player.isCrash(enmries.getData()));
    }

    public boolean completed() {
        if (showDoor)
            //吃完硬币
            return player.isCrash(door);
        return false;
    }

    protected GroupImplement<Enemy> getEnmries() {
        return enmries;
    }

    protected GroupImplement<Coin> getCoins() {
        return coins;
    }
}
