package gui.controller;

import framework.ConfigData;
import gui.MainWindow;
import gui.model.GenericGameMenuModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class GenericGameMenuController extends Controller<GenericGameMenuModel> implements Initializable {

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
        model.setPlayerName(usernameField.getText());
        model.setInfoMessage("Username set to \"" + usernameField.getText() + "\".");
        model.updateView();
    }

    /**
     * Start a local game against AI
     */
    @FXML
    public void pressPlayAgainstComputer(ActionEvent event) {
        model.prepareOfflineGame();
        mainWindow.switchView(MainWindow.ViewEnum.GAME);
    }

    /**
     * Go to lobby
     */
    public void pressGoToLobby(ActionEvent event) {
        model.prepareLobby();
        mainWindow.switchView(MainWindow.ViewEnum.GAME_LOBBY);
    }

    @FXML
    public void pressBackToMainMenu(ActionEvent event) {
        mainWindow.switchView(MainWindow.ViewEnum.MAINMENU);
    }

    @FXML
    public void pressHelp(ActionEvent event){
        model.setDialogMessageAndTitle(ConfigData.getInstance().getCurrentGame().getHelpText(), "Help");
        model.updateView();
    }
}