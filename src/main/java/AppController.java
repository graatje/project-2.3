import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class AppController {
    @FXML private Text actiontarget;

    @FXML protected void pressBKEButton(ActionEvent event) {
        actiontarget.setText("BKE pressed");
    }

    @FXML protected void pressOthelloButton(ActionEvent event) {
        actiontarget.setText("Othello pressed");
    }
}
