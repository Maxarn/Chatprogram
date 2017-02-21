

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
//    private static String HOST = "192.168.0.100";
    public static void main(String[] args) {
        if (args.length > 0) {
            String message = (args.length > 1) ? args[1] : "LOOOOOOOL";
            String HOST = args[0];
            try (Socket socket = new Socket(HOST, 1337)) {
                System.out.println("Connected to "+HOST+":1337");
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                System.out.println("Writing message");
                writer.write(message + "\r\n");
                writer.flush();

                System.out.println("Reading message");
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                writer.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            // TODO kukat gui kanske !? !? ?!
        }
    }
}