package ThreadTest.ArrayBlocking;

import jdk.dynalink.Operation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.concurrent.ArrayBlockingQueue;

/*
这是一个训练批处理文件的程序要求是：
初始容器容量有限
优雅的关闭线程
批处理操作
 */
public class logTest {
    private static final ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(100);
    private static Long currentTime = System.currentTimeMillis();
    private static final int MAX_NUMBERS = 200;

    public static void main(String[] args) {
        Runnable producer = ()->{
            //这里面放入的是我的日志文件
            try {
                for (int i=0;i<MAX_NUMBERS;i++) {
                    queue.put("[%s]正在写入存放数据……".formatted(Thread.currentThread().getName()));
                }
                queue.put("完成");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        //为程序创建五个生产者程序
        for (int i=0;i<5;i++) {
            Thread thread = new Thread(producer, "生产者 %d 号".formatted(i));
            thread.start();
        }

        Runnable consumer = ()->{
            boolean done = false;//用来判断是否处理完毕的标志

            try {
                while (!done) {
                        String take = queue.take();
                        if ("完成".equals(take)) {
                            done = true;
                        }
                        //这是io流操作，使用语法糖自动关闭IO流
                        //程序要求每隔两秒钟记录一次日志
                        try (PrintWriter printWriter =
                                     new PrintWriter(new BufferedWriter(
                                             new OutputStreamWriter(
                                                     new FileOutputStream("./logTest.txt", true),
                                                     StandardCharsets.UTF_8))))
                        {
                            printWriter.println( LocalDateTime.now()+take);
                        }

                        //这是nio流操作，不用自动关闭，当操作完成时，会自动关闭
//                    Files.writeString(Path.of("./logTest.txt"),take+'\n',StandardOpenOption.APPEND);
                }
                System.out.println("程序处理完成！");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        };

        Thread thread = new Thread(consumer, "消费者");
        thread.start();

    }
}
