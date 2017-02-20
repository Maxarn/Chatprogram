import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Server {
    private static final int PORT = 1337;
    private static List<SocketDoots> clients = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(PORT);
            Socket socket;
            while (true) {
                socket = ss.accept();
                System.out.println("Doot connected from " + socket.getInetAddress());
                SocketDoots sh = new SocketDoots(socket);
                clients.add(sh);
                Thread t = new Thread(sh);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void writeMessage(String message) {
        for (SocketDoots sh : clients) {
            System.out.println("Writing " + message + " to client " + sh.getName());
            sh.write(message);
        }
    }
}