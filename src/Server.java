import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {
    public static int PORT_NUMBER = 1337;
   public static void main(String[] args) {
        try {
            System.out.println(String.format("Server address: %s, Port: %s", InetAddress.getLocalHost().getHostAddress(),PORT_NUMBER));
        } catch (UnknownHostException e) {
            e.getMessage();
        }

//        try{
//            ServerSocket ss = new ServerSocket(PORT_NUMBER);
//            Socket s = ss.accept();
//
//
//            s.close();
//        }catch (Exception e) {
//            // Doot
//        }

    }

}
