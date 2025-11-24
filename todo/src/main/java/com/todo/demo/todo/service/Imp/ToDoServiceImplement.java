package com.todo.demo.todo.service.Imp;

import com.todo.demo.base.music.WarningMusic;
import com.todo.demo.base.page.Pager;
import com.todo.demo.base.timer.Timer;
import com.todo.demo.todo.mapper.ToDoMapper;
import com.todo.demo.todo.pojo.dto.ToDoQuery;
import com.todo.demo.todo.pojo.dto.TodoSave;
import com.todo.demo.todo.pojo.entity.Action;
import com.todo.demo.todo.pojo.entity.Status;
import com.todo.demo.todo.pojo.entity.ToDo;
import com.todo.demo.todo.pojo.vo.TagVo;
import com.todo.demo.todo.pojo.vo.ToDoVo;
import com.todo.demo.todo.service.ToDoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class ToDoServiceImplement implements ToDoService {

    private final int EXPIRE_DAY = 3;
//    private static final ArrayBlockingQueue<Action> queue =
//            new ArrayBlockingQueue<>(10);

    private final Pager<List<?>,BlockingQueue<? extends Action>> pager = new Pager<>();

    private final Timer<Action> timerSch = new Timer<>(15);

    private final LinkedBlockingQueue<ToDoVo> linkedBlockingQueue;

    private ToDoMapper toDoMapper;

    private LocalDateTime timeStamp;

    private WarningMusic warningMusic;

    private AtomicInteger atomicInteger;

    private AtomicLong toTalToDoTask;


    public ToDoServiceImplement(ToDoMapper toDoMapper) throws InterruptedException {
        this.toDoMapper = toDoMapper;
        this.warningMusic = new WarningMusic();
        timeStamp = LocalDateTime.now();
        this.linkedBlockingQueue = new LinkedBlockingQueue<>();
        this.atomicInteger = new AtomicInteger(0);
        this.toTalToDoTask = new AtomicLong();
        timeToUpdate();
        timeToDelete();
        total();
    }

    @Override
    public void save(TodoSave save) throws InterruptedException {
        log.debug("进入到参数是:{}",save);
        ToDo toDo = new ToDo();
        BeanUtils.copyProperties(save,toDo);

        if (toDo.getId() != null ){}
        else  {
            toDo.setCreateTime(new Date());
            toDo.setStatus(Status.TODO.getCode());
            toDoMapper.save(toDo);
            Action action = new Action();
            action.setTitle("发布任务+[%s]".formatted(save.getTitle()));
            action.setCreateTime(new Date());
            timerSch.addAction(action);
//            if (queue.remainingCapacity()>=1) {
//                queue.put(action);
//            }else {
//                queue.take();
//                queue.put(action);
//            }
        }
    }

    @Override
    public Pager<List<?>,BlockingQueue<? extends Action>> query(ToDoQuery query) throws InterruptedException {
        int pageNumber = query.getPageNumber() ==null ? 1 : query.getPageNumber();
        int pageSize = query.getPageSize() == null ? 4 : query.getPageSize();
        pageNumber = (pageNumber -1) * pageSize;
        query.setPageNumber(pageNumber);
        query.setPageSize(pageSize);
        List<ToDoVo> query1 = toDoMapper.query(query);
        Long l = toDoMapper.countAll(query);

        pager.setTotal(l);

//        query1.forEach(q->{
//            Date createTime = q.getCreateTime();
//            Date currentDate = new Date();
//            long currentDateTime = currentDate.getTime();
//            long time = createTime.getTime();
//            if (currentDateTime - time > 3600*1000*EXPIRE_DAY &&
//                    q.getStatus().equals(Status.TODO.getCode())) {
//                ToDo toDo = new ToDo();
//                toDo.setId(q.getId());
//                toDo.setStatus(Status.EXPIRED.getCode());
//                toDo.setUpdateTime(new Date());
//                toDo.setExpireTime(currentDate);
//                toDoMapper.update(toDo);
//                Action action = new Action();
//                action.setTitle("-[%s]-任务过期😡😭".formatted(q.getTitle()));
//                action.setCreateTime(new Date());
//                if (queue.remainingCapacity()>=1) {
//                    try {
//                        queue.put(action);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                }else {
//                    try {
//                        queue.take();
//                        queue.put(action);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//        });

//        List<ToDoVo> query2 = toDoMapper.query(query);
        pager.setData(query1);
        pager.setQueue(timerSch.get());
        System.out.println(pager);
        return pager;
    }

    @Override
    public void update(Long id) throws InterruptedException {
        log.debug("业务层参数:{}",id);
        ToDo toDo = new ToDo();
        toDo.setId(id);
        toDo.setStatus(Status.DONE.getCode());
        toDo.setUpdateTime(new Date());
        toDo.setCompleteTime(new Date());
        toDoMapper.update(toDo);
        Action action = new Action();
        action.setTitle("项目完成，恭喜！");
        action.setCreateTime(new Date());
        timerSch.addAction(action);
//        if (queue.remainingCapacity()>=1) {
//            queue.put(action);
//        }else {
//            queue.take();
//            queue.put(action);
//        }
    }


    @Override
    public List<TagVo> loadTags() {
        return toDoMapper.loadTags();
    }

    @Override
    public void delete(Long id) throws InterruptedException {
        toDoMapper.delete(id);
        Action action = new Action();
        int i = atomicInteger.addAndGet(1);
        action.setTitle("用户删除了--任务--,已删除 [%d] 项任务,还剩 [%d] 项任务".
                formatted(i, this.toTalToDoTask.get()));

        if (i < 3) {
            action.setTitle(action.getTitle());
        }
        else if ( i<=5) {
            action.setTitle(action.getTitle()+"\n删除了那么多,这都完不成?懦夫😒");
        } else if (i <= 10) {
            action.setTitle(action.getTitle()+"\n卧槽,牛逼了哥们!奉劝一句话,咩有精钢站,别揽瓷器活!😡");
        } else if ( i <= 20) {
            action.setTitle(action.getTitle()+"\n哥们,转世投胎吧,别再人间蹉跎岁月了😮‍💨");
        } else {
            action.setTitle(action.getTitle()+"\n此人只应天上有,人间难得几回闻😑");
        }

        action.setCreateTime(new Date());
        timerSch.addAction(action);
//        if (queue.remainingCapacity()>=1) {
//            queue.put(action);
//        }else {
//            queue.take();
//            queue.put(action);
//        }
    }

    public  void timeToUpdate() {
        ToDoQuery toDoQuery = new ToDoQuery();
        toDoQuery.setStatus(Status.TODO.getCode());



        Runnable timer = ()->{
            while (true) {
                LocalDateTime now = LocalDateTime.now();
                while (now.plusSeconds(-10).isAfter(timeStamp)) {
                    timeStamp = now;
                    List<ToDoVo> toDoVoList = toDoMapper.query(toDoQuery);
                    toDoVoList.forEach(vo->{
                        Date createTime = vo.getCreateTime();
                        Date date = new Date();

                        long l = date.getTime() - createTime.getTime();


                        if (l>3600*1000*24*EXPIRE_DAY) {
                            warningMusic.play();
                            ToDo toDo = new ToDo();
                            toDo.setId(vo.getId());
                            toDo.setExpireTime(new Date());
                            toDo.setUpdateTime(new Date());
                            toDo.setStatus(Status.EXPIRED.getCode());
                            toDoMapper.update(toDo);
                            Action action = new Action();
                            action.setTitle("-[%s]-任务过期😡😭".formatted(vo.getTitle()));
                            action.setCreateTime(new Date());
                            action.setIsExpired(true);
                            try {
                                timerSch.addAction(action);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    });
                }
            }
        };

        new Thread(timer,"时间调度器").start();

    }

    private void play() throws InterruptedException {
        if (!this.linkedBlockingQueue.isEmpty())  {
            linkedBlockingQueue.take();
            warningMusic.play();

        }
    }

    private void total() {
        ToDoQuery toDoQuery = new ToDoQuery();
        toDoQuery.setStatus(Status.TODO.getCode());
        Runnable totalTask = () -> {
            LocalDateTime now = LocalDateTime.now();
            while (true) {
                while (LocalDateTime.now().plusSeconds(-3).isAfter(now)) {
                    now = LocalDateTime.now();
                    this.toTalToDoTask.set(toDoMapper.countAll(toDoQuery));
                }
            }
        };

        new Thread(totalTask,"总任务").start();
    }


    private void timeToDelete() throws InterruptedException {
        ToDoQuery toDoQuery = new ToDoQuery();
        toDoQuery.setStatus(Status.EXPIRED.getCode());

        Runnable task = ()->{
            LocalDateTime now = LocalDateTime.now();
            while (true) {
                while (LocalDateTime.now().plusMinutes(-10).isAfter(now)) {
                    now = LocalDateTime.now();
                    List<ToDoVo> query = toDoMapper.query(toDoQuery);
                    if (query.size() >= 4) {
                        for (int i = 0; i < query.size(); i++) {
                            if (i >= query.size() - 4) {
                                ToDoVo toDoVo = query.get(i);
                                toDoMapper.delete(toDoVo.getId());
                                Action action = new Action();
                                action.setTitle("移除过期任务:[%s]".formatted(toDoVo.getTitle()));
                                action.setCreateTime(new Date());
                                try {
                                    timerSch.addAction(action);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }
            }
       };

        new Thread(task,"定时删除任务").start();
    }

}
