package socket;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class TestDemo {
    public static void main(String[] args) {
        try {
            InetAddress[] inetAddress = Inet4Address.getAllByName("google.com");
            for (InetAddress inetAddress1 : inetAddress) {
                System.out.println(inetAddress1);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
