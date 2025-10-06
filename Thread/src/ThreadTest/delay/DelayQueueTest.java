package ThreadTest.delay;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/*
演示延迟队列的功能
 */
public class DelayQueueTest {
    //我需要三个工能，每个功能不一样的延迟时间

    private static final DelayQueue<Task> queue = new DelayQueue<>();

    public static void main(String[] args) {
        Runnable task = ()->{
            Task task1 = new Task(1000L);
            Task task2 = new Task(2000L);
            Task task3 = new Task(3000L);
            queue.put(task1);
            System.out.println("存入任务a");
            queue.put(task2);
            System.out.println("存入任务b");
            queue.put(task3);
            System.out.println("存入任务c");
        };

        Thread thread = new Thread(task);
        thread.start();

        Runnable task2 = ()->{
            try {
                while (true) {
                    Task take = queue.take();
                    System.out.println(Thread.currentThread().getName()+"执行完成！");
                    take.doSomeThing();

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        for (int i=0;i<3;i++) {
            new Thread(task2).start();
        }

    }
    private static class Task implements Delayed {
        private long delayed;
        private long expireTime; //触发时间
        private Task(long delayed) {
            this.delayed = delayed;
            //这个是判断失效时间
            this.expireTime = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(delayed);
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(expireTime - System.nanoTime(),TimeUnit.NANOSECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.compare(this.getDelay(TimeUnit.MILLISECONDS),o.getDelay(TimeUnit.MILLISECONDS));
        }

        public static void doSomeThing() {
            System.out.println("任务执行成功！");
        }

        @Override
        public String toString() {
            return "Task{" +
                    "delayed=" + delayed +
                    ", expireTime=" + expireTime +
                    '}';
        }
    }
}
