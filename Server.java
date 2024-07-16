import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class Server {
    public FileReceiver fileReceiver;

    public FileReceiver getReceiver() {
        return (acceptedConnection) -> {
            try (BufferedInputStream inputStream = new BufferedInputStream(acceptedConnection.getInputStream());
                    FileOutputStream fileOutputStream = new FileOutputStream(new File("FromClient.txt"), true)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    public void run() throws IOException {
        int port = 8010;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running on port " + port);

            while (true) {
                try (Socket acceptedConnection = serverSocket.accept();
                        PrintWriter toClient = new PrintWriter(acceptedConnection.getOutputStream(), true)) {

                    System.out.println(
                            "Connection is accepted from client " + acceptedConnection.getRemoteSocketAddress());

                    fileReceiver = getReceiver();
                    fileReceiver.receiveFile(acceptedConnection);

                    toClient.println("File is successfully saved");
                    toClient.flush();

                } catch (IOException ex) {
                    System.out.println("Server encountered an error: " + ex.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Server could not start: " + e.getMessage());
        }

    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.run();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}