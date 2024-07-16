import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public void run() throws IOException {
        System.out.println("Client is running");
        int port = 8010;
        InetAddress address = InetAddress.getByName("localhost");
        Socket socket = new Socket(address, port);
        // PrintWriter toSocket = new PrintWriter(socket.getOutputStream());
        // now read the file and and write bytes in output stream
        File file = new File("text.txt");
        try (FileInputStream inputStream = new FileInputStream(file);
                BufferedOutputStream toSocket = new BufferedOutputStream(socket.getOutputStream())) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                toSocket.write(buffer, 0, bytesRead);
            }
            toSocket.flush();
        }
        // Read the response from the server
        try {
            BufferedReader fromSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = fromSocket.readLine();
            if (line == null || line.isEmpty()) {
                System.out.println("No response received from server or response is empty.");
            } else {
                System.out.println("Response from the server: " + line);
            }
        } catch (Exception e) {

            System.out.println("Error reading from socket: " + e.getMessage());
        } finally {
            socket.close();
        }
    }

    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
