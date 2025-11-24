package com.todo.demo.todo.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum     Level {

    ORDINARY("10","普通"),
    IMPORTANT("20","重要"),
    EMERGENCE("30","紧急"),
    DEADLINE("40","十万火急");
    private String code;
    private String message;
}
