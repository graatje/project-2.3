import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;

public class MenuController {
    @FXML private Text actiontarget;

    @FXML
    public void pressTTTMenuButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("TTTMenu.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 750,750));
        stage.show();
    }

    @FXML protected void pressOthelloMenuButton(ActionEvent event) {
        actiontarget.setText("Othello pressed");
    }

    @FXML public void pressExitButton(ActionEvent event) {
        Platform.exit();
    }
}
