package countDown;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class CountDownTestDemo2 {

    public static void main(String[] args) {
        int countNumber = 500;
        CountDownLatch done = new CountDownLatch(countNumber);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        //获取执行器
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            for (int i = 0; i < countNumber; i++) {
                executor.execute(new Worker(done, atomicInteger));
            }
            done.await();
            System.out.printf("number的值是:%d%n", atomicInteger.get());
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            System.out.println("程序运行结束R！");
        }

    }

    private static class Worker implements Runnable {

        private final CountDownLatch done;
        private  AtomicInteger number;

        public Worker(CountDownLatch done, AtomicInteger number) {
            this.done = done;
            this.number = number;
        }

        @Override
        public void run() {
            //做些事情
            number.addAndGet(1);
            done.countDown();
        }
    }
}
