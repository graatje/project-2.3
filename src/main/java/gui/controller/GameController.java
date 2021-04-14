package gui.controller;

import framework.ConfigData;
import framework.GameManager;
import gui.MainWindow;
import gui.model.GameModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController extends Controller<GameModel> implements Initializable {
    @FXML
    private Pane board;
    @FXML
    private Label messageField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model.setLabelNode(messageField);
        model.setBoardSize(board.getPrefWidth());
    }

    @FXML public void pressBackToMainMenu(ActionEvent event){
        mainWindow.switchView(MainWindow.ViewEnum.MAINMENU);
        GameManager gm = ConfigData.getInstance().getGameManager();
        gm.destroy();
    }

    @FXML public void onMouseReleased(MouseEvent event) {
        model.clickTile(event.getX(), event.getY());
    }

    public void pressRetry(ActionEvent event) {
        model.restartGame();
    }
}
