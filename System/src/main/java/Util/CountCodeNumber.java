package Util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

/**
 * 计算当前文件根目录的所有代码总量
 * @author snl
 * @since 2025-11-20
 */
public class CountCodeNumber {

    /**
     * 可重入锁
     */
    private static final ReentrantLock lock = new ReentrantLock();

    /**
     * 执行器
     */
    private static final ExecutorService  executorService = Executors.newScheduledThreadPool(
            Runtime.getRuntime().availableProcessors());

    /**
     * 阻塞队列
     */
    private static final BlockingQueue<Path> queue = new LinkedBlockingQueue<>();

    private static final String rootPath = System.getProperty("user.dir");

    private static final Path  targetPath = Path.of(rootPath,"count.txt");

    /**
     * 多线程原子性递增数字
     */
    private static AtomicLong codeNumber = new AtomicLong(0L);


    public static void main(String[] args) throws InterruptedException, IOException {
        //todo
        Path totalNumberPath = createTotalNumberPath();
        //todo 查找总代码
        Long lastTotalCode = countTotalCode(totalNumberPath);
        String message = "";
        findStaticPath(null);
        Thread.sleep(2000L);
        queue.put(Path.of("null"));
        PlanB();
        Thread.sleep(5000L); //逻辑非常脆弱，万一睡眠时间过了，其他线程没有执行完呢？
        //获取当前代码
        long countNUmber = codeNumber.get();
        if(lastTotalCode == null) {
            lastTotalCode = 0L;
        }else { //不推荐，硬编码太多
            if (countNUmber >= lastTotalCode) {
                 message = "["+LocalDateTime.now() + "]: " +
                        "一共写了%d行代码  ".formatted(countNUmber) + "比上次 "+
                        "+ %d%n".formatted(countNUmber - lastTotalCode);
            }else {
                 message = "["+LocalDateTime.now() + "]: " +
                        "一共写了%d行代码  ".formatted(countNUmber) + "比上次 "+
                        "- %d%n".formatted(lastTotalCode - countNUmber);
            }

        }
        Files.writeString(totalNumberPath,message,StandardOpenOption.APPEND);
    }

    /**
     * 查找以。java为扩展的文件，并写入到阻塞队列里面
     * @param searchPath 搜索扫描的文件路径
     */
    public static void findStaticPath(Path searchPath)  {

        if (Objects.isNull(searchPath)) {
            searchPath  =  Path.of(rootPath);
        }

        try(Stream<Path> pathStream = Files.walk(searchPath,1, FileVisitOption.FOLLOW_LINKS)) {
            for (Path p: pathStream.toList()) {
                if (p.toFile().isFile()) {
                    if (p.toString().endsWith(".java")) {
                        System.out.println(p);
                        queue.put(p);
                    }

                }else {
                    //是目录
                    if (Objects.equals(p.getParent(),searchPath)) {
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

            try(Stream<String> stream = Files.lines(path)) {

                /*
                效率低下的io扫描，不推荐
                Scanner scanner = new Scanner(path);
                首次执行的是
                 D:\java_practice\audio\src\main\java\audio\AudioTest.java
                为何会发生无限递增呢？
                while (scanner.hasNextLine()) { // 逻辑出现问题
                    String string = scanner.nextLine();
                    count++;
                }
                 */
                count = stream.count();
                codeNumber.getAndAdd(count);
                String message = path + "   总行数："+"[" + count + "]\n";
                lock.lock();
                Files.writeString(targetPath, message,StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }finally {
                lock.unlock();
            }
        };
    }

    /**
     * 尝试方法二，设置一个阻塞队列，每次工作线程只消费一次任务
     */

    private static void PlanB() throws IOException {
        if (targetPath.toFile().exists()) {
            Files.delete(targetPath);
        }
        Files.createFile(targetPath);
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

    /**
     * 创建新文件存放总代码量数字
     * @return 新路径
     * @throws IOException 如果创建文件失败或者写入失败
     */
    private static Path createTotalNumberPath() throws IOException {
        Path countTotalNumberPath = Path.of(rootPath,"countTotalNumber.txt");
        if (!countTotalNumberPath.toFile().exists()) {
            Files.createFile(countTotalNumberPath);
        }
        return countTotalNumberPath;
    }

    /**
     * 统计总代码量
     * @param path 统计路径
     * @return 当前总代码量
     */
    private static Long countTotalCode(Path path) {
        Long counts = null;
        try(Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8);) {
            List<String> streamList = stream.toList();
            if ( !streamList.isEmpty()) {
                //获取最新的总代码量
                String last = streamList.getLast();
                counts = toGetTotalNumber(last);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return counts;
    }

    /**
     * 判断总代码量,硬编码非常不推荐
     * @param last 最后一次的代码片段
     * @return 当前总代码量
     */
    private static Long toGetTotalNumber(String last) {
        String string = last.split("了")[1];
        String stringNumber = string.split("行")[0];
        return Long.valueOf(stringNumber);
    }
}

//AI给的建议：
/**
 * 1、并发设计线程判断结束不合理，判断条件不合理（死循环风险） ✅已经修复
 * 2、锁使用有严重问题，如果io层出现异常，锁永远不会被释放    ✅已经修复
 * 3、io扫描低效    ✅ 已经修复
 * 4、文件遍历存在重复递归  ❌ 未修复
 * 5、（重要）控制流程不明显--不能调用thread。sleep这是不可靠的做法，因为你不能确定是否线程都做完了 ❌ 未修复 ，等我学会了再写
 * 6、静态变量太多 ❌ 未修复
 * 7、程序结构可优化
 * 8、可读性问题
 */

/**
 * 优化思路：
 * 1、线程并发使用计数们
 * 2、模块分层
 * 3、结构优化
 */
