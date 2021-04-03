package gui.controller;

import gui.MainWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import java.net.URL;
import java.util.ResourceBundle;

public class GenericGameController extends Controller implements Initializable {
    @FXML
    private Pane TTTBoard;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML public void pressBackToMainMenu(ActionEvent event){
        mainWindow.switchView(MainWindow.viewEnum.MAINMENU);
    }
}
