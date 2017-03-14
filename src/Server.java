import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.*;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;


public class Server {
    private static final int PORT = 1337;
    private static List<SocketHandler> clients = new ArrayList<>();
    private static List<String> history = new ArrayList<>();

    public final static String ALGORITHM = "SSL";
    
    private final static char[] PASSWORD = "dootdoot".toCharArray();
    private final static String KEYSTORE_PATH = "keyDoot";
    public static HashMap registeredUsers = new HashMap<String, String>();
    
    public static void main(String[] args) {
        try {
            
            // Certificate handeling begin.
            SSLContext context = SSLContext.getInstance(ALGORITHM);
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            
            KeyStore ks = KeyStore.getInstance("JKS");
            
            ks.load(new FileInputStream(KEYSTORE_PATH), PASSWORD);
            kmf.init(ks, PASSWORD);
            context.init(kmf.getKeyManagers(), null, null);
            
            Arrays.fill(PASSWORD, '0');
            
            SSLServerSocketFactory factory = context.getServerSocketFactory();
            SSLServerSocket ss = (SSLServerSocket) factory.createServerSocket(PORT);
            
            String[] supported = ss.getSupportedCipherSuites();
            String[] anonCipherSuitesSupported = new String[supported.length];
            
            int numAnonCipherSuitesSupported = 0;
            for (int i = 0; i < supported.length; i++) {
                if (supported[i].indexOf("_anon_") > 0) {
                    anonCipherSuitesSupported[numAnonCipherSuitesSupported++] =
                    supported[i];
                }
            }
            
            String[] oldEnabled = ss.getEnabledCipherSuites();
            String[] newEnabled = new String[oldEnabled.length
                    + numAnonCipherSuitesSupported];
            
            System.arraycopy(oldEnabled, 0, newEnabled, 0, oldEnabled.length);
            System.arraycopy(anonCipherSuitesSupported, 0, newEnabled,
                    oldEnabled.length, numAnonCipherSuitesSupported);
            
            ss.setEnabledCipherSuites(newEnabled);
            // Certificate handeling end.
            
            while (true) {
                System.out.println("LIST SIEZ: " + clients.size());
                Socket socket = ss.accept();
                System.out.println("Doot connected from " + socket.getInetAddress());
                SocketHandler sh = new SocketHandler(socket);
                clients.add(sh);
                Thread t = new Thread(sh);
                t.start();
            }
        } catch (IOException | KeyManagementException
                | KeyStoreException | NoSuchAlgorithmException
                | CertificateException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void writeMessage(String message) {
        history.add(message);
        for (SocketHandler sh : clients) {
            System.out.println("Writing " + message + " to client " + sh.getName());
            sh.write(message);
        }
    }
    
    public static void writeHistory(PrintWriter writer) {
        for (String historyLine : history) {
            writer.write(historyLine + "\r\n");
            writer.flush();
        }
    }
    
    public static void clearClient(SocketHandler client) {
        if (clients.contains(client))
            clients.remove(client);
    }
    
    public static boolean registerUser(String username, String password) {
        if (registeredUsers.containsKey(username)) {
            return false;
        } else {
            registeredUsers.put(username, password);
            return true;
        }
    }
}