package com.todo.demo.base.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Status {

    OPERATION_SUCCESS(2000,"操作成功"),
    OPERATION_FAILED(3000,"操作失败");
    private Integer code;
    private String message;
}
