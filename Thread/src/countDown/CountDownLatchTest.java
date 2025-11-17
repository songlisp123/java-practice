package countDown;

import java.util.concurrent.CountDownLatch;

/**
 * 使用程序计数器执行程序
 */
public class CountDownLatchTest {

    public static void main(String...args) throws InterruptedException {
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(100);
        for (int i=0;i<100;i++) {
            new Thread(new Worker(startSignal,doneSignal,i),"第[%d]线程".formatted(i)).start();
        }
        //先让main线程做点事情
        Thread thread = Thread.currentThread();
        System.out.printf("这是%s线程%n",thread.getName());
        //启动所有线程
        startSignal.countDown();
        System.out.println("线程全部启动");
        doneSignal.await(); //等待所有线程执行任务成功
    }

    private static class Worker implements Runnable {

        private final CountDownLatch startSignal;
        private final CountDownLatch doneSignal;
        private final int i;

        private Worker(CountDownLatch startSignal, CountDownLatch doneSignal,int i) {
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
            this.i = i;
        }

        @Override
        public void run() {
            //调用count
            try {

                startSignal.await(); //阻塞所有线程
                //TODO
                Thread thread = Thread.currentThread();
                System.out.printf("这是%s线程%n",thread.getName());
                System.out.println(i);
                doneSignal.countDown();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
