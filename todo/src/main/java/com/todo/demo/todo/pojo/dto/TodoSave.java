package com.todo.demo.todo.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoSave {

    private String title;
    private String messageLevel;
    private String tag;
    private String description;
    private String message;

}
