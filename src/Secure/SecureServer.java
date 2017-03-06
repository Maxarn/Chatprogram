package Secure;

import java.io.*;
import java.net.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Arrays;
import javax.net.ssl.*;


public class SecureServer {
    private final static String algorithm = "SSL";
    private final static char[] PASSWORD = "dootdoot".toCharArray();
    private final static String KEYPATH = "keyDoot";
    private static int PORT = 1337;

    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                PORT = Integer.parseInt(args[0]);
            }catch (NumberFormatException e){
                System.out.println(String.format("Invalid port number: %s, shutting down.", args[0]));
                System.exit(-1);
            }
        }



        try {
            SSLContext context = SSLContext.getInstance(algorithm);

            // The reference implementation only supports X.509 keys
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");

            // Oracle's default kind of key store
            KeyStore ks = KeyStore.getInstance("JKS");

            // For security, every key store is encrypted with a
            // passphrase that must be provided before we can load
            // it from disk. The passphrase is stored as a char[] array
            // so it can be wiped from memory quickly rather than
            // waiting for a garbage collector.
            /**
             *  char[] password = System.console().readPassword();
             */

            ks.load(new FileInputStream(KEYPATH), PASSWORD);
            kmf.init(ks, PASSWORD);
            context.init(kmf.getKeyManagers(), null, null);

            // wipe the password
            Arrays.fill(PASSWORD, '0');
            SSLServerSocketFactory factory
                    = context.getServerSocketFactory();
            SSLServerSocket server
                    = (SSLServerSocket) factory.createServerSocket(PORT);

            // add anonymous (non-authenticated) cipher suites
            String[] supported = server.getSupportedCipherSuites();
            String[] anonCipherSuitesSupported = new String[supported.length];
            int numAnonCipherSuitesSupported = 0;
            for (int i = 0; i < supported.length; i++) {
                if (supported[i].indexOf("_anon_") > 0) {
                    anonCipherSuitesSupported[numAnonCipherSuitesSupported++] =
                            supported[i];
                }
            }
            String[] oldEnabled = server.getEnabledCipherSuites();
            String[] newEnabled = new String[oldEnabled.length
                    + numAnonCipherSuitesSupported];
            System.arraycopy(oldEnabled, 0, newEnabled, 0, oldEnabled.length);
            System.arraycopy(anonCipherSuitesSupported, 0, newEnabled,
                    oldEnabled.length, numAnonCipherSuitesSupported);
            server.setEnabledCipherSuites(newEnabled);

            // Now all the set up is complete and we can focus
            // on the actual communication.
            while (true) {

                // This socket will be secure,
                // but there's no indication of that in the code!
                try (Socket theConnection = server.accept()) {
                    InputStream in = theConnection.getInputStream();
                    int c;
                    while ((c = in.read()) != -1) {
                        System.out.write(c);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException | KeyManagementException
                | KeyStoreException | NoSuchAlgorithmException
                | CertificateException | UnrecoverableKeyException ex) {
            ex.printStackTrace();
        }
    }
}

