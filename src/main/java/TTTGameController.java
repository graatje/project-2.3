import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class TTTGameController{
    @FXML
    private Pane TTTBoard;

    public TTTGameController() {
        int gridSize = 4;
        int boardWidth = 640; // later: grootte dynamisch opvragen?
        int boardHeight = 480;
        float cellWidth = boardWidth/(gridSize+1);

        // 4 gridboxes => 5 lines |1|2|3|4|
        for(int i=0;i<gridSize+1;i++) {
            float x = cellWidth*i;

            Line horizontalDivider = new Line();
            horizontalDivider.setStartX(x);
            horizontalDivider.setStartY(0.0f);
            horizontalDivider.setEndX(x);
            horizontalDivider.setEndY(100.0f);

            TTTBoard.getChildren().add(horizontalDivider);
        }
    }
}
