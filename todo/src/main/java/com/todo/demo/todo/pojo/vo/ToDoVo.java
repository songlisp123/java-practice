package com.todo.demo.todo.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.todo.demo.todo.pojo.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Arrays;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDoVo {
    private Long id;
    private String title;
    private String status;
    private String messageLevel;
    private String tag;
    private String description;
    private String message;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS",timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS",timezone = "GMT+8")
    private Date updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS",timezone = "GMT+8")
    private Date expireTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS",timezone = "GMT+8")
    private Date completeTime;

    private String tagCode;

    public void setTag(String tag) {
        Tag tag1 = Arrays.stream(Tag.values())
                .filter(t -> t.getCode().toString().equals(tag))
                .findFirst()
                .orElseGet(()->Tag.CODE);
        this.tag = tag1.getMessage();
    }
}
