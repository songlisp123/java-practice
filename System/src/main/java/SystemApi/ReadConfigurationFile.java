package SystemApi;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Stream;

public class ReadConfigurationFile {

    private static final List<Path> paths = new ArrayList<>();

    public static void main(String[] args) {
        //TODO
        Properties properties = new Properties();
        List<Path> staticPath = findStaticPath(null);
        try( Reader reader = Files.newBufferedReader(paths.getFirst())){
           properties.load(reader);
            String username = properties.getProperty("user.username");
            System.out.println("用户名："+username);
            String password = properties.getProperty("user.password");
           System.out.println("密码是："+password);
           String userLoin = properties.getProperty("user.first.login");
           System.out.println("第一次登录："+userLoin);
            String userLastLogin = properties.getProperty("user.last.login");
            System.out.println("最后一次登录时间："+userLastLogin);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Path> findStaticPath(Path searchPath)  {
        String parent = "System";
        if (Objects.isNull(searchPath)) {
            String rootPath = System.getProperty("user.dir");
            searchPath  =  Path.of(rootPath,parent);
        }

        try(Stream<Path> pathStream = Files.walk(searchPath,1,FileVisitOption.FOLLOW_LINKS)) {
            for (Path p: pathStream.toList()) {
                if (p.toFile().isFile()) {
                    //todo
                    if (Objects.equals(p.getFileName().toString(),"application.properties")) {
//                        System.out.println(p);
                        paths.add(p);
                    }

                }else {
                    //是目录
                    if (Objects.equals(p.getParent(),searchPath)) {
                        System.out.println(p);
                        findStaticPath(p);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return paths;
    }
}
