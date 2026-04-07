package com.snl.swing.practice01.entity.player;

import com.snl.swing.practice01.CONSTANTS.GameConstants;
import com.snl.swing.practice01.Music;
import com.snl.swing.practice01.entity.Group;
import com.snl.swing.practice01.entity.Sprite;
import com.snl.swing.practice01.entity.goods.AbstractGoods;
import com.snl.swing.practice01.entity.goods.LifeGoods;
import com.snl.swing.practice01.entity.weapon.Weapon;
import com.snl.swing.practice01.entity.weapon.gun.*;
import com.snl.swing.practice01.entity.weapon.knife.AbstractKnife;
import com.snl.swing.practice01.entity.weapon.knife.SmallKnife;
import com.snl.swing.practice01.entity.weapon.sword.AbstractSword;
import com.snl.swing.practice01.entity.weapon.sword.LightSaber;
import com.snl.swing.practice01.entity.weapon.sword.SmallSword;
import com.snl.swing.practice01.state.InputState;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class Player extends Sprite implements PlayerAction {

    /**
     * 颜色
     */
    private Color color;
    private Point2D leftUpConor;
    private Point2D rightUpConor;
    private Point2D leftDownConor;
    private Point2D rightDownConor;
    /**
     * x轴速度
     */
    private final double SPEED = 3.5;
    /**
     * 子弹袋最大容积
     */
    private final int boomCounts = 6;

    /**
     * y轴速度
     */
    private  double Y_SPEED;
    /**
     * 是否落入到地面？？
     */
    private boolean onGround;

    /**
     * 玩家生命
     */
    private int life;

    /**
     * 玩家得分
     */
    private int score;

    //还有其他的属性……
    /**
     * 炸弹背包
     */
    //TODO
    private Group<Sprite> Grenade;

    /**
     * 当前枪械
     */
    private Weapon currentWeapon;

    /**
     * 对旧武器的引用
     */
    private Weapon pistol;

    /**
     * 初始化冲锋枪
     */
    private Weapon submachingGun;

    /**
     * 初始化狙击步枪
     */
    private Weapon sniperRifle;

    /**
     * 初始化突击步枪
     */
    private Weapon assaultRifle;

    public static Logger logger = Logger.getLogger("game");

    /**
     * 初始化剑
     */
    private AbstractSword sword;

    /**
     * 初始化光剑
     */
    private LightSaber lightSaber;

    /**
     * 玩家生命
     */
    private double lifePoints;

    /**
     * 玩家初始生命点数
     */
    private double originLifePoints;

    /**
     * 玩家的生命条框
     */
    private Shape shape;

    /**
     * 玩家生命槽
     */
    private Shape lifeShape;

    /**
     * 玩家与生命槽之间的距离
     */
    private final double GAP = 12;

    /**
     * 生命槽高度
     * @apiNote 生命槽宽度等于初始化生命值
     */
    private final int LIFE_HEIGHT = 15;

    private double textXPos;
    private double textYPos;
    private Color lifeColor;
    private boolean hasShing;
    private long startShing;
    private String string;
    private Point2D stringPoint2D;

    private AbstractKnife knife;

    //获取物品后的文字
    String goodInfo;
    private List<GoodInfo> goodInfos = new ArrayList<>();

    public Player() {
        super();
        initData();
    }

    public Player(double xPos, double yPos, int WEIGHT, int HEIGHT) {
        super(xPos, yPos, WEIGHT, HEIGHT);
        initData();
    }

    private void initData() {
        color = Color.CYAN;
        onGround = true;
        life = GameConstants.PLAYERLIFES;
        score = 0;
        //初始化武器
        assaultRifle = new AssaultRifle(30,"AK 47突击步枪");
        currentWeapon = assaultRifle; //初始化武器为突击步枪
        pistol = new Pistol(boomCounts,"小手枪");
        submachingGun = new SubmachineGun(60,"Mp40冲锋枪");
        sniperRifle = new SniperRifle(getxPos(),getyPos(),"98K 狙击步枪",15);
        //剑
        sword = new SmallSword(this.getxPos() +getWEIGHT() / 2.0,
                this.getyPos() + getHEIGHT() / 2.0, "手里剑");
        //光剑
        lightSaber = new LightSaber(this.getxPos() +getWEIGHT() / 2.0,
                this.getyPos() + getHEIGHT() / 2.0, "光剑");
        //刀
        knife = new SmallKnife(getxPos(),getyPos(),"刀");
        lifePoints = 100f; // 初始化为100
        originLifePoints = lifePoints;
        //初始化生命槽
        stringPoint2D = new Point2D.Double(getxPos() ,getyPos()-40);
        string = "";
        goodInfo = "";
        //计算生命槽
        calculateLife();
        //重置位置
        resetPoint();
    }

    private void calculateLife() {
        double left_x = getxPos() - (originLifePoints - getWEIGHT()) / 2;
        double left_y = getyPos() - GAP - LIFE_HEIGHT;
        shape = new Rectangle2D.Double(left_x,left_y,originLifePoints,LIFE_HEIGHT);
        var s = new Rectangle2D.Double(left_x,left_y,lifePoints,LIFE_HEIGHT);
        var temp = new Rectangle2D.Double(left_x,left_y,0,0);
        lifeShape = s;
        //计算渲染文本值
        textXPos = left_x + originLifePoints + 10;
        textYPos = left_y;
        //渐变因子
        double f = lifePoints / originLifePoints;
        lifeColor = (f <= 0.5) ? Color.RED:Color.GREEN;
        //频繁闪烁
        if (f <= 0.5 && !hasShing) {
            startShing = System.currentTimeMillis();
            hasShing = true;
            lifeShape = temp;
        }
        long now = System.currentTimeMillis();
        if (hasShing && now - startShing >= Math.pow(f,2)*1_000) {
            hasShing = false;
            lifeShape = s;
        }
    }

    //*********************  更新  *******************//
    @Override
    public void update(double delta, InputState state) {

    }

    /**
     * 更新玩家
     *
     * @param delta   时间间隔
     * @param state   输入状态
     * @param aGroup  敌人组
     * @param destory 摧毁的精灵集合
     * @param wall    墙壁组
     * @param goods
     */
    public void update(double delta, InputState state, Group aGroup, Group destory, Group wall, Group<AbstractGoods> goods) {
        if (state.up && onGround) {
            //如果当玩家处于地面切向上按钮按下的时候
            Y_SPEED = -15;
            onGround = false;
            Music.jumping();
        }
        double dx = 0.0f;
        if (state.left) dx -= SPEED;
        if (state.right) dx += SPEED;

        Y_SPEED += GameConstants.GRAVITY * 0.15; //重力

        //先处理y
        movesY(Y_SPEED,wall);
        //处理x
        movesX(dx,wall);

        //攻击动作
        if (state.attackPressed) {
            attack(state);
        }
        changePosition(state);
        //更换武器
        changeWeapon();
        //更新武器
        updateWeapon(delta,state,aGroup,destory,wall,goods);
        //更新生命槽
        calculateLife();
        //更新弹药位置
        updateBooms();
        //更新文本
        updateGoodsInfo();
    }

    private void updateGoodsInfo() {
        if (goodInfos.isEmpty())
            return;
        //如果有下一个怎么办？？
        goodInfos.removeIf(GoodInfo::isDead);
    }

    private void updateBooms() {
        double x = getxPos();
        double y = getyPos();
        stringPoint2D = new Point2D.Double(x ,y-40);
        if (currentWeapon instanceof Gun gun) {
            int bullets = gun.getBullets();
            string = gun.getName()+": "+bullets;
        }else {
            string = "";
        }
    }

    private void updateWeapon(double delta, InputState state, Group aGroup, Group destory,
                              Group wall, Group goods)
    {
        //更新武器
        currentWeapon.update(this.getxPos() + getWEIGHT() / 2.0,
                this.getyPos() + getHEIGHT() / 2.0,
                delta,aGroup,destory,wall,goods);

        //判断敌人是否我空或者null
        if (aGroup != null && !aGroup.isEmpty()) {
            Collection<Sprite> data = aGroup.getData();
            for (Sprite s : data) {
                if (s.isCrash(this)) {
                    this.decreaseLifePoint(10);
                }
            }


//            BoomGroup group = null;
//            for (Sprite s : data) {
//                if (s instanceof AdvancedEnemy u) {
//                    group = u.getBoomGroup();
//                    break;
//                }
//            }
//
//            if (group == null)
//                return;
//            data = group.getData();
//            for (Sprite s : data) {
//                if (s.isCrash(this)) {
//                    //如果玩家碰到子弹
//                    this.decreaseLifePoint(10);
//                    s.setDead(true);
//                }
//            }
        }

        //判断是否吃到掉落物品
        handleGoods(goods);
    }

    private void handleGoods(Group goods) {
        if (goods == null || goods.isEmpty())
            return;
        Collection<AbstractGoods> data = goods.getData();
        Iterator<AbstractGoods> iterator;
        for (iterator = data.iterator();iterator.hasNext();)
        {
            //获取物品元素
            var g = iterator.next();
            //判断是否相撞
            if (this.isCrash(g))
            {
                //如果发生碰撞
                if (g instanceof LifeGoods l)
                {
                    //如果是恢复生命？？
                    double recovery = l.getRecovery();
                    addLifePoints(recovery);
                    goodInfo = String.format("恭喜获得 %.2f生命值",recovery);
                }
                g.setDead(true);
                GoodInfo info = new GoodInfo(goodInfo);
                goodInfos.add(info);
            }

        }
    }

    private void changeWeapon() {
        Weapon newWeapon = currentWeapon;

        switch (InputState.c) {
            case '1' -> newWeapon = assaultRifle;
            case '2' -> newWeapon = pistol;
            case '3' -> newWeapon = submachingGun;
            case '4' -> newWeapon = sniperRifle;
            case '5' -> newWeapon = sword;
            case '6' -> newWeapon = lightSaber;
            case '7' -> newWeapon = knife;
        }
        if (newWeapon != currentWeapon) {
            //如果用户更换武器
            currentWeapon =newWeapon;
            if (newWeapon == sword) {
                Music.drawTheSword();
            }
            else if (newWeapon instanceof Gun) {
                Music.changeGun();
            } else if (newWeapon == lightSaber) {
                Music.lightSaber();
            } else if (newWeapon == knife) {
                Music.smallKnife();
            }
        }
    }

    private void movesX(double dx,Group wall) {
        int steps = (int) Math.ceil(Math.abs(dx));
        double step = Math.signum(dx);

        for (int i=0;i<steps;i++) {
            move(step,0);
            if (isTouch(wall.getData())) {
                //如果发生碰撞
                move(-step,0);
                return;
            }
        }
    }

    /**
     * 垂直距离移动
     */
    private void movesY(double dy,Group wall) {
        //获取每次行走的步数
        int steps = (int) Math.ceil(Math.abs(dy));
        //判断方向，step为-1表示向上，step为1表示向下
        double step = Math.signum(dy);

        //逐步数修改状态
        for (int i=0;i<steps;i++) {
            move(0,step);
            if (isTouch(wall.getData())) {
                //如果发生碰撞，y轴速度变为0
                move(0,-step);
                Y_SPEED = 0;
                if (step > 0) {
                    //如果发生的碰撞代表着落入地面
                    onGround = true;
                }
                return;
            }
        }
    }

    @Override
    public void move(double xPos, double yPos) {
        double x = getxPos() + xPos;
        double y = getyPos() + yPos;
        super.setxPos(x);
        super.setyPos(y);
        resetPoint(x,y);
    }

    //*********************  绘制  *******************//
    @Override
    public void paint(Graphics g, InputState state) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2.setPaint(Color.WHITE);
        drawLine(g2);
        paintLifePoints(g2);
        paintBombs(g2);
        //绘制信息
        paintInfos(g2);
        //绘制武器
        currentWeapon.render(g2);
        g2.dispose();
    }

    private void paintInfos(Graphics g) {
        if (goodInfos.isEmpty())
            return;
        for (GoodInfo info :goodInfos)
            info.render(g);
    }

    private void paintBombs(Graphics2D g2) {
        g2.setStroke(new BasicStroke(2));
        g2.drawString(string, (int) stringPoint2D.getX(), (int) stringPoint2D.getY());
    }


    private void paintLifePoints(Graphics2D g2) {
        //TODO 如何绘制生命条？
        g2.setStroke(new BasicStroke(2));
        g2.draw(shape);
        g2.setColor(Color.green);
        g2.fill(lifeShape);
        g2.drawString("%.2f / %.2f%%".formatted(lifePoints,originLifePoints),
                (int) textXPos, (int) textYPos);
    }

    private void drawLine(Graphics2D g2) {
        //硬编码
        //顺时针画图
        g2.setStroke(new BasicStroke(2));
        g2.drawLine((int) leftUpConor.getX(), (int) leftUpConor.getY(),
                (int) rightUpConor.getX(), (int) rightUpConor.getY());
        g2.drawLine((int) rightUpConor.getX(), (int) rightUpConor.getY(),
                (int) rightDownConor.getX(), (int) rightDownConor.getY());
        g2.drawLine((int) rightDownConor.getX(), (int) rightDownConor.getY(),
                (int) leftDownConor.getX(), (int) leftDownConor.getY());
        g2.drawLine((int) leftDownConor.getX(), (int) leftDownConor.getY(),
                (int) leftUpConor.getX(), (int) leftUpConor.getY());
    }

    private void changePosition(InputState state) {
        double x = getxPos();
        double y = getyPos();
        if (state.left) {
            //左按键
//            point = new Point2D.Double()
            leftUpConor = new Point2D.Double(x - 4,y);
            rightUpConor = new Point2D.Double(x + getWEIGHT() - 4,y);
        }else {
            leftUpConor = new Point2D.Double(x,y);
            rightUpConor = new Point2D.Double(x + getWEIGHT() ,y);
        }
        if (state.right) {
            leftUpConor = new Point2D.Double(x + 4,y);
            rightUpConor = new Point2D.Double(x + getWEIGHT() + 4,y);
        }
    }

    @Override
    public void reset() {
        super.reset();
        resetPoint();
        //重置弹药/??大雾？？如果
    }

    //*************************  碰撞  ********************************//

    public <T extends Sprite> T eat(Collection<T> collection) {
        for (T sprite : collection)
        {
            if (super.isCrash(sprite)) {
                sprite.setDead(true);
                return sprite;
            }
        }
        return null;
    }

    @Override
    public boolean isTouch(Collection<? extends Sprite> sprites) {
        boolean touched =false;
        if (sprites == null || sprites.isEmpty() )
            return touched;
        for (Sprite sprite : sprites)
            if (isCrash(sprite))
            {
                touched = true;
                break;
            }
        return touched;
    }

    //************************* 处理触碰屏幕 *************************//
    @Override
    public void handleBeyondScene(int width,int height) {
        if (touchLeftBounder(0))
            setxPos(Math.max(0,getxPos()));
        if (touchUpBounder(0))
            setyPos(Math.max(0,getyPos()));
        if (touchRightBounder(width))
            setxPos(Math.min(width - getWEIGHT(),getxPos()));
        if (touchBottomBounder(height)) {
            onGround = true;
            setyPos(Math.min(height - getHEIGHT(), getyPos()));
        }
        resetPoint();
    }

    /**
     * 撞到左边界
     * @param x 左边界的值，对于屏幕来说是{@code 0}
     * @return 发生碰撞，返回{@code true} ,否则返回{@code false}
     */
    private boolean touchLeftBounder(double x) {
        return getxPos() <= x;
    }

    private boolean touchRightBounder(double weight) {
        return (getxPos()+getWEIGHT()) >= weight;
    }

    private boolean touchUpBounder(double y) {
        return getyPos() <= y;
    }

    private boolean touchBottomBounder(double height) {
        return (getyPos()+getHEIGHT()) >= height;
    }

    private void resetPoint() {
        double x = getxPos();
        double y = getyPos();
        resetPoint(x,y);
    }

    private void resetPoint(double x,double y) {
        leftUpConor = new Point2D.Double(x,y);
        leftDownConor = new Point2D.Double(x,y + getHEIGHT()); //左下角
        rightUpConor = new Point2D.Double(x + getWEIGHT(),y); //右上角
        rightDownConor = new Point2D.Double(x+getWEIGHT() , y + getHEIGHT()); //右下角
    }


    //**********************************  攻击动作  ***************************//

    @Override
    public void attack(InputState state) {
        currentWeapon.attack(state);
    }

    //**************************  生命系统  ************************//
    public int getLife() {
        return life;
    }

    public void decreaseLife() {
        this.life--;
    }

    public void increasedLife() {
        this.life++;
    }

    public double getLifePoints() {
        return lifePoints;
    }

    public void decreaseLifePoint (double decreased) {
        lifePoints -= decreased;
    }

    public void addLifePoints(double added) {
        lifePoints = Math.min(100,lifePoints + added);
    }

    public void resetLifePoints() {
        lifePoints = originLifePoints;
    }

    //******************************** 得分  *********************//

    public int getScore() {
        return score;
    }

    public void addScore(int added) {
        this.score += added;
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    //获取该玩家所有的武器
    public Weapon[] allWeapons() {
        return new Weapon[]{
                assaultRifle,pistol,submachingGun,sniperRifle,sword,lightSaber,knife
        };
    }

    class GoodInfo {
        //字符串
        String s;

        Font font = new Font("隶书",Font.PLAIN,20);
        long start;
        final long life = 1000L;

        public GoodInfo(String s) {
            this.s = s;
            start = System.currentTimeMillis();
        }

        public boolean isDead() {
            long now = System.currentTimeMillis();
            return now - start >= life;
        }

        public void render(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.cyan);
            g2.drawString(s, (float) getxPos(), (float) (getyPos() - 50));
            g2.dispose();
        }

    }
}
