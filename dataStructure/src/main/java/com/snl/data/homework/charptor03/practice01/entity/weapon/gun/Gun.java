package com.snl.data.homework.charptor03.practice01.entity.weapon.gun;

import com.snl.data.homework.charptor03.practice01.entity.Group;
import com.snl.data.homework.charptor03.practice01.entity.booms.Boom;
import com.snl.data.homework.charptor03.practice01.entity.booms.BoomGroup;

import java.awt.*;

public abstract class Gun implements GunWeapon {


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

    private long shootTime;
    private boolean isReload;

    /**
     * 子弹袋
     */
    private BoomGroup group;

    public Gun(int maxBullets) {
        group = new BoomGroup(maxBullets);
    }

    public Gun(double originShootSpeed, double originHearingRangle, int originRecoil,
               double originKillDamage, String name,int maxBullets) {
        this.originShootSpeed = originShootSpeed;
        this.currentShootSpeed = originShootSpeed;

        this.originHearingRangle = originHearingRangle;
        this.hearingRangle = originHearingRangle;

        this.originKillDamage = originKillDamage;
        this.killDamage = originKillDamage;

        this.originRecoil = originRecoil;
        this.currentRecoil = originRecoil;
        this.name = name;

        group = new BoomGroup(maxBullets);
    }

    @Override
    public int getBullets() {
        return group.size();
    }

    @Override
    public int maxBullets() {
        return group.getBoomMaxSize();
    }

    @Override
    public void shoot(Boom boom) {
        group.add(boom);
        System.err.println("枪械射击");
    }

    public boolean isEmpty() {
        return group.isEmpty();
    }

    @Override
    public void render(Graphics g) {
        group.render(g);
    }

    @Override
    public void update() {
        //空实现
    }

    public void update(double delta, Group aGroup, Group destory, Group wall) {
        group.update(delta,aGroup,destory,wall);
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
}
