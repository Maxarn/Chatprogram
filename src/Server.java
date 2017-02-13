import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {
   public static void main(String[] args) {
        try {
            System.out.println("Server local address: " + InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.getMessage();
        }

       /* try{
            ServerSocket ss = new ServerSocket(1337);
            Socket s = ss.accept();


            s.close();
        }catch (Exception e) {
            // Doot
        }
        */
    }

}
