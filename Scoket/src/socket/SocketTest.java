package socket;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class SocketTest {
    public static void main(String[] args)  {
        Scanner out = new Scanner(System.in);
        try
        {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("localhost",8189),10000);
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter printWriter = new PrintWriter(
                    new BufferedOutputStream(socket.getOutputStream()),true);
            System.out.println("链接成功！");
            printWriter.println("你好👋");
            while (in.hasNext()) {
                System.out.println("是否想要退出？");
                String answer = out.nextLine();
                if ("是".equals(answer)) break;
                System.out.println("输入您想要发送到服务端的信息：");
                String message = out.nextLine();

                System.out.println("[客户端]："+message);
                //发送到服务端
                printWriter.println(message);
                //接收服务端发送的信息
                String s= in.nextLine();
                System.out.println("[服务端：]"+s);
            }
            System.out.println(1);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
