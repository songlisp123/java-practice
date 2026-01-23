package com.snl.data.homework.charptor03.practice01.entity.enmry;

import java.util.Random;

public class StringTalk {

    private String[] data;
    private int count;
    private Random random;

    public StringTalk() {
        data = new String[]{
                "傻逼！","你在看什么？","卧槽你麻痹","你看什么呢？","我日你八辈仙人"
        };
        count = 0;
        random = new Random();
    }

    public String talk() {
         return data[count];
    }

    public void update() {
        count = random.nextInt(data.length);
    }
}
