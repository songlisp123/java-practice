package com.tedu.micro.demo.device;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tedu.micro.demo.device.dao.mapper.GunMapper;
import com.tedu.micro.demo.device.pojo.po.GunInfoPo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class DeviceTest {

    @Autowired
    private GunMapper gunMapper;

    @Test
    void insert() {
        GunInfoPo gunInfoPo = new GunInfoPo();
        gunInfoPo.setName("测试永强");
        gunInfoPo.setType(1);
        gunInfoPo.setStatus(10);
        gunInfoPo.setCreateTime(LocalDateTime.now());
        gunInfoPo.setUpdateTime(LocalDateTime.now());
        gunMapper.insert(gunInfoPo);
    }

    @Test
    void selectById() {
        GunInfoPo gunInfoPo = gunMapper.selectById(2);
        System.out.println(gunInfoPo);
    }

    @Test
    void getOneWhere() {
        QueryWrapper<GunInfoPo> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("id",1);
        GunInfoPo gunInfoPo = gunMapper.selectOne(objectQueryWrapper);
        System.out.println(gunInfoPo);
    }

    @Test
    void getIn() {
        QueryWrapper<GunInfoPo> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.in("id",1,2,3);
        for (GunInfoPo gunInfoPo : gunMapper.selectList(objectQueryWrapper)) {
            System.out.println(gunInfoPo);
        }

    }

    @Test
    void getLike() {
        QueryWrapper<GunInfoPo> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.like("name", "测试");
        for (GunInfoPo gunInfoPo : gunMapper.selectList(objectQueryWrapper)) {
            System.out.println(gunInfoPo);
        }
    }

    @Test
    void update() {
        QueryWrapper<GunInfoPo> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("id",3);
        GunInfoPo gunInfoPo = new GunInfoPo();
        gunInfoPo.setName("你麻痹");
        gunInfoPo.setUpdateTime(LocalDateTime.now());
        int update = gunMapper.update(gunInfoPo, objectQueryWrapper);

    }

    @Test
    void delete() {
        QueryWrapper<GunInfoPo> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("id",4);
        gunMapper.delete(objectQueryWrapper);
    }

    @Test
    void select() {
        QueryWrapper<GunInfoPo> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.in("id",1,2,3);
        objectQueryWrapper.and(c->c.like("name","逼"));
        gunMapper.selectList(objectQueryWrapper);
    }


}
