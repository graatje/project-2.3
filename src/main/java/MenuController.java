import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class MenuController {
    @FXML private Text actiontarget;

    @FXML protected void pressTTTMenuButton(ActionEvent event) {
        actiontarget.setText("BKE pressed");
    }

    @FXML protected void pressOthelloMenuButton(ActionEvent event) {
        actiontarget.setText("Othello pressed");
    }

    @FXML public void pressCreditsButton(ActionEvent event) {
        actiontarget.setText("Credits pressed");
    }

    @FXML public void pressExitButton(ActionEvent event) {
        actiontarget.setText("Exit pressed");
    }
}
