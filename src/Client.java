

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    //    private static String HOST = "192.168.0.100";
    public static void main(String[] args) {
        if (args.length > 1) {
            String USERNAME = args[1];
            String HOST = args[0];
            try (Socket socket = new Socket(HOST, 1337)) {
                System.out.println("Connected to "+HOST+":1337");
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader userInuput = new BufferedReader(new InputStreamReader(System.in));

                ClientListener cl = new ClientListener(socket);
                Thread t = new Thread(cl);
                t.start();


                String s;
                System.out.print("Write message: ");
                while((s = userInuput.readLine()) != null){



                    writer.write(USERNAME + ": " + s + "\r\n");
                    writer.flush();
                    /*
                    System.out.println("Reading message");
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                    */


                }
                writer.close();
//                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println("java Client <ip> <username>");
            // TODO kukat gui kanske !? !? ?!
        }
    }

}