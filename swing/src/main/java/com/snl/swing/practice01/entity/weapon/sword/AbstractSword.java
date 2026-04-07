package com.snl.swing.practice01.entity.weapon.sword;

import com.snl.swing.practice01.Music;
import com.snl.swing.practice01.article.Article;
import com.snl.swing.practice01.entity.Group;
import com.snl.swing.practice01.entity.GroupImplement;
import com.snl.swing.practice01.entity.Sprite;
import com.snl.swing.practice01.entity.enmry.Enemy;
import com.snl.swing.practice01.entity.goods.AbstractGoods;
import com.snl.swing.practice01.entity.goods.LifeGoods;
import com.snl.swing.practice01.state.InputState;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 抽象剑维护了名字，伤害，持久性，攻速等剑属性
 * 维护了攻击时的动作
 */
public abstract class AbstractSword extends Sprite implements SwordWeapon {

    //剑的名字
    private String name;
    /**
     * 原始伤害
     */
    private double originKillDamage;
    /**
     * 当前伤害
     */
    private double currentKillDamage;
    /**
     * 耐久度
     */
    private double currentDurability;

    /**
     * 原始攻速
     */
    private double originalSpeed;

    /**
     * 当前攻速
     */
    private double currentSpeed;

    /**
     * 此武器的抽象路径
     */
    private Shape shape;
    /**
     * 以下是攻击时的动作
     */
    private boolean attacked;
    //当前攻击时间
    private long attackTime;
    //攻击持续时间
    private final long attackDurityTime = 150L;
    // 当前旋转角度（弧度）
    private double attackAngle = 0;
    // 最大攻击角度（90°）
    private static final double MAX_ATTACK_ANGLE = Math.PI;
    //武器的粒子效果
    private Group<Article> smokes;
    //残影效果
    private List<SwordAfterImage> list = new ArrayList<>();
    //武器的颜色
    private Color color;

    public AbstractSword(double xPos, double yPos, int WEIGHT, int HEIGHT, double originKillDamage,
                         double originalSpeed, double currentDurability)
    {
        super(xPos, yPos, WEIGHT, HEIGHT);
        this.originKillDamage = originKillDamage;
        this.currentKillDamage = originKillDamage;
        this.currentDurability = currentDurability;
        this.originalSpeed = originalSpeed;
        this.currentSpeed = originalSpeed;
        this.color = Color.magenta;
        smokes = new GroupImplement<>();
    }

    @Override
    public void render(Graphics g) {
        this.paint(g,null);
    }

    /**
     * 攻击时的剑的动作
     *
     * @param xPos    玩家的位置
     * @param yPos    和玩家的位置相关
     * @param agroup  敌人组
     * @param destory 毁灭组
     * @param wall    墙壁组
     * @param goods
     */
    public void update(double xPos, double yPos, double delta, Group agroup, Group destory, Group wall, Group<AbstractGoods> goods) {
        setxPos(xPos);
        setyPos(yPos);
        long now = System.currentTimeMillis();
        if(attacked) {
            if (list.size() > 12)
                //如果残影的数量超过最大值,移除第一个
                list.removeFirst();
            //添加残影
            list.add(new SwordAfterImage(
                    xPos,yPos,attackAngle
            ));
            long e = now - attackTime;
            //获取进度条
            double progress = Math.min(1.0, (double) e / attackDurityTime);
//            System.out.println("progress = " + progress);
            //睡着角度变化
            /**
             * 注意 ！以下都是 ai 完成的
             * TODO 以后再学
             */
            //缓动
            progress = 1 - Math.pow(1-progress,3);
            attackAngle = MAX_ATTACK_ANGLE * progress;
            if (e >= attackDurityTime) {
                attacked = false;
                attackAngle = MAX_ATTACK_ANGLE;
            }
        }else {
            attackAngle *= 0.8;
        }
        if (!list.isEmpty()) {
            var iterator = list.iterator();
            while (iterator.hasNext()) {
                SwordAfterImage next = iterator.next();
                next.alpha -= 0.035f;
                if (next.alpha <= 0) {
                    iterator.remove();
                }
            }
        }
        //更新粒子
        smokes.update(0);
        //判断是否碰撞敌人
        //判断与敌人的状态
        Collection data = agroup.getData(); //敌人数据

        //判断与敌人的状态
        Iterator<Sprite> iterator;
        for (iterator = data.iterator();iterator.hasNext();)
        {
            //遍历组中元素判断是否与炸弹碰撞
            Sprite next = iterator.next();
            if (this.isCrash(next))
            {
                //设置两者的活动状态
                System.out.println("相撞");
                ((Enemy)next).decreaseLifePoint(currentKillDamage);
                if (((Enemy) next).getLifePoints() == 0 ){
                    next.setDead(true);
                    destory.add(next);
                    //敌人死亡，创建掉落物品
                    var p =
                            LifeGoods.getInstance(next.getxPos(),next.getyPos(),
                                    next.getWEIGHT(), next.getHEIGHT(),12.5);
                    goods.add(p);
                }
            }
        }

        //判断与墙壁的状态
        //判断与墙壁的相对位置
//        data = wall.getData();
//        for (iterator = data.iterator();iterator.hasNext();) {
//            //遍历组中元素判断是否与炸弹碰撞
//            Sprite next = iterator.next();
//            if (this.isCrash(next))
//            {
//                Music.bulletsCrashWall();
//            }
//        }
    }

    @Override
    public void update(double delta, InputState state) {
        //空实现
    }

    @Override
    public void attack(InputState state) {
        attacked = true;
        attackTime = System.currentTimeMillis();
        attackAngle = 0;
        Music.swingSword();
    }

    @Override
    public void paint(Graphics g, InputState state) {
        //todo
        Graphics2D g2 = (Graphics2D) g.create();
        //绘制残影
        for (SwordAfterImage img : list) {
            var gImg = (Graphics2D)g2.create();
            gImg.setComposite(
                    AlphaComposite.getInstance(
                            AlphaComposite.SRC_OVER,
                            img.alpha
                    )
            );
            gImg.setPaint(new Color(color.getRed(), color.getGreen(), color.getBlue(), getColor().getAlpha()));
            gImg.rotate(-img.currentAngle, img.xPos, img.yPos);
            gImg.fill(shape);
            gImg.dispose();
        }
        g2.setPaint(color);
        g2.rotate(-attackAngle,getxPos(),getyPos());
        g2.fill(shape);
        //绘制粒子
        smokes.render(g);
        //去除引用
        g2.dispose();
    }

    public void setName(String name) {
        this.name = name;
    }

    public Group<Article> getSmokes() {
        return smokes;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public void setSmokes(Group<Article> smokes) {
        this.smokes = smokes;
    }

    @Override
    public String getInfo() {
        return "[%s]:伤害：%.2f,攻速:%.2fm/s,耐久力：%.2f%%".formatted(name,
                currentKillDamage,currentSpeed,currentDurability);
    }

    @Override
    public double getKillDamage() {
        return currentKillDamage;
    }

    @Override
    public double getOriginalDamage() {
        return originKillDamage;
    }

    @Override
    public void addDamage(double added) {
        currentKillDamage += added;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getDurability() {
        return currentDurability;
    }

    @Override
    public double getSpeed() {
        return currentSpeed;
    }

    public Shape getShape() {
        return shape;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    private class SwordAfterImage {
        double xPos;
        double yPos;
        //当前角度
        double currentAngle;
        //当前alpha
        float alpha;

        public SwordAfterImage(double xPos, double yPos, double currentAngle) {
            this.xPos = xPos;
            this.yPos = yPos;
            this.currentAngle = currentAngle;
            this.alpha = 1.0f;
        }
    }
}
