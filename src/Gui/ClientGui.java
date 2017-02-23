package Gui;/**
 * Created by suddkuk on 2017-02-22.
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.xml.soap.Text;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * TODO:
 *      1 thread listen for server response
 *      1 thread listen for messaege to send
 */


public class ClientGui extends Application {
    private String USERNAME;
    private String HOST;
    private int PORT;
    private PrintWriter writer  = null;
    private BufferedReader reader = null;
    private BufferedReader userInuput  = null;

    private Socket serverSocket;

    private boolean validateInput(String ip, String port, String username, TextArea textArea){
        boolean ggwp = true;
        if(ip.isEmpty()) {
            textArea.appendText("IP cannot be empty.\n");
            ggwp = false;
        }
        if (username.isEmpty()) {
            textArea.appendText("Username cannot be empty.\n");
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
        try {
            serverSocket = new Socket(HOST, PORT);
        } catch (IOException e) {
            textArea.appendText(e.getMessage() + "\n");
            return false;
        }
        return true;

    }

    private void loginScene(Stage primaryStage){
        GridPane gp = new GridPane();
        BorderPane bp = new BorderPane();
        Label ipLabel = new Label("IP");
        Label portLabel = new Label("Port");
        Label usernameLabel = new Label("Username");
        TextField ipField = new TextField("192.168.0.100");
        TextField portField = new TextField("1337");
        TextField usernameField = new TextField("Dootface");
        TextArea textArea = new TextArea();
        Button button = new Button("Login");
        button.setOnAction(event -> {
            if(validateInput(ipField.getText(), portField.getText(), usernameField.getText(), textArea)){
                if(createSocket(textArea)) {
                    chatScene(primaryStage);
                }
            }
        });

        gp.add(ipLabel,0,0);
        gp.add(portLabel,0,1);
        gp.add(usernameLabel,0,2);
        gp.add(ipField,1,0);
        gp.add(portField,1,1);
        gp.add(usernameField,1,2);
        gp.add(button,0,3);

        bp.setCenter(gp);
        bp.setBottom(textArea);

        Scene scene = new Scene(bp);
        primaryStage.setScene(scene);

    }

    private void chatScene(Stage primaryStage){
        BorderPane bp = new BorderPane();
        BorderPane bottomPane = new BorderPane();
        TextArea serverResponse = new TextArea();
        TextArea clientInput = new TextArea();
        Button button = new Button("Send");
        button.setPrefSize(70,70);
        button.setOnAction(event -> {
            String s = String.format("%s: %s",USERNAME, clientInput.getText());
            sendMessage(s,serverResponse);
//            System.out.println(clientInput.getText());
        });

        bottomPane.setCenter(clientInput);
        bottomPane.setRight(button);

        bp.setBottom(bottomPane);
        bp.setCenter(serverResponse);

        Scene scene = new Scene(bp);
        primaryStage.setScene(scene);

        try {

            startClientListener();

        } catch (IOException e) {
            serverResponse.appendText(e.getMessage() + "\n");
        }

    }

    private void sendMessage(String message, TextArea serverResponse){
        try {
            writer = new PrintWriter(serverSocket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            writer.write(message);
        } catch (IOException e) {
            serverResponse.appendText(e.getMessage() + "\n");
        }finally {
            writer.close();
            try {
                reader.close();
            } catch (IOException e) {
                serverResponse.appendText(e.getMessage() + "\n");
            }
        }
    }
    private void startClientListener() throws IOException {
        ClientListener cl = new ClientListener(serverSocket);
        Thread t = new Thread(cl);
        t.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        loginScene(primaryStage);
        primaryStage.show();
    }
}
