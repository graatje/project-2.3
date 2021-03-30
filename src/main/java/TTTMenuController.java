import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class TTTMenuController {
    @FXML private Text doSomething;

    @FXML public void pressPlayAgainstComputer(ActionEvent event) {
        doSomething.setText("Speel tegen de computer");
    }

    @FXML public void pressPlayOnline(ActionEvent event) {
        doSomething.setText("Speel online");
    }

    @FXML public void pressSettings(ActionEvent event) {
        doSomething.setText("Instellingen");
    }
}
