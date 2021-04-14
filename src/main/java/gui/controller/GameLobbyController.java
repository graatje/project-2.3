package gui.controller;

import gui.MainWindow;
import gui.model.GameLobbyModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class GameLobbyController extends Controller {

    @FXML
    private ListView challengerList;
    @FXML
    private CheckBox isAIBox;

    public void pressBackToMainMenu(ActionEvent event) {
        mainWindow.switchView(MainWindow.viewEnum.MAINMENU);
    }

    public void pressRefresh(ActionEvent event) {
        model.updateView();
    }

    public void pressChallengePlayer() {
        ((GameLobbyModel)model).challengePlayer((int) challengerList.getSelectionModel().getSelectedIndices().get(0));
    }

    /**
     * This is the functionality of the old "Play online" button
     */
    public void pressSubscribe() {
        ((GameLobbyModel)model).prepareOnlineGame(isAIBox.isSelected());
        mainWindow.switchView(MainWindow.viewEnum.GAME);
    }
}
