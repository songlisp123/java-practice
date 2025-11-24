package com.todo.demo.todo.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDo {

    private Long id;
    private String title;
    private String status;
    private String messageLevel;
    private String tag;
    private String description;
    private String message;
    private Date createTime;
    private Date updateTime;

    private Date expireTime;

    private Date completeTime;

}
