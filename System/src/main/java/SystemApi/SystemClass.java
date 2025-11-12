package SystemApi;


import java.util.Map;
import java.util.Properties;

public class SystemClass {
    public static void main(String[] args) {
        Map<String, String> env = System.getenv();
        for (Map.Entry<String,String> entry : env.entrySet()) {
            System.out.println(entry.getKey()+entry.getValue());
        }
        Properties properties = System.getProperties();
        String property = System.getProperty("user.dir");
        System.out.printf("根目录是:%s%n",property);

    }
}
