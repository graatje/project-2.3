package gui.controller;

import gui.MainWindow;
import gui.model.GameLobbyModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;

public class GameLobbyController extends Controller {

    @FXML
    private ListView challengerList;
    @FXML
    private CheckBox isAIBox;

    public void pressBackToMainMenu(ActionEvent event) {
        ((GameLobbyModel)model).logout();
        mainWindow.switchView(MainWindow.viewEnum.MAINMENU);
    }

    public void pressRefresh(ActionEvent event) {
        ((GameLobbyModel)model).refreshPlayerList();
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
