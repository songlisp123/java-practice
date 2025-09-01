package socket;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer {
    public static void main(String[] args) {
        //开启固定的端口号
        int port =args.length>1?Integer.parseInt(args[0]):8088;

        try (ServerSocket s = new ServerSocket(port);
             Socket socket = s.accept()
        )
        {
            server(socket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void server(Socket in) {
        try (Scanner input = new Scanner(in.getInputStream());
             PrintWriter printWriter = new PrintWriter(new BufferedOutputStream(
                     in.getOutputStream()
             ),true)
        )
        {
            boolean done = false;
            while (!done&&input.hasNext()) {
                String line = input.nextLine();
                printWriter.println(line);
                System.out.println(line);
                if ("是".equals(line)) done = true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
