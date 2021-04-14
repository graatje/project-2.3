package project23.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import project23.framework.ConfigData;
import project23.framework.GameManager;
import project23.gui.MainWindow;
import project23.gui.model.GameModel;

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

    @FXML
    public void pressBackToMainMenu(ActionEvent event) {
        mainWindow.switchView(MainWindow.ViewEnum.MAINMENU);
        GameManager gm = ConfigData.getInstance().getGameManager();
        gm.destroy();
    }

    @FXML
    public void onMouseReleased(MouseEvent event) {
        model.clickTile(event.getX(), event.getY());
    }

    public void pressRetry(ActionEvent event) {
        model.restartGame();
    }
}
