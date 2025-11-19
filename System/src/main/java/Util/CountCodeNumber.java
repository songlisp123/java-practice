package Util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

public class CountCodeNumber {

    private static final ReentrantLock lock = new ReentrantLock();

    private static final ExecutorService  executorService = Executors.newCachedThreadPool();

    private static final BlockingQueue<Path> queue = new LinkedBlockingQueue<>();

    private static AtomicLong codeNumber = new AtomicLong(0L);

    public static void main(String[] args) throws InterruptedException {
        findStaticPath(null);
        Thread.sleep(2000L);
        queue.put(Path.of("null"));
        PlanB();
        Thread.sleep(5000L);
        System.out.printf("一共写了%d行代码%n",codeNumber.get());
    }

    public static void findStaticPath(Path searchPath)  {

        if (Objects.isNull(searchPath)) {
            String rootPath = System.getProperty("user.dir");
            searchPath  =  Path.of(rootPath);
        }

        try(Stream<Path> pathStream = Files.walk(searchPath,1, FileVisitOption.FOLLOW_LINKS)) {
            for (Path p: pathStream.toList()) {
                if (p.toFile().isFile()) {
                    //todo
//                    System.out.println(p);
                    if (p.toString().endsWith(".java")) {
                        System.out.println(p);
                        //TODO
                        //创建新线程，查找代码行数
//                        executorService.execute(handler(p));
                        queue.put(p);
                    }

                }else {
                    //是目录
                    if (Objects.equals(p.getParent(),searchPath)) {
//                        System.out.println(p);
                        //递归调用该方法
                        findStaticPath(p);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 方法一破产
     * @param path 目标路径
     * @return runnable接口
     */
    private static Runnable handler(Path path) {
        return () -> {
            long count = 0L;
            try(Scanner scanner = new Scanner(path)) {
                //首次执行的是
                // D:\java_practice\audio\src\main\java\audio\AudioTest.java
                //为何会发生无限递增呢？
                while (scanner.hasNextLine()) { // 逻辑出现问题
                    String string = scanner.nextLine();
//                    System.out.println(string);
                    count++;
                }
                codeNumber.getAndAdd(count);
                String property = System.getProperty("user.dir");
                Path targetPath = Path.of(property,"count.txt");
                String message = path.toString() + "   总行数："+"[" + count + "]\n";
                if (!targetPath.toFile().exists()) {
                    Files.createFile(targetPath);
                }
                lock.lock();

                Files.write(targetPath,message.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            finally {
                lock.unlock();
            }
        };
    }

    /**
     * 尝试方法二，设置一个阻塞队列，每次工作线程只消费一次任务
     */

    private static void PlanB() {
        while (queue.remainingCapacity() > 1) {

            try {
                Path path = queue.take();
                if (Objects.equals(path.toString(),"null")) {
                    break;
                }
                executorService.execute(handler(path));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        executorService.shutdown();
    }
}
