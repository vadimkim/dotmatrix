package ee.ant.dotmatrix;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.GridPane;

public class Controller {

    @FXML
    private RadioButton rbutton68;

    @FXML
    private GridPane dotMatrix;

    @FXML
    private void initialize() {

        // Handle 6x8 radio button event
        rbutton68.setOnAction((event) -> {
            createMatrix(6,8);
        });

        createMatrix(5,8);
    }

    private void createMatrix (int x, int y) {
        dotMatrix.getChildren().clear();
        for (int i=0; i<=x; i++) {
            for (int j=0; j<=y; j++) {
                Button btn = new Button();
                btn.setId(i + "," + j);
                btn.setMinSize(16.0, 16.0);
                btn.setMaxSize(16.0, 16.0);
                btn.setOnAction(((ActionEvent click) -> {

                    ObservableList classes = btn.getStyleClass();
                    if (classes.size() == 1) {
                        btn.getStyleClass().add("buttonOn");
                    } else {
                        btn.getStyleClass().remove(1);
                    }
                }));
                dotMatrix.add(btn, i, j);
            }
        }
    }
}
