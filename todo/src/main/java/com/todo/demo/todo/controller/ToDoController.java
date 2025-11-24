package com.todo.demo.todo.controller;

import com.todo.demo.base.page.Pager;
import com.todo.demo.base.response.JsonResult;
import com.todo.demo.textEditor.Editor;
import com.todo.demo.todo.pojo.dto.ToDoQuery;
import com.todo.demo.todo.pojo.dto.TodoSave;
import com.todo.demo.todo.pojo.vo.TagVo;
import com.todo.demo.todo.pojo.vo.ToDoVo;
import com.todo.demo.todo.service.ToDoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/todo/")
public class ToDoController {

    @Autowired
    private ToDoService service;

    @PostMapping("save")
    public JsonResult save(TodoSave save) throws InterruptedException {
        log.debug("保存参数:{}",save);
        service.save(save);
        return JsonResult.ok();
    }
    @GetMapping("query")
    public JsonResult query(ToDoQuery query) throws InterruptedException {
        log.debug("查询参数:{}",query);
        Pager pager = service.query(query);
        return JsonResult.ok(pager);
    }

    @PostMapping("update/{id}")
    public JsonResult update(@PathVariable Long id) throws InterruptedException {
        log.debug("当前参数L{}",id);
        service.update(id);
        return JsonResult.ok();
    }

    @GetMapping("loadTag")
    public  JsonResult tag() {
        List<TagVo> vos = service.loadTags();
        return JsonResult.ok(vos);
    }

    @PostMapping("delete/{id}")
    public  JsonResult DELETE(@PathVariable Long id) throws InterruptedException {
        service.delete(id);
        return JsonResult.ok();
    }

    @GetMapping("edit")
    public JsonResult ok() {
        Editor.play();
        return JsonResult.ok("应用程序启动成功");
    }
}
