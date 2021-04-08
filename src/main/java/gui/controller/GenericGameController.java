package gui.controller;

import framework.ConfigData;
import framework.ConnectedGameManager;
import framework.GameManager;
import gui.MainWindow;
import gui.model.GenericGameModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class GenericGameController extends Controller {
    @FXML
    private Pane TTTBoard;

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
