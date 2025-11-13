package ThreadTest.completeedFuture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompleteFutureDemo {
    private static final Pattern IMG_PATTERN = Pattern.compile(
            "[<]\\s*[iI][mM][gG]\\s*[^>]*[sS][rR][cC]\\s*[=]\\s*['\"]([^'\"]*)['\"][^>]*[>]");
    private ExecutorService executor = Executors.newCachedThreadPool();
    private URI uriToProcess;

    public CompletableFuture<String> readPage(URI uri)
    {
        return CompletableFuture.supplyAsync(() -> {
            try
            {
                var contents = new String(uri.toURL().openStream().readAllBytes());
                System.out.println("Read page from " + uri);
                System.out.println(contents);
                return contents;
            }
            catch (IOException e)
            {
                throw new UncheckedIOException(e);
            }
        }, executor);
    }

    public List<URI> getImageLinks(String webpage) // not blocking
    {
        var result = new ArrayList<URI>();
        Matcher matcher = IMG_PATTERN.matcher(webpage);
        while (matcher.find())
        {
            URI uri = URI.create(uriToProcess + "/" + matcher.group(1));
            System.out.println("发现图片链接R");
            result.add(uri);
        }
        System.out.println("Found links: " + result);
        return result;
    }

    public CompletableFuture<List<BufferedImage>> getImages(List<URI> uris)
    {
        return CompletableFuture.supplyAsync(() -> {
            try
            {
                var result = new ArrayList<BufferedImage>();
                for (URI uri : uris)
                {
                    result.add(ImageIO.read(uri.toURL()));
                    System.out.println("Loaded " + uri);
                }
                return result;
            }
            catch (IOException e)
            {
                throw new UncheckedIOException(e);
            }
        }, executor);
    }

    public void saveImages(List<BufferedImage> images)
    {
        System.out.println("Saving " + images.size() + " images");
        try
        {
            for (int i = 0; i < images.size(); i++)
            {
                String filename = "/tmp/image" + (i + 1) + ".png";
                ImageIO.write(images.get(i), "PNG", new File(filename));
            }
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }
        executor.shutdown();
    }

    public CompleteFutureDemo(URI uri)
    {
        uriToProcess = uri;
    }

    public void run() throws IOException, InterruptedException
    {
        CompletableFuture.completedFuture(uriToProcess)
                .thenComposeAsync(this::readPage, executor)
                .thenApply(this::getImageLinks)
                .thenCompose(this::getImages)
                .thenAccept(this::saveImages);

        // or use the HTTP client:
      /*
      HttpClient client = HttpClient.newBuilder().build();
      HttpRequest request = HttpRequest.newBuilder(uriToProcess).GET().build();
      client.sendAsync(request, BodyHandlers.ofString())
         .thenApply(HttpResponse::body)
         .thenApply(this::getImageLinks)
         .thenCompose(this::getImages)
         .thenAccept(this::saveImages);
      */
    }

    public static void main(String[] args)
            throws IOException, InterruptedException
    {
        new CompleteFutureDemo(URI.create("https://www.cnki.net")).run();
    }
}
