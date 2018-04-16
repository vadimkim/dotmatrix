package ee.ant.dotmatrix;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

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
    private RadioButton rbutton1016;
    @FXML
    private RadioButton rbutton1216;
    @FXML
    private RadioButton rbutton1416;
    @FXML
    private RadioButton rbutton1616;
    @FXML
    private GridPane dotMatrix;
    @FXML
    private TextField hexString;
    @FXML
    private Button drawButton;

    @FXML
    private void initialize() {

        // Handle 5x8 radio button event
        rbutton58.setOnAction(event -> createEmptyMatrix(8,5));

        // Handle 6x8 radio button event
        rbutton68.setOnAction(event -> createEmptyMatrix(8,6));

        // Handle 7x8 radio button event
        rbutton78.setOnAction(event -> createEmptyMatrix(8,7));

        // Handle 8x8 radio button event
        rbutton88.setOnAction(event -> createEmptyMatrix(8,8));

        // Handle 10x16 radio button event
        rbutton1016.setOnAction(event -> createEmptyMatrix(16,10));

        // Handle 12x16 radio button event
        rbutton1216.setOnAction(event -> createEmptyMatrix(16,12));

        // Handle 14x16 radio button event
        rbutton1416.setOnAction(event -> createEmptyMatrix(16,14));

        // Handle 16x16 radio button event
        rbutton1616.setOnAction(event -> createEmptyMatrix(16,16));

        // Handle draw button event
        drawButton.setOnAction(event -> parseHexString(hexString.getText()));

        // default is 5x8 matrix
        createEmptyMatrix(8,5);
    }

    /**
     * Draw empty matrix and register draw events
     * @param rows number of raws
     * @param columns number of columns
     */
    private void createEmptyMatrix(int rows, int columns) {
        dotMatrix.getChildren().clear();
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
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
        if (rows == 16) {
            drawLines (columns);
        }
    }

    /**
     * Draw vertical and horizontal lines
     * @param columns number of columns
     */
    private void drawLines(int columns) {
        // Draw horizontal line
        for (int i = 0; i < columns ; i++) {
            Button btn = (Button) dotMatrix.getChildren().get(2*i*8 + 7);
            btn.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0,0,1,0))));
        }

        // Draw vertical line
        for (int i = 0; i < 16 ; i++) {
            Button btn = (Button) dotMatrix.getChildren().get(i + 8*columns);
            if (i == 7) { // One button has both bottom and left lines
                btn.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0,0,1,1))));
            } else {
                btn.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0,0,0,1))));
            }
        }
    }

    /**
     * Parse hex string and draw symbol
     * @param text - line with symbol hex code. Either for single segment or 4 segments
     */
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
        fillMatrixWithDots(bytes);
    }

    /**
     * Draw symbol from bytes array
     *
     * @param bytes byte array that encodes symbol
     */
    private void fillMatrixWithDots(byte[] bytes) {
        if (bytes.length > 8) {
            // there are 4 segments
            createEmptyMatrix(16, bytes.length / 2);
           drawSegment(0,0, Arrays.copyOfRange(bytes, 0, bytes.length / 4));
           drawSegment(0,bytes.length / 4, Arrays.copyOfRange(bytes, bytes.length / 4, bytes.length / 2));
           drawSegment(8,0, Arrays.copyOfRange(bytes, bytes.length / 2, 3 * bytes.length / 4));
           drawSegment(8,bytes.length / 4, Arrays.copyOfRange(bytes, 3 * bytes.length / 4,  bytes.length));
        } else {
            // there is only one segment
            createEmptyMatrix(8, bytes.length );
            drawSegment(bytes);
        }
    }

    /**
     * Draw single segment
     * @param bytes array of hex for single element
     */
    private void drawSegment(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            byte column = bytes[i];
            for (int j = 0; j < 8; j++) {
                if ((column & 1) == 1) {
                    dotMatrix.getChildren().get(i * 8 + j).getStyleClass().add("buttonOn");
                }
                column = (byte) (column >> 1);
            }
        }
    }

    /**
     * Draw segment based on offset. Top left has 0,0 and bottom right 8, number of columns
     * @param offsetCols - columns offset
     * @param offsetRaws - raws offset
     * @param bytes - segment data
     */
    private void drawSegment(int offsetCols, int offsetRaws, byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            byte column = bytes[i];
            for (int j = offsetCols; j < offsetCols + 8; j++) {
                if ((column & 1) == 1) {
                    dotMatrix.getChildren().get(i*16 + j + offsetRaws*16).getStyleClass().add("buttonOn");
                }
                column = (byte) (column >> 1);
            }
        }
    }

    /**
     * Create font hex string and display it
     */
    private void generateHexString() {
        // TODO refactor using segments
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
