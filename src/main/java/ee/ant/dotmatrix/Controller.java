package ee.ant.dotmatrix;

import ee.ant.dotmatrix.model.Segment;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import javax.xml.bind.DatatypeConverter;

public class Controller {

    @FXML
    private RadioButton rbutton58;
    @FXML
    private RadioButton rbutton68;
    @FXML
    private RadioButton rbutton78;
    @FXML
    private RadioButton rbutton88;
    @FXML
    private GridPane dotMatrix;
    @FXML
    private TextField hexString;
    @FXML
    private Button drawButton;

    private Segment[] segments;

    @FXML
    private void initialize() {


        // Handle 5x8 radio button event
        rbutton58.setOnAction(event -> {
            segments = new Segment[]{new Segment(5)};
            drawSegments();
        });

        // Handle 6x8 radio button event
        rbutton68.setOnAction(event -> {
            segments = new Segment[]{new Segment(6)};
            drawSegments();
        });

        // Handle 7x8 radio button event
        rbutton78.setOnAction(event -> {
            segments = new Segment[]{new Segment(7)};
            drawSegments();
        });

        // Handle 8x8 radio button event
        rbutton88.setOnAction(event -> {
            segments = new Segment[]{new Segment(8)};
            drawSegments();
        });

        // Handle draw button event
        drawButton.setOnAction(event -> parseHexString(hexString.getText()));

        // default is 5x8 matrix
        segments = new Segment[]{new Segment(5)};
        drawSegments();
    }

    /**
     * Draw all segments
     */
    private void drawSegments() {
        dotMatrix.getChildren().clear();
        for (Segment segment : segments) {
            for (int i = 0; i < segment.getColumns(); i++) {
                for (int j = 0; j < 8; j++) {
                    Button btn = new Button();
                    btn.setMinSize(16.0, 16.0);
                    btn.setMaxSize(16.0, 16.0);
                    btn.setOnAction(((ActionEvent click) -> {
                        ObservableList classes = btn.getStyleClass();
                        if (classes.size() == 1) {
                            btn.getStyleClass().add("buttonOn");
                        } else {
                            btn.getStyleClass().remove(1);
                        }
                        generateHexString();
                    }));
                    dotMatrix.add(btn, i, j);
                }
            }
        }
    }

    private void parseHexString(String text) {
        // Assume that hex string contains words separated by comas like
        // 0x00, 0x07, 0x05, 0x07, 0x00
        // create corresponding byte[] array
        String binaryString = text.replace("{", "")
                .replace("}", "")
                .replace("0x", "")
                .replace(",", "")
                .replace(" ", "");
        byte[] bytes = DatatypeConverter.parseHexBinary(binaryString);

        if (bytes.length > 8) {
            // it is 4x segment symbol
            // TODO 4 segment
        } else {
            // it is 1 segment symbol
            segments = new Segment[]{new Segment(bytes.length)};
            fillMatrixWithDots(bytes, 8);
        }
    }

    /**
     * Draw symbol from bytes array
     *
     * @param bytes byte array that encodes symbol
     */
    private void fillMatrixWithDots(byte[] bytes, int rows) {
        for (int i = 0; i < bytes.length; i++) {
            byte column = bytes[i];
            for (int j = 0; j < rows; j++) {
                if ((column & 1) == 1) {
                    dotMatrix.getChildren().get(i * 8 + j).getStyleClass().add("buttonOn");
                }
                column = (byte) (column >> 1);
            }
        }
    }

//    /**
//     * Creates empty matrix of buttons
//     * @param x size X
//     * @param y size Y
//     */
//    private void createMatrix (int x, int y) {
//        dotMatrix.getChildren().clear();
//        for (int i=0; i<x; i++) {
//            for (int j=0; j<y; j++) {
//                Button btn = new Button();
//                btn.setId(i + "," + j);
//                btn.setMinSize(16.0, 16.0);
//                btn.setMaxSize(16.0, 16.0);
//                btn.setOnAction(((ActionEvent click) -> {
//                    ObservableList classes = btn.getStyleClass();
//                    if (classes.size() == 1) {
//                        btn.getStyleClass().add("buttonOn");
//                    } else {
//                        btn.getStyleClass().remove(1);
//                    }
//                    generateHexString();
//                }));
//                dotMatrix.add(btn, i, j);
//            }
//        }
//    }

    /**
     * Create font hex string and display it
     */
    private void generateHexString() {
        StringBuilder hexOut = new StringBuilder();
        ObservableList<Node> buttons = dotMatrix.getChildren();
        byte element = 0; // initial byte
        for (int i = 0; i < buttons.size(); i++) {
            Node button = buttons.get(i);
            // reset element each 8 bits
            if (i % 8 == 0) {
                element = 0;
            }

            // set bit of element if corresponding button has 2 styles (i.e. pushed)
            if (button.getStyleClass().size() == 2) {
                element = (byte) (element | (1 << (i % 8)));
            }

            // append element to array at the end
            if ((i % 8) == 7) {
                hexOut.append("0x").append(String.format("%02x", element)).append(",");
            }
        }
        String out = hexOut.toString();
        hexString.setText("{ " + out.substring(0, out.length() - 1) + " }");
    }
}
