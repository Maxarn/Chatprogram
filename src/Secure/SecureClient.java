package Secure;

import Gui.ClientGui;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.*;
import java.util.regex.Pattern;

public class SecureClient  implements Runnable{
    private static String HOST = "localhost";
    private static int PORT = 1337;
    private SSLSocket socket;
    BufferedReader reader;

    private static final String keyPath = "keyDoot";
    private static final String password = "dootdoot";
    
    public static void main(String[] args) {
        System.setProperty("javax.net.ssl.trustStore", keyPath);
        System.setProperty("javax.net.ssl.trustStorePassword", password);

        Pattern pattern = Pattern.compile("^(\\d{1,3}\\.){3}\\d{1,3}$");
        if (args.length > 0) {
            if (!pattern.matcher(args[0]).matches()) {
                System.out.println(String.format("Invalid IP: %s, shutting down.", args[0]));
                System.exit(-1);
            }
            else {
                HOST = args[0];
            }
        }
        if (args.length > 1) {
            try {
                PORT = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println(String.format("Invalid port number: %s, shutting down.", args[1]));
                System.exit(-1);
            }
        }


        try {
            System.out.println(String.format("Connecting to %s, port %s", HOST, PORT));
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(HOST, PORT);

            InputStream inputstream = System.in;
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

            OutputStream outputstream = sslsocket.getOutputStream();
            OutputStreamWriter outputstreamwriter = new OutputStreamWriter(outputstream);
            BufferedWriter bufferedwriter = new BufferedWriter(outputstreamwriter);

            String string = null;
            System.out.print("Message: ");
            while ((string = bufferedreader.readLine()) != null) {
                bufferedwriter.write(string + '\n');
                bufferedwriter.flush();
                System.out.print("Message: ");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    private static boolean testIp(String s){

        return false;
    }
    @Override
    public void run() {
        String line;
        try{
            while ((line = reader.readLine()) != null) {
                ClientGui.serverResponse.appendText((line + "\n"));
            }
            reader.close();
        }catch (IOException e){
//            e.printStackTrace();
//            Do nothing, just die.
        }
    }
}
