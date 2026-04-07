package com.snl.swing.practice01.level;

import com.snl.swing.practice01.CONSTANTS.GameConstants;
import com.snl.swing.practice01.Music;
import com.snl.swing.practice01.camary.Camera;
import com.snl.swing.practice01.entity.Door;
import com.snl.swing.practice01.entity.GroupImplement;
import com.snl.swing.practice01.entity.Sprite;
import com.snl.swing.practice01.entity.enmry.Enemy;
import com.snl.swing.practice01.entity.goods.AbstractGoods;
import com.snl.swing.practice01.entity.goods.Coin;
import com.snl.swing.practice01.entity.player.Player;
import com.snl.swing.practice01.entity.wall.Wall;
import com.snl.swing.practice01.state.InputState;

import java.awt.*;
import java.awt.geom.AffineTransform;

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
    private GroupImplement<AbstractGoods> goods;

    private Camera camera;

    //更新颜色
    long start;
    long span = 500L;

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
        camera = new Camera(GameConstants.Weight,GameConstants.Height);
        //掉落物品
        goods = new GroupImplement<>();
        //初始化时间
        start = System.currentTimeMillis();
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
        //重置物品
        if (!goods.isEmpty()) {
            goods.clear();
        }
        if (showDoor)
            showDoor = false;
    }


    // ********************  渲染  ***************************//
    @Override
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        AffineTransform old = g2.getTransform();
        g2.translate(-camera.x,-camera.y);
        //绘制
        player.paint(g2);
        enmries.render(g2);
        walls.render(g2);
        coins.render(g2);
        goods.render(g2);
        if (showDoor)
            door.paint(g2);
        g2.setTransform(old);
        g2.dispose();
    }

    // ********************  更新  ***************************//
    @Override
    public void update(double delta, InputState state, int weight, int height) {
        player.update(delta,state,enmries,destoryedSprite,walls,goods);
        enmries.update(delta,walls);
        coins.update(delta);
        walls.update(delta);
        //实现有待完善
        if (player.isBeyondScene(weight,height))
            player.handleBeyondScene(weight,height+400);
        //更新掉落物品
        goods.update(delta,walls);
        //判断元素是否消失
        eatCoin();
        //检查是否成功
        checkEmpty();
        //判断玩家是否相撞
        camera.y = player.getyPos() + player.getHEIGHT() / 2.0 - camera.height / 2.0;
        camera.y = Math.max(0, camera.y);
        camera.y = Math.min(camera.y,GameConstants.MAPHeight- camera.height);

//        更新颜色,暂时不实现
//        long now = System.currentTimeMillis();
//        if (now - start >= span)
//        {
//            //更新颜色
//            InputState.changeIngColor = !InputState.changeIngColor;
//            start = now;
//        }
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
