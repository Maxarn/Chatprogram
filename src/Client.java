import java.io.*;
import java.net.*;


public class Client extends Thread {
    private static String USERNAME = "fuckface";

    private static String SERVER = "192.168.0.100";
    private static int PORT = 1337;
    private static Socket socket = null;

    public static void createSocket() throws IOException {
        if (socket == null) {
            socket = new Socket(SERVER, PORT);
        }
    }


    public static void sendMessage(String message) throws IOException {
        Socket socket = null;
        socket = new Socket(SERVER, PORT);
        OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
        osw.write(message, 0, message.length());


        socket.close();


    }

    public void run() {
        String message = "";
        try {


            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            Socket client = new Socket(SERVER, PORT);
            while(!message.toLowerCase().equals("quit")) {

                OutputStream outToServer = client.getOutputStream();
                System.out.print("\nMessage to server: ");
                message = br.readLine();

                DataOutputStream out = new DataOutputStream(outToServer);
                out.writeUTF(String.format("%s: %s",USERNAME, message));

                InputStream inFromServer = client.getInputStream();
                DataInputStream in = new DataInputStream(inFromServer);

                System.out.println("SERVER SENDS: " + in.readUTF());

            }
            System.out.println("Closing socket.");
            client.close();

        }catch(IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {


        Thread t = new Client();
        t.start();


    }

}
