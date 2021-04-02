package gui.controller;

import gui.MainWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;

public class GenericGameMenuController extends Controller {
    @FXML public void pressPlayAgainstComputer(ActionEvent event) {
        mainWindow.switchView(MainWindow.viewEnum.GAME);
    }

    @FXML public void pressOKUsername(ActionEvent event) {
    }

    public void pressPlayOnline(ActionEvent event) {
    }
}
