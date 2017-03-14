import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by suddkuk on 2017-02-21.
 */
public  class ClientListener implements Runnable{
    Socket socket;
    BufferedReader reader;

    @Override
    public void run() {
        System.out.println("Reading message");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public ClientListener(Socket socket) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
}
