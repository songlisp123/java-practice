package com.todo.demo.todo.service;

import com.todo.demo.base.page.Pager;
import com.todo.demo.todo.pojo.dto.ToDoQuery;
import com.todo.demo.todo.pojo.dto.TodoSave;
import com.todo.demo.todo.pojo.vo.TagVo;
import com.todo.demo.todo.pojo.vo.ToDoVo;

import java.util.List;

public interface ToDoService {
    void save(TodoSave save) throws InterruptedException;

    Pager query(ToDoQuery query) throws InterruptedException;

    void update(Long id) throws InterruptedException;

    List<TagVo> loadTags();

    void delete(Long id) throws InterruptedException;
}
