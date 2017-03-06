package Gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.net.ssl.SSLSocket;

/**
 * Created by suddkuk on 2017-02-23.
 */
public class ClientListener implements Runnable{
    SSLSocket socket;
    BufferedReader reader;

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
    public ClientListener(SSLSocket socket) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    public void closeSocket() throws IOException {
        this.reader.close();
        this.socket.close();
    }
}
