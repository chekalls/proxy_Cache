package Navigateur;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.*;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class Main extends Application {

    private TextField urlField;
    private WebView webView;
    private WebEngine webEngine;
    @Override
    public void start(Stage primaryStage){
        webView = new WebView();

        webEngine = webView.getEngine();

        //URL par défaut
        webEngine.load("https://google.com");

        urlField = new TextField();
        urlField.setPromptText("Entrez une url");
        urlField.setOnAction(e -> navigateTo(urlField.getText()));

        HBox navigationBar = getHBox();

        BorderPane root = new BorderPane();
        root.setTop(navigationBar);
        root.setCenter(webView);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Navigateur Web JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox getHBox() {
        Button backButton = new Button("<");
        Button forwardButton = new Button(">");
        Button refreshButton = new Button("refresh");

        backButton.setOnAction(e -> {
            if (webEngine.getHistory().getCurrentIndex() > 0) {
                webEngine.getHistory().go(-1);
            }
        });

        forwardButton.setOnAction(e -> {
            if (webEngine.getHistory().getCurrentIndex() < webEngine.getHistory().getEntries().size() - 1) {
                webEngine.getHistory().go(1);
            }
        });

        refreshButton.setOnAction(e -> webEngine.reload());

        GridPane pane = new GridPane();
        HBox navigationBar = new HBox(10,backButton,forwardButton,urlField,refreshButton);
        navigationBar.setStyle("-fx-padding: 10; -fx-background-color: #e0e0e0;");
        return navigationBar;
    }

    private void navigateTo(String url) {
        if (url != null && !url.isEmpty()) {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url;
            }
            webEngine.load(url);
        }
    }


    public static void main(String[] args){launch(args);}
}