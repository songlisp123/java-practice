package com.todo.demo.todo.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagVo {

    private Long id;
    private Integer code;
    private String message;
}
