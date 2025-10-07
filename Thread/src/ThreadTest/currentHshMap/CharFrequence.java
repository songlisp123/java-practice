package ThreadTest.currentHshMap;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class CharFrequence {
    private static ConcurrentHashMap<String,Long> hashMap =
            new ConcurrentHashMap<>();
    //给定的查询参数
    private static String string = "我";
    //给定的根目录路径
    private static Path root = Path.of(".");

    public static void main(String[] args) throws InterruptedException {
        //命令行参数
        root=(args.length>0)?Path.of(args[0]):root;
        //路径参数列表
        Stream<Path> pathStream = find(root);
        //线程任务
        Runnable task = ()->{
            pathStream.forEach(p-> {
                try {
                    process(p);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        };
        new Thread(task,"任务").start();
    }

    /**
     * 查找根目录{@code root}的所有子文件
     * @param rootPath 根目录
     * @return {@code null}如果路径不存在<br />
     * {@code Stream<Path>}如果路径存在，则返回包含{@code path}对象的流
     */
    private static Stream<Path> find(Path rootPath) {
        try {
            Stream<Path> pathStream = Files.walk(rootPath);
            return pathStream.filter(p->p.getFileName().toString().endsWith(".txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 处理每个文件的文本，程序逐字读取文件，并判断是否与给定的参数一致<br />
     * 若一致将会存放到{@code hashmap}中,该程序保存给定文本的出现频率
     * @param path {@code path}参数
     * @throws IOException 如果文件不存在抛出io流异常
     *
     */
    public static void process(Path path) throws IOException {
        try(var in = new  Scanner(path, StandardCharsets.UTF_8))
        {
            while (in.hasNext()) {
                String word = in.next();

                for (int i=0;i<word.length();i++) {
                    String c = word.substring(i,i+1);
                    if (string.endsWith(c)) {
                        hashMap.merge(c, 1L, Long::sum);
                        hashMap.forEach((k,v)->{
                            System.out.println("键："+k+"值："+v);
                        });
                    }
                }

            }
        }

    }
}
