package ThreadTest.currentHshMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class currentHashMapTest {
    private static ConcurrentHashMap<String,Long> freq = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(processors);
        Path path = Path.of(".");
        for (Path p:descendants(path)) {
            if (p.getFileName().toString().endsWith(".java")) {
                executorService.execute(()->process(p));
            }
        }
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.MINUTES);
        freq.forEach((k,v)->{
            if (v>10) {
                System.out.println("键 %s 一共出现 %d 次".formatted(k,v));
            }
        });
    }

    public static Set<Path> descendants(Path rootPath) throws IOException {
        try (Stream<Path> pathStream = Files.walk(rootPath)) {
            return pathStream.collect(Collectors.toSet());
        }
    }

    public static void process(Path file) {
        try(var in = new Scanner(file))
        {
            while (in.hasNext()) {
                String word = in.next();
                freq.merge(word,1L,Long::sum);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
