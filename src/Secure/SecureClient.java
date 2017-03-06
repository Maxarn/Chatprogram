package Secure;

import Gui.ClientGui;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.*;

/**
 * Created by suddkuk on 2017-03-06.
 */
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

        try {
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(HOST, PORT);

            InputStream inputstream = System.in;
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

            OutputStream outputstream = sslsocket.getOutputStream();
            OutputStreamWriter outputstreamwriter = new OutputStreamWriter(outputstream);
            BufferedWriter bufferedwriter = new BufferedWriter(outputstreamwriter);

            String string = null;
            while ((string = bufferedreader.readLine()) != null) {
                bufferedwriter.write(string + '\n');
                bufferedwriter.flush();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
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
