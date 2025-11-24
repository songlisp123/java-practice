package com.todo.demo.todo.mapper;

import com.todo.demo.todo.pojo.dto.ToDoQuery;
import com.todo.demo.todo.pojo.entity.ToDo;
import com.todo.demo.todo.pojo.vo.TagVo;
import com.todo.demo.todo.pojo.vo.ToDoVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDoMapper {
    void save(ToDo toDo);

    List<ToDoVo>  query(ToDoQuery query);

    void update(ToDo toDo);

    Long countAll(ToDoQuery query);

    List<TagVo> loadTags();

    void delete(Long id);
}
