package MultiThreadedWebServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class Server {
    public Consumer<Socket> getConsumer() {
        return (socket) -> {
            try {
                PrintWriter toClient = new PrintWriter(socket.getOutputStream());
                toClient.println("Hello from server");
                toClient.close();
                socket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        };
    }

    public static void main(String[] args) {
        int port = 8010;
        Server server = new Server();
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(100000);
            System.out.println("Server is running on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected from " + socket.getInetAddress().getHostAddress());
                Thread thread = new Thread(() -> server.getConsumer().accept(socket));
                thread.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
