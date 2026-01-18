package com.snl.data.homework.charptor03.practice01.state;

import com.snl.data.homework.charptor03.practice01.Music;
import com.snl.data.homework.charptor03.practice01.entity.player.Player;
import com.snl.data.homework.charptor03.practice01.level.levelwrapper.AbstractLevelWrapper;
import com.snl.data.homework.charptor03.practice01.level.levelwrapper.GameLevelImplement;

import java.util.logging.Logger;

public final class GameState {

    public static boolean stopping;
    boolean finished;
    boolean losing;
    boolean hasBeenBooted;
    AbstractLevelWrapper gameLevel;
    final Player player;

    public static Logger logger = Logger.getLogger("game");

    public GameState(GameLevelImplement gameLevel) {
        this(gameLevel,null);
    }

    public GameState(Player player) {
        this(null,player);
    }

    public GameState(AbstractLevelWrapper gameLevel, Player player) {
        stopping = false;
        losing = false;
        finished = false;
        hasBeenBooted = false;
        this.gameLevel = gameLevel;
        this.player = player;
        Music.backGround();
    }

    public void update() {
        if (gameLevel.isCrash()) {
            logger.warning("玩家碰撞xxxxx");
            //玩家碰触到敌人
            //减少玩家生命点数
            player.decreaseLifePoint(5);
            //判断该玩家的生命点数是否为零
            if (player.getLifePoints()  <= 0) {
                //判断玩家生命是否小于0
                if (player.getLife() <= 0) {
                    losing = true;
                    finished = false;
                    logger.warning("玩家死亡，游戏结束");
                }else {
                    //否则玩家生命减一
                    player.decreaseLife();
                    //发生碰撞,重置状态
                    player.resetLifePoints();
                    reset();
                }
                return;
            }
        }

        if (gameLevel.completed()) {
            //如果通关，有两种情况，第一种：当前关卡不是最后一关
            //事实上，这个程序有bug，因为game线程会在指定的一帧里面连续调用这个东西
            if (!gameLevel.hasNext() && !finished) {
                //如果最后一关，并且当前finish标志没有设置为true
                //如果全部通关
                finished = true;
                losing = false;
                logger.warning("恭喜通关游戏");
                return;
            }
            //否则进入到下一关卡
            logger.info("当前关卡通关,进入到下一关");
            gameLevel.next();
            reset();
        }
    }


    private void reset() {
        gameLevel.reset();
        hasBeenBooted = false;
    }

    public boolean isHasBeenBooted() {
        return hasBeenBooted;
    }

    public boolean isLosing() {
        return losing;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isStopping() {
        return stopping;
    }

    public void setHasBeenBooted(boolean hasBeenBooted) {
        this.hasBeenBooted = hasBeenBooted;
    }
}
