package com.todo.demo.todo.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Status {
    TODO("10","待办"),
    DONE("20","完成"),
    EXPIRED("30","逾期");
    private String code;
    private String message;
}
