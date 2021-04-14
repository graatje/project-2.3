package project23.gui.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import project23.framework.ConfigData;
import project23.framework.GameType;
import project23.gui.MainWindow;
import project23.gui.model.MainMenuModel;

public class MainMenuController extends Controller<MainMenuModel> {
    @FXML
    private Text actiontarget;

    @FXML
    public void pressTTTMenuButton(ActionEvent event) {
        mainWindow.switchView(MainWindow.ViewEnum.GAME_MENU);
        ConfigData.getInstance().setCurrentGameType(GameType.TIC_TAC_TOE);
    }

    @FXML
    public void pressOthelloMenuButton(ActionEvent event) {
        mainWindow.switchView(MainWindow.ViewEnum.GAME_MENU);
        ConfigData.getInstance().setCurrentGameType(GameType.OTHELLO);
    }

    @FXML
    public void pressExitButton(ActionEvent event) {
        mainWindow.quit();
        Platform.exit();
    }

    @FXML
    public void pressConfigurationButton(ActionEvent event) {
        mainWindow.switchView((MainWindow.ViewEnum.GAME_CONFIGURATION));
    }
}
