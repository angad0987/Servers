import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public ExecutorService executorService;

    public Server(int poolSize) {
        executorService = Executors.newFixedThreadPool(poolSize);
    }

    public void handle(Socket socket) {
        try {
            PrintWriter toClent = new PrintWriter(socket.getOutputStream());
            toClent.println("Hello from server");
            toClent.close();
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 8010;
        int poolSize = 30;
        Server server = new Server(poolSize);
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(100000);
            System.out.println("Server is running");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Server accepted connection FROM : " + socket.getRemoteSocketAddress());
                server.executorService.execute(() -> server.handle(socket));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}