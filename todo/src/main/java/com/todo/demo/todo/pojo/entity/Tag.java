package com.todo.demo.todo.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Tag {
    LIFE(10,"生活"),
    HOMEWORK(20,"作业"),
    FAMILY(30,"家庭"),
    WORK(40,"工作"),
    LOVING(50,"爱情"),
    TECH(60,"科技"),
    FOODS(70,"美食"),
    SAO(80,"打扫"),
    ARTICLE(90,"文章"),
    CODE(100,"代码");
    private Integer code;
    private String message;
}
