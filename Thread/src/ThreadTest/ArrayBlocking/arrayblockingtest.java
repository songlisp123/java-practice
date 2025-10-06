package ThreadTest.ArrayBlocking;

import ThreadTest.windowHandle.CustonFormatter;
import ThreadTest.windowHandle.windowHandler;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
这是一个并发阻塞数组队列的小程序。
主要作用：用来演示生产者和消费者如何在不同的线程之间协调调度。
该程序使用并发数组阻塞队列，实现为循环数组。
 */
public class arrayblockingtest {
    private static ArrayBlockingQueue<Integer> arrayBlockingQueue =
            new ArrayBlockingQueue<>(5);

    private static final int MAX_THREAD_NUMBER = 10;
    private static final Random random = new Random();
    private static final int MAX_NUMBERS = 10;

    private static final Logger log = Logger.getLogger("ArrayBlocking");

    private  static  windowHandler windowHandler = new windowHandler();
    private static CustonFormatter formatter = new CustonFormatter();

    private static AtomicLong production = new AtomicLong(0);
    private static AtomicLong consumption = new AtomicLong(0);
    static {
        log.setUseParentHandlers(false);
        windowHandler.setFormatter(formatter);
        log.addHandler(windowHandler);
        log.setLevel(Level.ALL);
    }

    public static void main(String[] args) {
        //生产者线程将会随机产生一个数字

        Runnable task = ()->{
            //这是线程启动后将要执行的代码，需要
            //将会获取时间片并将填充阻塞数组。
            try {

                while (true) {
                    Thread thread = Thread.currentThread();
                    Integer randomNumber = random.nextInt(0, 100);
                    log.fine("[%s]正在操作……".formatted(thread.getName()));
                    arrayBlockingQueue.put(randomNumber);
                    production.addAndGet(1L);
                    System.out.println("生产："+production);
                    log.fine("[%s]存入数字：%d".formatted(thread.getName(),randomNumber));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        for (int i=0;i<8;i++) {
            Thread thread = new Thread(task);
            thread.setName("生产者 %d 号".formatted(i+1));
            thread.start();
        }

        //消费者线程
        for (int i =0;i<MAX_THREAD_NUMBER;i++) {
            Runnable task2 = ()->{
                try {
                    while (true) {
                        Thread thread = Thread.currentThread();
                        log.fine("正在从线程【%s】中提取数字……".formatted(thread.getName()));//走到这一步，阻塞了
                        Integer take = arrayBlockingQueue.take();
                        consumption.getAndAdd(1L);
                        System.out.println("消费："+consumption);
                        log.fine("线程【%s】提取到数字：%d".formatted(thread.getName(), take));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            new Thread(task2).start();
        }
    }
}
