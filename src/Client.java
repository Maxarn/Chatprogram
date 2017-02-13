import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class Client extends Application {
    private static String USERNAME = "";
    private static boolean LOGGED_IN = false;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Post login screen with all the chat stuffz
     */
    private void setSceneChat(Stage primaryStage){
        BorderPane bp = new BorderPane();

        TextArea chatRenderer = new TextArea();
        chatRenderer.setEditable(false);
        bp.setCenter(chatRenderer);

        HBox hBox = new HBox();
        TextArea chatInput = new TextArea();
        Button button = new Button("Send");
        button.setOnAction(event -> {
            System.out.println(String.format("%s: %s",USERNAME,chatInput.getText()));
            chatInput.clear();
        });
        hBox.getChildren().addAll(chatInput,button);
        bp.setBottom(hBox);

        Scene scene = new Scene(bp,1024,600);
        primaryStage.setScene(scene);

    }


    @Override
    public void start(Stage primaryStage) {
        /**
         * Login Screen
         */
        BorderPane bp = new BorderPane();
        VBox loginBox = new VBox();
        HBox hBox = new HBox();

        Label ipLabel = new Label("IP");
        TextField ipField = new TextField();
        Label portLabel = new Label("Port");
        TextField portField = new TextField();
        hBox.getChildren().addAll(ipLabel,ipField,portLabel,portField);
        loginBox.getChildren().add(hBox);

        hBox = new HBox();
        Label usernameLabel = new Label("Username");
        TextField usernameField = new TextField();

        hBox.getChildren().addAll(usernameLabel,usernameField);
        loginBox.getChildren().add(hBox);

        Button loginButton = new Button("Login");
        loginButton.setOnAction(event -> {
                if (validateUserInput(usernameField.getText())){
                    setSceneChat(primaryStage);
                }



            }
        );
        loginBox.getChildren().add(loginButton);
        bp.setCenter(loginBox);

        Scene scene = new Scene(bp, 1024, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean validateUserInput(String s){
        System.out.println(String.format("Username: %s",s));
        if (!s.isEmpty()){
            USERNAME = s;
            return true;
        }
        else { return false;}
    }
}
