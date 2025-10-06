package ThreadTest.linkedBlockingQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

/*
此程序是为了实现一个三阶段数据流水线处理系统的测试方案
具体方案设计：
生产者 → 阶段1（清洗数据） → 阶段2（计算） → 阶段3（输出）
在这个例子中，将会以………………离职
 */
public class LikeQueue {

    private static final LinkedBlockingQueue<Integer> linkQueue1 = new LinkedBlockingQueue<>();
    private static final LinkedBlockingQueue<Integer> linkQueue2 = new LinkedBlockingQueue<>();
    private static final List<Integer> lists = new ArrayList<>(100000);

    private static final Random random = new Random();
    private static AtomicLong production = new AtomicLong(0L);
    private static AtomicLong consumption = new AtomicLong(0L);

    public static void main(String[] args) {

        for (int i=0;i<100000;i++) {
            lists.add(random.nextInt(0,100));
        }
        //我需要一个生产者来处理，清洗数据
        Runnable producer = ()->{
            lists.stream().filter(e->e%2==0)
                    .filter(e->e>50)
                    .forEach(e-> {
                        try {
                            linkQueue1.put(e);
                            production.addAndGet(1L);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    });
            try {
                linkQueue1.put(100);
                production.addAndGet(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        new Thread(producer,"生产者").start();

        //阶段二数据计算
        Runnable task2 = ()->{
            boolean done = false;
            try {
                while (!done) {
                    int take = linkQueue1.take();
                    if (take == 100) {
                        done = true;
                        linkQueue2.put(take);
                        continue;
                    }
                    System.out.println("原来的是："+take);
                    take = take * 2;
                    linkQueue2.put(take);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        new Thread(task2,"计算").start();

        //阶段三，消费者抽取打印
        Runnable task3 = ()->{
            boolean done = false;
            try {
                while (!done) {
                    int take = linkQueue2.take();
                    if (take == 100) {
                        done = true;
                        continue;
                    }
                    consumption.addAndGet(1L);
                    System.out.println("答案是：" + take);
                }
                System.out.println("处理完毕！");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        new Thread(task3,"输出").start();
    }
}
