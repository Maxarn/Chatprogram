
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread{
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private static int PORT = 1337;
    private ServerSocket serverSocket;
    private static String chatHistory = "";
    private static Thread t;
    public static boolean RUNSERVER = true;


    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(30000);
    }

    public void run() {
        LOGGER.log(Level.INFO, String.format("Waiting for client on port %s...", serverSocket.getLocalPort()));
        Socket server = null;
        try {

            server = serverSocket.accept();
            LOGGER.log(Level.INFO, String.format("Just connected to %s", server.getRemoteSocketAddress()));

            while (RUNSERVER) {
                try {
                    DataInputStream in = new DataInputStream(server.getInputStream());
                    DataOutputStream out = new DataOutputStream(server.getOutputStream());
                    String clientMessage = in.readUTF();
                    if (clientMessage != null){
                        LOGGER.log(Level.INFO, String.format("%s\n", clientMessage));
                        out.writeUTF(clientMessage);
                    }

//                out.writeUTF(String.format()"Thank you for connecting to " + server.getLocalSocketAddress()
//                        + "\nGoodbye!");

                    t.sleep(1000);
                } catch (SocketTimeoutException s) {
                    System.out.println("Socket timed out!");
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }

            }
            server.close();
            LOGGER.log(Level.INFO, "Stopping Server...");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        LOGGER.setLevel(Level.ALL);
        try {
            t = new Server(PORT);
            t.start();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void startServer() {
        LOGGER.setLevel(Level.INFO);

        try {
            System.out.println("Starting thread");
            t = new Server(PORT);
            t.start();

        }catch(IOException e) {
            e.printStackTrace();
        }
    }


}
