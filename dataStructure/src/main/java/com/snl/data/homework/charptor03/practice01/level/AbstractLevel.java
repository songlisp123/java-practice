package com.snl.data.homework.charptor03.practice01.level;

import com.snl.data.homework.charptor03.practice01.CONSTANTS.GameConstants;
import com.snl.data.homework.charptor03.practice01.Music;
import com.snl.data.homework.charptor03.practice01.entity.Door;
import com.snl.data.homework.charptor03.practice01.entity.GroupImplement;
import com.snl.data.homework.charptor03.practice01.entity.Sprite;
import com.snl.data.homework.charptor03.practice01.entity.enmry.Enemy;
import com.snl.data.homework.charptor03.practice01.entity.goods.Coin;
import com.snl.data.homework.charptor03.practice01.entity.player.Player;
import com.snl.data.homework.charptor03.practice01.entity.wall.Wall;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;

public abstract class AbstractLevel<T extends Sprite> implements Level<T> {

    private static Player player;
    private GroupImplement<Enemy> enmries;
    private GroupImplement<Coin> coins;
    private GroupImplement<Coin> eated;
    private GroupImplement<Sprite> destoryedSprite;
    private GroupImplement<Wall> walls;
    private Door door;
    private boolean showDoor;
    private boolean isReating;

    public AbstractLevel() {
        initData();
    }

    private void initData() {
        enmries = new GroupImplement<>();
        coins = new GroupImplement<>();
        eated = new GroupImplement<>();
        destoryedSprite = new GroupImplement<>();
        walls = new GroupImplement<>();
        showDoor = false;
        //填充精灵
    }

    // ********************  重置  ***************************//
    @Override
    public void reset() {
        //发生碰撞该怎么办？
        coins.addAll(eated);
        enmries.addAll(destoryedSprite);
        player.reset();
        enmries.reset();
        coins.reset();
        if (showDoor)
            showDoor = false;
    }


    // ********************  渲染  ***************************//
    @Override
    public void render(Graphics g) {
        walls.render(g);
        player.paint(g);
        enmries.render(g);
        coins.render(g);
        if (showDoor)
            door.paint(g);
    }

    // ********************  更新  ***************************//
    @Override
    public void update(double delta, InputState state, int weight, int height) {
        player.update(delta,state,enmries,destoryedSprite,walls);
        enmries.update(delta,walls);
        coins.update(delta);
        //实现有待完善
        if (player.isBeyondScene(weight,height))
            player.handleBeyondScene(weight,height);
        //如果玩家碰到墙壁
        //判断元素是否消失
        eatCoin();
        //检查是否成功
        checkEmpty();
        //判断音乐
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
        player.addScore(GameConstants.GAINT);
        eated.add(eat);
        return eat;
    }

    /**
     * 判断玩家是否全部搜集硬币
     */
    private void checkEmpty() {
        if (coins.isEmpty())
            showDoor = true;
    }

    /**
     * 判断是否与敌人相撞
     * @return 如果发生碰撞则返回 {@code true}，否则返回 {@code false}
     */
    public boolean isCrash() {
        return (player.isTouch(enmries.getData()));
    }


    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * 是否完成当前关卡
     * @return 是则返回 {@code true} ,否则返回 {@code false}
     * @apiNote 判断逻辑是否出现传送门以及玩家是否触碰到床送门
     */
    public boolean completed() {
        return showDoor && player.isCrash(door);
    }

    protected GroupImplement<Enemy> getEnmries() {
        return enmries;
    }

    protected GroupImplement<Coin> getCoins() {
        return coins;
    }

    protected GroupImplement<Wall> getWalls() {
        return walls;
    }

    protected void setPlayer(Player player) {
        AbstractLevel.player = player;
    }

    public void setDoor(Door door) {
        this.door = door;
    }

}
