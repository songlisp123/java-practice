package com.snl.data.homework.charptor03.practice01.level;

import com.snl.data.homework.charptor03.practice01.Music;
import com.snl.data.homework.charptor03.practice01.entity.*;
import com.snl.data.homework.charptor03.practice01.entity.enmry.Enemy;
import com.snl.data.homework.charptor03.practice01.entity.goods.Coin;
import com.snl.data.homework.charptor03.practice01.entity.player.Player;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;
import java.util.List;

import java.util.ArrayList;

public  class GameLevel {

    private Sprite player;
    private List<Sprite> enemies;
    private List<Sprite> coins;
    private Sprite door;
    private boolean completed;

    public GameLevel() {
        this(null);
    }

    public GameLevel(Sprite player) {
        this.player = player;
        enemies = new ArrayList<>();
        coins = new ArrayList<>();
        door = new Door(500,350,50,50);
        createEnemies();
        createCoins();
    }

    private void createCoins() {
        Sprite coin;
        coin = new Coin(250,300,20,20);
        coins.add(coin);
    }

    private void createEnemies() {
        //创建六个敌人
        Sprite enmey;
        enmey = new Enemy(50,50,20,20);
        enemies.add(enmey);

        enmey = new Enemy(100,100,20,20);
        enemies.add(enmey);

        enmey = new Enemy(150,150,20,20);
        enemies.add(enmey);
    }

    public List<Sprite> getEnemies() {
        return enemies;
    }

    public Sprite getPlayer() {
        return player;
    }

    /**
     * 回复到初始状态
     */
    public void reset() {
        for (Sprite sprite : enemies)
            sprite.reset();
        for (Sprite sprite : coins)
            sprite.reset();
        this.player.reset();
    }

    public void update(double delta, InputState state,int weight,int height) {
        for (Sprite sprite : enemies) {
            sprite.update(delta,state);
        }
        if (this.player.isTouchWall(weight, height)) {
            player.handleTouchWall(weight, height);
        }
        for (Sprite sprite : enemies) {
            if (sprite.isTouchWall(weight, height))
                sprite.handleTouchWall(weight, height);
        }

        for (Sprite sprite : coins)
            sprite.update(delta,state);
        this.player.update(delta,state);
        checkEat();
    }

    public boolean isCrash() {
        if (((Player)player).isCrash(enemies)) {
            System.err.println("发生碰撞");
            Music.beep();
            return true;
        }
        return false;
    }

    public void checkEat() {
        Sprite eat = ((Player) player).eat(coins);
        if (eat == null) {
            return;
        }
        coins.remove(eat);
        Music.beep();
    }

    public void repaint(Graphics g) {
        for (Sprite sprite : enemies) {
            sprite.paint(g);
        }
        for (Sprite sprite : coins)
            sprite.paint(g);
        this.player.paint(g);
        this.door.paint(g);
    }

    private void checkSucess() {
        if (coins.isEmpty())
            completed = true;
    }
}
