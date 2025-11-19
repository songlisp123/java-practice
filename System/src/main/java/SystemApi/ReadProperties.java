package SystemApi;

import java.util.Properties;

public class ReadProperties {
    public static void main(String[] args) {
        Properties properties =
                System.getProperties();
        properties.forEach((k,v)->{
            System.out.printf("键是：%s -> 值是：%s%n",k,v);
        });
    }
}
