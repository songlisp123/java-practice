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

    private static final String rootPath = System.getProperty("user.dir");

    private static AtomicLong codeNumber = new AtomicLong(0L);

    public static void main(String[] args) throws InterruptedException {
        findStaticPath(null);
        Thread.sleep(2000L);
        queue.put(Path.of("null"));
        PlanB();
        Thread.sleep(5000L); //逻辑非常脆弱，万一睡眠时间过了，其他线程没有执行完呢？
        System.out.printf("一共写了%d行代码%n",codeNumber.get());
    }

    public static void findStaticPath(Path searchPath)  {

        if (Objects.isNull(searchPath)) {
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
                Path targetPath = Path.of(rootPath,"count.txt");
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
//        while (queue.remainingCapacity() > 1) { //死循环，不推荐
        while (true) { //死循环，不推荐
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

//AI给的建议：
/**
 * 1、并发设计线程判断结束不合理，判断条件不合理（死循环风险）
 * 2、锁使用有严重问题，如果io层出现异常，锁永远不会被释放
 * 3、io扫描低效
 * 4、文件遍历存在重复递归
 * 5、（重要）控制流程不明显--不能调用thread。sleep这是不可靠的做法，因为你不能确定是否线程都做完了
 * 6、静态变量太多
 * 7、程序结构可优化
 * 8、可读性问题
 */

/**
 * 优化思路：
 * 1、线程并发使用计数们
 * 2、模块分层
 * 3、结构优化
 */
