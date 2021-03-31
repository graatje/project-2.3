import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class TTTMenuController {
    @FXML public void pressPlayAgainstComputer(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("TTTGame.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 750,750));
        stage.show();
    }

    @FXML public void pressOKUsername(ActionEvent event) {

    }

    public void pressPlayOnline(ActionEvent event) {
    }
}
