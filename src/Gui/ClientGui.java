package Gui;/**
 * Created by suddkuk on 2017-02-22.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.xml.soap.Text;
import java.beans.EventHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import javafx.concurrent.Task;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * TODO:
 *      1 thread listen for server response
 *      1 thread listen for messaege to send
 */


public class ClientGui extends Application {
    private final String REGISTER_KEY = "REGISTER";
    private final String LOGIN_KEY = "LOGIN";
    
    private String USERNAME;
    private String HOST;
    private int PORT;
    private PrintWriter writer  = null;
    private BufferedReader reader = null;
    private BufferedReader userInuput  = null;
    public static TextArea serverResponse;

    private SSLSocket serverSocket = null;
    private ClientListener cl;

    private final static String PASSWORD = "dootdoot";
    private final static String KEYSTORE_PATH = "keyDoot";
    
    private boolean validateInput(String ip, String port, String username, String password, TextArea textArea){
        boolean ggwp = true;
        if(ip.isEmpty()) {
            textArea.appendText("IP cannot be empty.\n");
            ggwp = false;
        }
        if (username.isEmpty()) {
            textArea.appendText("Username cannot be empty.\n");
            ggwp = false;
        }
        if (password.isEmpty()) {
            textArea.appendText("Password cannot be empty.\n");
            ggwp = false;
        }
        if (!port.isEmpty()) {
            try {
                PORT = Integer.parseInt(port);
            } catch (NumberFormatException e) {
                textArea.appendText(String.format("%s is not a number.\n", port));
                ggwp = false;
            }
        }
        else {
            textArea.appendText("Port cannot be empty.\n");
            ggwp = false;
        }
        if(ggwp){
            HOST = ip;
            USERNAME = username;
        }
        return ggwp;
    }
    
    private boolean createSocket(TextArea textArea)  {
        if( serverSocket == null) {
            try {
                SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                serverSocket = (SSLSocket) sslsocketfactory.createSocket(HOST, PORT);

                createReaderAndWriter();
                
                cl = new ClientListener(serverSocket);
                Thread t = new Thread(cl);
                t.start();

//            serverSocket = new Socket(HOST, PORT);
            } catch (IOException e) {
                textArea.appendText(e.getMessage() + "\n");
                return false;
            }
        }
        return true;
    }
    
    private void registerUser(String username, String password){

        writer.write(REGISTER_KEY + username + " " + password + "\r\n");
        writer.flush();
    }

    private void loginUser(String username, String password){

        writer.write(LOGIN_KEY + username + " " + password + "\r\n");
        writer.flush();
    }

    private void loginScene(Stage primaryStage){
        GridPane gp = new GridPane();
        BorderPane bp = new BorderPane();
        Label ipLabel = new Label("IP");
        Label portLabel = new Label("Port");
        Label usernameLabel = new Label("Username");
        Label passwordLabel = new Label("Password");
        TextField ipField = new TextField("localhost");
        TextField portField = new TextField("1337");
        TextField usernameField = new TextField("Dootface");
        PasswordField passwordField = new PasswordField();
        TextArea textArea = new TextArea();
        Button loginbutton = new Button("LOGIN");
        Button registerButton = new Button("REGISTER");

        loginbutton.setOnAction(event -> {
            if(validateInput(ipField.getText(), portField.getText(), usernameField.getText(), passwordField.getText(), textArea)) {
                if (createSocket(textArea)) {
                    loginUser(usernameField.getText(), passwordField.getText());
                }
            }
        });
        registerButton.setOnAction(event -> {
            if(validateInput(ipField.getText(), portField.getText(), usernameField.getText(), passwordField.getText(), textArea)) {
                if (createSocket(textArea)) {
                    registerUser(usernameField.getText(), passwordField.getText());
                }
            }
        });

        // CSS
        ipLabel.getStyleClass().add("login-label");
        portLabel.getStyleClass().add("login-label");
        usernameLabel.getStyleClass().add("login-label");
        passwordLabel.getStyleClass().add("login-label");
        ipField.getStyleClass().add("login-field");
        portField.getStyleClass().add("login-field");
        usernameField.getStyleClass().add("login-field");
        passwordField.getStyleClass().add("login-field");
        textArea.getStyleClass().add("");
        loginbutton.getStyleClass().add("button");
        registerButton.getStyleClass().add("button");


        gp.add(ipLabel,0,0);
        gp.add(portLabel,0,1);
        gp.add(usernameLabel,0,2);
        gp.add(passwordLabel,0,3);
        gp.add(ipField,1,0);
        gp.add(portField,1,1);
        gp.add(usernameField,1,2);
        gp.add(passwordField,1,3);
        gp.add(loginbutton,4,4);
        gp.add(registerButton,5,4);

        bp.setCenter(gp);
        bp.setBottom(textArea);

        Scene scene = new Scene(bp);
        scene.getStylesheets().add("Gui/gui.css");
        primaryStage.setScene(scene);
    }

    private void chatScene(Stage primaryStage){
        BorderPane bp = new BorderPane();
        BorderPane bottomPane = new BorderPane();
        BorderPane topPane = new BorderPane();
//        serverResponse = new TextArea();
//        serverResponse.setEditable(false);
//        serverResponse.setWrapText(true);
        TextArea clientInput = new TextArea();
        clientInput.setWrapText(true);
        clientInput.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)){
                try {
                    validateMessage(clientInput.getText().trim());
                    clientInput.clear();
                } catch (IOException e) {
                    serverResponse.appendText(e.getMessage() + "\n");
                }
                // Dont add a newline to clientInput textarea.
                event.consume();
            }
        });
        Button button = new Button("Send");
        button.setPrefSize(70,70);
        button.setOnAction(event -> {
            try {
                validateMessage(clientInput.getText().trim());
                clientInput.clear();
            } catch (IOException e) {
                serverResponse.appendText(e.getMessage() + "\n");
            }
        });
        Button quitButton = new Button("Quit");
        quitButton.setOnAction(event -> {
            closeProgram();
            Platform.exit();
        });

        // CSS
//        serverResponse.getStyleClass().add("");
        clientInput.getStyleClass().add("");
        button.getStyleClass().add("button");


        topPane.setRight(quitButton);

        bottomPane.setCenter(clientInput);
        bottomPane.setRight(button);

        bp.setBottom(bottomPane);
        bp.setCenter(serverResponse);
        bp.setTop(topPane);

        Scene scene = new Scene(bp);
        scene.getStylesheets().add("Gui/gui.css");
        primaryStage.setScene(scene);

//        try {
//            createReaderAndWriter();
//
//            cl = new ClientListener(serverSocket);
//            Thread t = new Thread(cl);
//            t.start();
//
//
//        } catch (IOException e) {
//            serverResponse.appendText(e.getMessage() + "\n");
//        }

    }

    private void validateMessage(String message) throws IOException{
        if (!message.isEmpty()){
            sendMessage(message);
        }
    }

    private void sendMessage(String message) throws IOException{
        String s = String.format("%s: %s\r\n",USERNAME,message);
        writer.write(s);
        writer.flush();
    }

    private void createReaderAndWriter() throws IOException {
        try {
            writer = new PrintWriter(serverSocket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

        } catch (IOException e) {
            serverResponse.appendText(e.getMessage() + "\n");
        }
    }

    // Close all the streams and stuff
    private void closeProgram(){
        try {
            writer.close();
            reader.close();
            serverSocket.close();
            cl.closeSocket();

        } catch (IOException e) {
            serverResponse.appendText(e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        System.setProperty("javax.net.ssl.trustStore", KEYSTORE_PATH);
        System.setProperty("javax.net.ssl.trustStorePassword", PASSWORD);
        
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        loginScene(primaryStage);
        primaryStage.show();

        Task task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while(!isCancelled()) {
                    
                    if (cl != null && cl.login_successfull) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                chatScene(primaryStage);
                            }
                        });
                        break;
                    }
                }
                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        
        serverResponse = new TextArea();
        serverResponse.setEditable(false);
        serverResponse.setWrapText(true);
        serverResponse.getStyleClass().add("");
    }

    @Override
    public void stop(){
        closeProgram();
        System.exit(0);
    }
}
