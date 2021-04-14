package gui.controller;

import framework.ConfigData;
import gui.MainWindow;
import gui.model.GameLobbyModel;
import gui.model.GenericGameMenuModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class GenericGameMenuController extends Controller implements Initializable {

    @FXML
    Label usernameConfirmation;
    @FXML
    TextField usernameField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model.setLabelNode(usernameConfirmation);
    }

    @FXML
    public void pressOKUsername(ActionEvent event) {
        ((GenericGameMenuModel) model).setPlayerName(usernameField.getText());
        model.setInfoMessage("Username set to \"" + usernameField.getText() + "\".");
        model.updateView();
    }

    /**
     * Start a local game against AI
     * @param event
     */
    @FXML
    public void pressPlayAgainstComputer(ActionEvent event) {
        ((GenericGameMenuModel) model).prepareOfflineGame();
        mainWindow.switchView(MainWindow.viewEnum.GAME);
    }

    /**
     * Go to lobby
     * @param event
     */
    public void pressGoToLobby(ActionEvent event) {
        ((GenericGameMenuModel) model).prepareLobby();
        mainWindow.switchView(MainWindow.viewEnum.GAME_LOBBY);
    }

    @FXML
    public void pressBackToMainMenu(ActionEvent event) {
        mainWindow.switchView(MainWindow.viewEnum.MAINMENU);
    }

    @FXML
    public void pressHelp(ActionEvent event){
        model.setDialogMessage(ConfigData.getInstance().getCurrentGame().getHelpText());
        model.updateView();
    }
}