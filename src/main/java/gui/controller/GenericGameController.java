package gui.controller;

import framework.ConfigData;
import framework.ConnectedGameManager;
import framework.GameManager;
import gui.MainWindow;
import gui.model.GenericGameModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class GenericGameController extends Controller implements Initializable {
    @FXML
    private Pane board;
    @FXML
    private Text messageField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model.setTextNode(messageField);
        ((GenericGameModel)model).setBoardSize(board.getPrefWidth());
    }

    @FXML public void pressBackToMainMenu(ActionEvent event){
        mainWindow.switchView(MainWindow.viewEnum.MAINMENU);

        GameManager gm = ConfigData.getInstance().getGameManager();

        if (gm instanceof ConnectedGameManager){
            ((ConnectedGameManager) gm).closeClient();
        }

        ((GenericGameModel)model).clearBoard();
    }

    @FXML public void onMouseReleased(MouseEvent event) {
        ((GenericGameModel)model).clickTile(event.getX(), event.getY());
    }
}
