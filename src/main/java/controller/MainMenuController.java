package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import java.io.IOException;


public class MainMenuController extends Controller {
    @FXML private Text actiontarget;

    @FXML
    public void pressTTTMenuButton(ActionEvent event) throws IOException {
        actiontarget.MainWindow.switchView(MainWindow.viewEnum.GAME_MENU);
    }

    @FXML protected void pressOthelloMenuButton(ActionEvent event) {
        //actiontarget.MainWindow.switchView(MainWindow.viewEnum.GAME_MENU);
    }

    @FXML public void pressExitButton(ActionEvent event) {
        Platform.exit();
    }
}
