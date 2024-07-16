import java.net.Socket;

@FunctionalInterface
public interface FileReceiver {
    void receiveFile(Socket acceptedConnection);

}