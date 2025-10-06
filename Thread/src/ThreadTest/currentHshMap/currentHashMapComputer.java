package ThreadTest.currentHshMap;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class currentHashMapComputer {
    private static ConcurrentHashMap<String,Long> hashMap =
            new ConcurrentHashMap<>();

    private static LinkedBlockingQueue<Path> paths =
            new LinkedBlockingQueue<>();

    private static final int MAX_SIZES = 2000;

    private static final File outputFile = new File("./20251006.txt");

    public static void main(String[] args) {
        Path path = args.length>0?Path.of(args[0]):Path.of(".");
        Stream<Path> files = findFile(path);

        //生产者线程将从根目录遍历，寻找文件并将其放入到链表阻塞队列中
        Runnable producer = ()->{
            try {
                files.forEach(p-> {
                    try {
                        //我还需要一个逻辑来迭代当前文件夹并将文件放入到链表阻塞队列中
                        paths.put(p);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                paths.put(Path.of("null"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        new Thread(producer,"producer").start();

        //消费者线程
        Runnable consumer = ()->{
            boolean done = false;
            try {
                while (!done) {
                    Path take = paths.take();
                    if ("null".equals(take.toString())) {
                        done = true;
                        paths.put(take);
                        continue;
                    }
                    File file = take.toFile();
                    String name = file.getName();
                    long length = file.length();

                    //导出到文件中
                    try(PrintWriter printWriter = new PrintWriter(
                            new BufferedWriter(
                                new OutputStreamWriter(
                                    new FileOutputStream(outputFile,true),
                                    StandardCharsets.UTF_8))))
                    {
                        printWriter.println(name+"\t"+length/1024+"kb");

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        for (int i=0;i<5;i++) {
            new Thread(consumer,"消费者 %d 号".formatted(i)).start();
        }

    }

    public static Stream<Path> findFile(Path rootPath) {
        try {
            Stream<Path> pathStream = Files.walk(rootPath);
            return pathStream.filter(p->p.toFile().isFile())
                    .filter(p->p.toFile().length()>MAX_SIZES);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
