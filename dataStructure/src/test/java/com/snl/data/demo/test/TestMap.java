package com.snl.data.demo.test;

import com.snl.data.homework.charptor03.practice01.level.SimpleLevelMap;

public class TestMap {

    public static void main(String[] args) {
        SimpleLevelMap levelMap = new SimpleLevelMap();
        Character[][] level = levelMap.getLevel_1();
        if (level == null)
            return;
        int i,j;
        for (j = 0;j< level.length;j++) {
            for (i =0 ;i<level[0].length;i++) {
                char c = level[j][i];
                System.out.print("wall  ");
                if (i == level[0].length - 1)
                    System.out.println();
            }
        }
    }
}
