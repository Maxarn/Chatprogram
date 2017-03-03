import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketHandler implements Runnable {
    private String name;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public SocketHandler(Socket socket) {
        this.socket = socket;
        this.name = socket.getInetAddress().toString();
    }

    @Override
    public void run() {
        try {
            System.out.println("Creating reader/writer");
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            Server.writeHistory(writer);
            
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Read " + line + " from socket");
                Server.writeMessage(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String message) {
        writer.write(message + "\r\n");
        writer.flush();
    }

    public String getName() {
        return name;
    }
}