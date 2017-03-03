import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Server {
    private static final int PORT = 1337;
    private static List<SocketHandler> clients = new ArrayList<>();
    private static List<String> history = new ArrayList<>();
    
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(PORT);
            Socket socket;
            while (true) {
                System.out.println("LIST SIEZ: " + clients.size());
                socket = ss.accept();
                System.out.println("Doot connected from " + socket.getInetAddress());
                SocketHandler sh = new SocketHandler(socket);
                clients.add(sh);
                Thread t = new Thread(sh);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void writeMessage(String message) {
        history.add(message);
        for (SocketHandler sh : clients) {
            System.out.println("Writing " + message + " to client " + sh.getName());
            sh.write(message);
        }
    }
    
    public static void writeHistory(PrintWriter writer) {
        for (String historyLine : history) {
            writer.write(historyLine + "\r\n");
            writer.flush();
        }
    }
}