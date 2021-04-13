package gui.controller;

import framework.ConfigData;
import framework.ConnectedGameManager;
import gui.MainWindow;
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
    public void pressPlayAgainstComputer(ActionEvent event) {
        ((GenericGameMenuModel) model).setGameManager();
        ConfigData.getInstance().getCurrentGame().setOnline(false);
        ConfigData.getInstance().getCurrentGame().isAI(false);
        System.out.println("Starting..."+ (ConfigData.getInstance().getGameManager()));
        ConfigData.getInstance().getGameManager().requestStart();
        mainWindow.switchView(MainWindow.viewEnum.GAME);
    }

    @FXML
    public void pressOKUsername(ActionEvent event) {
        ((GenericGameMenuModel) model).setPlayerName(usernameField.getText());
        model.setInfoMessage("Username set to \"" + usernameField.getText() + "\".");
        model.updateView();
    }

    @FXML
    public void pressPlayLocalOnline(ActionEvent event){
        ((GenericGameMenuModel) model).setGameManager();
        mainWindow.switchView(MainWindow.viewEnum.GAME);
    }

    public void pressPlayOnline(ActionEvent event) {
        ConfigData.getInstance().getCurrentGame().setOnline(true);
        ((GenericGameMenuModel) model).setGameManager();
        mainWindow.switchView(MainWindow.viewEnum.GAME_LOBBY); //TODO
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