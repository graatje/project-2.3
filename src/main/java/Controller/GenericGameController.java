package Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import java.net.URL;
import java.util.ResourceBundle;

public class GenericGameController extends Controller implements Initializable {
    @FXML
    private Pane TTTBoard;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int gridSize = 3;
        double boardSize = TTTBoard.getPrefWidth();
        System.out.println("board size: "+boardSize);

        // 3 gridboxes => 2 lines 1|2|3
        for(int i=0;i<gridSize-1;i++) {
            double pos = (double) (i+1)/gridSize*boardSize;

            Line horizontalDivider = new Line();
            horizontalDivider.setStartX(pos);
            horizontalDivider.setStartY(0.0f);
            horizontalDivider.setEndX(pos);
            horizontalDivider.setEndY(boardSize);

            Line verticalDivider = new Line();
            verticalDivider.setStartX(0.0f);
            verticalDivider.setStartY(pos);
            verticalDivider.setEndX(boardSize);
            verticalDivider.setEndY(pos);

            TTTBoard.getChildren().add(horizontalDivider);
            TTTBoard.getChildren().add(verticalDivider);
        }
    }
}
