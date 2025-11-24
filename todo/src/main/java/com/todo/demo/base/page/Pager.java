package com.todo.demo.base.page;

import com.todo.demo.todo.pojo.entity.Action;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.BlockingQueue;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pager<U,T extends BlockingQueue<? extends Action>> {

    private Long total;
    private U data;
    private T queue;

}
