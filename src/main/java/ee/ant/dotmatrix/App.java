package ee.ant.dotmatrix;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static HostServices services;

    @Override
    public void start(Stage primaryStage) throws Exception{
        services = getHostServices();
        Parent root = FXMLLoader.load(getClass().getResource("primary.fxml"));
        primaryStage.setTitle("Dot matrix font generator");
        primaryStage.getIcons().add(new Image("ee/ant/dotmatrix/ant.png"));
        var main = new Scene(root, 800, 500);
        main.getStylesheets().add("ee/ant/dotmatrix/styles.css");
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