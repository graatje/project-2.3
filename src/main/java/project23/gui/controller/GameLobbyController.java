package project23.gui.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import project23.gui.MainWindow;
import project23.gui.model.GameLobbyModel;

import java.net.URL;
import java.util.ResourceBundle;

public class GameLobbyController extends Controller<GameLobbyModel> implements Initializable {

    @FXML
    private ListView challengerList;
    @FXML
    private CheckBox isAIBox;
    @FXML
    private Label messageLabel;

    public void pressBackToMainMenu(ActionEvent event) {
        model.logout();
        mainWindow.switchView(MainWindow.ViewEnum.MAINMENU);
    }

    public void pressRefresh(ActionEvent event) {
        model.refreshPlayerList();
    }

    public void pressChallengePlayer() {
        ObservableList<Integer> indices = challengerList.getSelectionModel().getSelectedIndices();
        if (!indices.isEmpty()) {
            model.challengePlayer(indices.get(0));
        } else {
            model.setInfoMessage("Please select a player to challenge.");
            model.updateView();
        }
    }

    /**
     * This is the functionality of the old "Play online" button
     */
    public void pressSubscribe() {
        model.prepareOnlineGame();
        mainWindow.switchView(MainWindow.ViewEnum.GAME);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set default value
        model.setAI(isAIBox.isSelected());
        model.setLabelNode(messageLabel);

        // Listen for changes
        isAIBox.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            model.setAI(newValue);
        });
    }
}
