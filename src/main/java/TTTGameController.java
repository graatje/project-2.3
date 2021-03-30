import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class TTTGameController{
    @FXML private Text actionGame;

    @FXML public void pressButton(ActionEvent event) {
        actionGame.setText("play game");
        }
}
