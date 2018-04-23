package ee.ant.dotmatrix;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    private static HostServices services;

    @Override
    public void start(Stage primaryStage) throws Exception{
        services = getHostServices();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("main.fxml")));
        primaryStage.setTitle("Dot matrix font generator");
        primaryStage.getIcons().add(new Image("ant.png"));
        Scene main = new Scene(root, 800, 500);
        main.getStylesheets().add("styles.css");
        primaryStage.setScene(main);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    static void openURL(String uri) {
        services.showDocument(uri);
    }
}
