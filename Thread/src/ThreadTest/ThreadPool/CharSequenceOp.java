package ThreadTest.ThreadPool;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class CharSequenceOp {
    private static Path rootPath = Path.of(".");
    private static String searchWord = "我";
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        rootPath=(args.length>0)?Path.of(args[0]):rootPath;
        Stream<Path> root = root(rootPath);

        //创建任务清单
        List<Callable<Long>> tasks = new ArrayList<>();
        List<Callable<Path>> searchTasks = new ArrayList<>();
        //每一个文件创建一个任务
        root.forEach(path -> {
            Callable<Long> task = () -> occurrences(searchWord,path);
            tasks.add(task);
            searchTasks.add(searchForTask(searchWord,path));
        });

        //创建消费者清单
        ExecutorService pool = Executors.newCachedThreadPool();
        Instant startTime = Instant.now();
        List<Future<Long>> futures = pool.invokeAll(tasks);
        long counts = 0;
        for (Future<Long> future : futures) {
            long count = future.get();
            counts += count;
        }

        Instant endTime = Instant.now();

        System.out.println("花费时间 %d ms".formatted(Duration.between(startTime,endTime).toMillis()));
        System.out.println("单词 我 一共出现 %d 次".formatted(counts));

        Path founded = pool.invokeAny(searchTasks);

        System.out.println("文字在"+founded+"找到");

        if (pool instanceof ThreadPoolExecutor threadPoolExecutor) {
            System.out.println("最大线程数量 %d".formatted(threadPoolExecutor.getLargestPoolSize()));
        }
        pool.shutdown();


    }

    /**
     * 根据目录遍历所有子目录哦
     * @param path 根目录
     * @return 一个包含所有结尾为.txt文件的流
     */
    public static Stream<Path> root(Path path) {
        try
        {
            Stream<Path> pathStream = Files.walk(path);
            return pathStream.filter(p->p.toFile().isFile())
                    .filter(p->p.toFile().toString().endsWith(".txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 任务函数，用来从不同文件中读取给定单词的函数
     * @param word 搜索的单词，默认是字符串
     * @param path 文件对象
     * @return 单词在该文件中出现的次数
     */
    public static Long occurrences(String word,Path path) {
        try(var in = new Scanner(path, StandardCharsets.UTF_8))
        {
            long count = 0L;
            while (in.hasNext()) {
                String words = in.next();
                for (int i = 0;i<words.length();i++) {
                    String c = words.substring(i,i+1);
                    if (word.equals(c)) count++;
                }
            }
            return count;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * 这是判断搜索线程的调式
     * @param word 要进行采集的数字
     * @param path 路径参数，表示一个文件
     * @return 检索路径文件的任务函数
     */
    public static Callable<Path> searchForTask(String word,Path path) {
        return ()->{
            try (Scanner in = new Scanner(path)) {
                while (in.hasNext()) {
                    String words = in.next();
                    for (int i=0;i<words.length();i++) {
                        String c = words.substring(i,i+1);
                        if (word.equals(c)) return path;
                        if (Thread.currentThread().isInterrupted()) {
                            System.out.println("搜索路径"+path+"终止");
                            return null;
                        }
                    }
                }
            }
            throw new NoSuchElementException();
        };
    }
}
