package gui.controller;

import framework.ConfigData;
import framework.GameType;
import gui.MainWindow;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class MainMenuController extends Controller {
    @FXML private Text actiontarget;

    @FXML public void pressTTTMenuButton(ActionEvent event) {
        mainWindow.switchView(MainWindow.viewEnum.GAME_MENU);
        ConfigData.getInstance().setGameType(GameType.TTT);
    }

    @FXML public void pressOthelloMenuButton(ActionEvent event) {
        mainWindow.switchView(gui.MainWindow.viewEnum.GAME_MENU);
        ConfigData.getInstance().setGameType(GameType.OTHELLO);
    }

    @FXML public void pressExitButton(ActionEvent event) {
        //TODO: via mainwindow?
        Platform.exit();
    }

    @FXML public void pressConfigurationButton(ActionEvent event){
        mainWindow.switchView((MainWindow.viewEnum.GAME_CONFIGURATION));
    }
}
