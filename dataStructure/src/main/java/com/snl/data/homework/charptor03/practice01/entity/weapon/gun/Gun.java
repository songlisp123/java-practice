package com.snl.data.homework.charptor03.practice01.entity.weapon.gun;

import com.snl.data.homework.charptor03.practice01.Music;
import com.snl.data.homework.charptor03.practice01.article.Smoke;
import com.snl.data.homework.charptor03.practice01.article.SmokeImplement;
import com.snl.data.homework.charptor03.practice01.entity.Group;
import com.snl.data.homework.charptor03.practice01.entity.GroupImplement;
import com.snl.data.homework.charptor03.practice01.entity.Sprite;
import com.snl.data.homework.charptor03.practice01.entity.booms.Boom;
import com.snl.data.homework.charptor03.practice01.entity.booms.BoomGroup;
import com.snl.data.homework.charptor03.practice01.entity.booms.TimerBoom;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import java.awt.*;

public abstract class Gun extends Sprite implements GunWeapon {

    //当前射速
    private double currentShootSpeed;
    //原始射速
    private  double originShootSpeed;
    //当前听力范围
    private double hearingRangle;
    //原始听力范围
    private  double originHearingRangle;
    //当前后坐力
    private int currentRecoil;
    //原始后坐力
    private int originRecoil;
    //该武器当前杀伤力
    private  double killDamage;
    //原始杀伤力
    private  double originKillDamage;

    //武器名字
    private String name;

    //射击时间
    private long shootTime;
    //是否正在装弹
    private boolean isReload;
    /**
     * 子弹袋
     */
    private BoomGroup group;
    //该武器的抽象路径
    private Shape shape;

    //弧度
    private double radius;

    /**
     * 枪械射击烟雾
     */
    private GroupImplement<Smoke> smokes;

    public Gun(double originShootSpeed, double originHearingRangle, int maxBullets,
               double originKillDamage, String name, int bullets) {
        group = new BoomGroup(maxBullets);
        smokes = new GroupImplement<>();
    }

    public Gun(double originShootSpeed, double originHearingRangle, int originRecoil,
               double originKillDamage, String name,int maxBullets,double x,double y) {
        this.originShootSpeed = originShootSpeed;
        this.currentShootSpeed = originShootSpeed;

        this.originHearingRangle = originHearingRangle;
        this.hearingRangle = originHearingRangle;

        this.originKillDamage = originKillDamage;
        this.killDamage = originKillDamage;

        this.originRecoil = originRecoil;
        this.currentRecoil = originRecoil;
        this.name = name;
        //子弹
        group = new BoomGroup(maxBullets);
        //射击烟雾粒子效果
        smokes = new GroupImplement<>();
        setxPos(x);
        setyPos(y);
    }

    @Override
    public int getBullets() {
        return group.size();
    }

    @Override
    public int maxBullets() {
        return group.getBoomMaxSize();
    }

    public void shoot(Boom boom) {
        group.add(boom);
    }

    @Override
    public boolean isEmpty() {
        return group.isEmpty();
    }

    @Override
    public void render(Graphics g) {
        //渲染武器
        //TODO 渲染武器
        //更新子弹夹
        group.render(g);
        //绘制烟雾
        smokes.render(g);
    }

    public void update(double x,double y,
                       double delta, Group aGroup,
                       Group destory, Group wall) {
        //更新武器
        update(x,y);
        //TODO
        //更新子弹
        group.update(delta,aGroup,destory,wall,killDamage);
        //更新烟雾
        smokes.update(delta);
    }

    public void update(double x, double y) {

    }

    @Override
    public void attack(InputState state) {
        if (isEmpty())
        {
            Music.emptyBullets();
            return;
        }
        shoot(createBoom(state));
        showSmoke();
    }

    public Boom createBoom(InputState state) {
        Boom boom;
        System.out.println("currentShootSpeed = " + currentShootSpeed);
        switch (state.direction) {
            case NORTH -> boom = new Boom(getxPos() + getWEIGHT() / 2.0 - 5
                    ,getyPos() + 10,
                    10,10, state.direction, 0,-currentShootSpeed);
            case SOUTH -> boom = new Boom(getxPos() + getWEIGHT() / 2.0 - 3
                    ,getyPos() + getHEIGHT(),
                    6,10, state.direction,0,currentShootSpeed);
            case WEST -> boom = new Boom(getxPos()
                    ,getyPos() + getHEIGHT() / 2.0 - 3,
                    10,6, state.direction,-currentShootSpeed,0);
            case EAST -> boom = new Boom(getxPos()
                    ,getyPos() + getHEIGHT() / 2.0 - 3,
                    10,6, state.direction,currentShootSpeed,0);
            default -> boom = new TimerBoom(getxPos() + getWEIGHT()
                    ,getyPos() + getHEIGHT() / 2.0 - 5,
                    10,10, state.direction,currentShootSpeed);
        }
        return boom;
    }

    public void showSmoke() {
        for (int i=0;i<500;i++)
            smokes.add(new SmokeImplement(getxPos(),getyPos(),
                    10,10,Color.lightGray));
    }

    @Override
    public void update(double delta, InputState state) {
        //TODO 更新枪械
        throw new UnsupportedOperationException("未知操作");
    }

    @Override
    public void paint(Graphics g, InputState state) {
        //空实现
        throw new UnsupportedOperationException("未知操作");
    }

    @Override
    public double shootSpeed() {
        return currentShootSpeed;
    }

    @Override
    public double originShootSpeed() {
        return originShootSpeed;
    }

    @Override
    public double hearingrange() {
        return hearingRangle;
    }

    @Override
    public double originHearingrange() {
        return originHearingRangle;
    }

    @Override
    public int recoil() {
        return currentRecoil;
    }

    @Override
    public int originRecoil() {
        return originRecoil;
    }

    @Override
    public double getKillDamage() {
        return killDamage;
    }

    @Override
    public double getOriginalDamage() {
        return originKillDamage;
    }

    @Override
    public void addDamage(double added) {
        killDamage += added;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getInfo() {
        return "[%s]:枪管直径5mm,杀伤力：%.2f,听力范围: %.2fm,射速:%.2fm/s,适合高精尖暗杀".formatted(getName(),
                getKillDamage(),hearingrange(),shootSpeed());
    }

    public boolean isReload() {
        return isReload;
    }

    public long getShootTime() {
        return shootTime;
    }

    public void setShootTime(long shootTime) {
        this.shootTime = shootTime;
    }

    public void setReload(boolean reload) {
        isReload = reload;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public Shape getShape() {
        return shape;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
