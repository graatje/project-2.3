import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class AppController {
    @FXML private Text actiontarget;

    @FXML protected void pressBKEMenuButton(ActionEvent event) {
        actiontarget.setText("BKE pressed");
    }

    @FXML protected void pressOthelloMenuButton(ActionEvent event) {
        actiontarget.setText("Othello pressed");
    }
}
