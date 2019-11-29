package ee.ant.dotmatrix;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrimaryController {

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
    private MenuItem aboutDialog;

    @FXML
    private void initialize() {
        // show AboutDialog
        aboutDialog.setOnAction(event -> showAbout());

        // Handle 5x8 radio button event
        rbutton58.setOnAction(event -> createEmptyMatrix(8, 5));

        // Handle 6x8 radio button event
        rbutton68.setOnAction(event -> createEmptyMatrix(8, 6));

        // Handle 7x8 radio button event
        rbutton78.setOnAction(event -> createEmptyMatrix(8, 7));

        // Handle 8x8 radio button event
        rbutton88.setOnAction(event -> createEmptyMatrix(8, 8));

        // Handle 10x16 radio button event
        rbutton1016.setOnAction(event -> createEmptyMatrix(16, 10));

        // Handle 12x16 radio button event
        rbutton1216.setOnAction(event -> createEmptyMatrix(16, 12));

        // Handle 14x16 radio button event
        rbutton1416.setOnAction(event -> createEmptyMatrix(16, 14));

        // Handle 16x16 radio button event
        rbutton1616.setOnAction(event -> createEmptyMatrix(16, 16));

        // Handle draw button event
        drawButton.setOnAction(event -> {
            try {
                parseHexString(hexString.getText());
            } catch (DecoderException e) {
                e.printStackTrace();
            }
        });

        // default is 5x8 matrix
        createEmptyMatrix(8, 5);
    }

    /**
     * Show promotion
     */
    private void showAbout() {
        var uri = "www.ant.ee";
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About author");
        alert.setHeaderText("");
        alert.setGraphic(new ImageView(new Image("ee/ant/dotmatrix/ant.png")));
        var link = new Hyperlink(uri);
        link.setOnAction(event -> App.openURL(uri + "/?dotmatrix"));
        alert.getDialogPane().contentProperty().set(link);
        alert.showAndWait();
    }

    /**
     * Draw empty matrix and register draw events
     *
     * @param rows    number of raws
     * @param columns number of columns
     */
    private void createEmptyMatrix(int rows, int columns) {
        dotMatrix.getChildren().clear();
        hexString.clear();
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                var btn = new Button();
                btn.setMinSize(16.0, 16.0);
                btn.setMaxSize(16.0, 16.0);
                btn.setOnAction(((ActionEvent click) -> {
                    var classes = btn.getStyleClass();
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
            drawLines(columns);
        }
    }

    /**
     * Draw vertical and horizontal lines
     *
     * @param columns number of columns
     */
    private void drawLines(int columns) {
        // Draw horizontal line
        for (int i = 0; i < columns; i++) {
            var btn = (Button) dotMatrix.getChildren().get(2 * i * 8 + 7);
            btn.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 1, 0))));
        }

        // Draw vertical line
        for (int i = 0; i < 16; i++) {
            var btn = (Button) dotMatrix.getChildren().get(i + 8 * columns);
            if (i == 7) { // One button has both bottom and left lines
                btn.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 1, 1))));
            } else {
                btn.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 0, 1))));
            }
        }
    }

    /**
     * Parse hex string and draw symbol
     *
     * @param text - line with symbol hex code. Either for single segment or 4 segments
     */
    private void parseHexString(String text) throws DecoderException {
        // Assume that hex string contains words separated by comas like
        // 0x00, 0x07, 0x05, 0x07, 0x00
        // create corresponding byte[] array
        var binaryString = text.replace("{", "")
                .replace("}", "")
                .replace("0x", "")
                .replace(",", "")
                .replace(" ", "");

        var bytes = Hex.decodeHex(binaryString);
        fillMatrixWithDots(bytes);
    }

    /**
     * Draw symbol from bytes array. It can be either single segment or 4 segments if number of bytes is
     * grater than 8
     *
     * @param bytes byte array that encodes symbol
     */
    private void fillMatrixWithDots(byte[] bytes) {
        var colums = bytes.length;
        if (bytes.length > 8) {
            // there are 4 segments
            colums = bytes.length / 2;
            createEmptyMatrix(16, colums);
            drawSegment(0, 0, Arrays.copyOfRange(bytes, 0, bytes.length / 4));
            drawSegment(0, bytes.length / 4, Arrays.copyOfRange(bytes, bytes.length / 4, bytes.length / 2));
            drawSegment(8, 0, Arrays.copyOfRange(bytes, bytes.length / 2, 3 * bytes.length / 4));
            drawSegment(8, bytes.length / 4, Arrays.copyOfRange(bytes, 3 * bytes.length / 4, bytes.length));
        } else {
            // there is only one segment
            createEmptyMatrix(8, colums);
            drawSegment(bytes);
        }

        // select radiobutton of corresponding size
        switch (colums) {
            case 5: rbutton58.setSelected(true);
                break;
            case 6: rbutton68.setSelected(true);
                break;
            case 7: rbutton78.setSelected(true);
                break;
            case 8: rbutton88.setSelected(true);
                break;
            case 10: rbutton1016.setSelected(true);
                break;
            case 12: rbutton1216.setSelected(true);
                break;
            case 14: rbutton1416.setSelected(true);
                break;
            case 16: rbutton1616.setSelected(true);
        }
    }

    /**
     * Draw single segment
     *
     * @param bytes array of hex for single element
     */
    private void drawSegment(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            var column = bytes[i];
            for (int j = 0; j < 8; j++) {
                if ((column & 1) == 1) {
                    dotMatrix.getChildren().get(i * 8 + j).getStyleClass().add("buttonOn");
                }
                column = (byte) (column >> 1);
            }
        }
    }

    /**
     * Draw segment based on offset. Top left has 0,0 and bottom right 8, number of columns. Used
     * for multi-segment character
     *
     * @param offsetCol - column offset
     * @param offsetRaw - raw offset
     * @param bytes      - segment data
     */
    private void drawSegment(int offsetCol, int offsetRaw, byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            var column = bytes[i];
            for (int j = offsetCol; j < offsetCol + 8; j++) {
                if ((column & 1) == 1) {
                    dotMatrix.getChildren().get(i * 16 + j + offsetRaw * 16).getStyleClass().add("buttonOn");
                }
                column = (byte) (column >> 1);
            }
        }
    }

    /**
     * Create font hex string and display it
     */
    private void generateHexString() {
        var hexOut = new StringBuilder();

        if (dotMatrix.getChildren().size() > 64) {
            // Multi- segment
            hexOut.append(readSegment(0,0));
            hexOut.append(readSegment(dotMatrix.getChildren().size() / 32, 0));
            hexOut.append(readSegment(0, 8));
            hexOut.append(readSegment(dotMatrix.getChildren().size() / 32, 8));
        } else {
            // Single segment
            hexOut.append(getSegmentHex(dotMatrix.getChildren()));
        }

        var out = hexOut.toString();
        hexString.setText("{ " + out.substring(0, out.length() - 1) + " }");
    }

    /**
     * Create hex string for segment based on offset. Used for multi-segment
     * hex string calculation
     *
     * @param offsetCol column offset
     * @param offsetRaw raw offset
     * @return hex string for the segment
     */
    private char[] readSegment(int offsetCol, int offsetRaw) {

        var segment = new ArrayList<Node>();
        for (int i=offsetCol; i < offsetCol + dotMatrix.getChildren().size()/32; i++) {
            for (int j=offsetRaw; j < offsetRaw + 8; j++) {
                segment.add(dotMatrix.getChildren().get(i*16 + j));
            }
        }

        return getSegmentHex(segment).toCharArray();
    }

    /**
     * Calculates hex codes and return string for single segment 8*columns
     * @param segment buttons node list in a segment
     * @return hex string
     */
    private String getSegmentHex(List<Node> segment) {
        var hexOut = new StringBuilder();
        // Single segment code
        byte element = 0; // initial byte
        for (int i = 0; i < segment.size(); i++) {
            var button = segment.get(i);
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
        return hexOut.toString();
    }

}
