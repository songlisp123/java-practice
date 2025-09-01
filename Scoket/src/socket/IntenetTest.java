package socket;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IntenetTest {
    public static void main(String[] args)  {
        try {
            if (args.length>0) {
                String host = args[0];
                InetAddress[] inetAddresses = InetAddress.getAllByName(host);
                for (InetAddress inetAddress : inetAddresses) {
                    System.out.println(inetAddress);
                }
            }else {
                InetAddress host = InetAddress.getLocalHost();
                System.out.println(host);
            }
        }catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
