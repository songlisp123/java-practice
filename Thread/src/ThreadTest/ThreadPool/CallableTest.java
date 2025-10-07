package ThreadTest.ThreadPool;

import java.util.concurrent.*;

public class CallableTest implements Callable<String> {


    @Override
    public String call() throws Exception {
        return "你好！";
    }
}

class test {
    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
        Future<String> submit = executorService.submit(new CallableTest());
        String string = submit.get();
        System.out.println(string+"执行成功！");
        String call = new CallableTest().call();
        System.out.println(call);
        Callable<String> task = ()->"你好";
        FutureTask<String> futureTask = new FutureTask<>(task);


    }
}
