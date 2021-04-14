package gui.controller;

import framework.ConfigData;
import gui.MainWindow;
import gui.model.MainMenuModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class MainMenuController extends Controller<MainMenuModel> {
    @FXML private Text actiontarget;

    @FXML public void pressTTTMenuButton(ActionEvent event) {
        mainWindow.switchView(MainWindow.ViewEnum.GAME_MENU);
        ConfigData.getInstance().setCurrentGameName("Tic-tac-toe");
    }

    @FXML public void pressOthelloMenuButton(ActionEvent event) {
        mainWindow.switchView(MainWindow.ViewEnum.GAME_MENU);
        ConfigData.getInstance().setCurrentGameName("Reversi");
    }

    @FXML public void pressExitButton(ActionEvent event) {
        mainWindow.quit();
        Platform.exit();
    }

    @FXML public void pressConfigurationButton(ActionEvent event){
        mainWindow.switchView((MainWindow.ViewEnum.GAME_CONFIGURATION));
    }
}
