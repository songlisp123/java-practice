package com.todo.demo.todo.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDoQuery {

    private Long id;
    private String title;
    private String tag;
    private String messageLevel;
    private String message;

    private String tagCode;
    private String status;

    private Integer pageNumber; //当前页码
    private Integer pageSize; //当前要查询的数量
}
