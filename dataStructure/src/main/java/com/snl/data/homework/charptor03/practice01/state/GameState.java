package com.snl.data.homework.charptor03.practice01.state;

import com.snl.data.homework.charptor03.practice01.entity.player.Player;
import com.snl.data.homework.charptor03.practice01.level.levelwrapper.AbstractLevelWrapper;
import com.snl.data.homework.charptor03.practice01.level.levelwrapper.GameLevelImplement;

public final class GameState {

    boolean stopping;
    boolean finished;
    boolean losing;
    boolean hasBeenBooted;
    AbstractLevelWrapper gameLevel;
    Player player;

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
    }

    public void update() {
        if (gameLevel.isCrash()) {
            //玩家碰触到敌人
            //减少玩家生命
            player.decreaseLife();
            //判断玩家生命是否小于0
            if (player.getLife() <= 0) {
                losing = true;
                finished = false;
                return;
            }
            //发生碰撞,重置状态
            reset();
        }

        if (gameLevel.completed()) {
            //如果通关，有两种情况，第一种：当前关卡不是最后一关
            if (gameLevel.hasNext()) {
                gameLevel.next();
                reset();
            }else {
                //如果全部通关
                finished = true;
                losing = false;
            }
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
