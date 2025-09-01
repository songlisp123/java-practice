package socket;

import javax.net.SocketFactory;
import java.io.IOException;
import java.net.Socket;

public class Sercuty {
    public static void main(String[] args)  {
        SocketFactory socketFactory = SocketFactory.getDefault();
        try(Socket socket = socketFactory.createSocket("localhost",8890) )
        {

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
