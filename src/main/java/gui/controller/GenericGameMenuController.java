package gui.controller;

import framework.ConfigData;
import framework.GameManager;
import gui.MainWindow;
import gui.model.GenericGameMenuModel;
import gui.model.GenericGameModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import othello.board.OthelloBoard;
import ttt.board.TTTBoard;

public class GenericGameMenuController extends Controller {

    @FXML Text usernameConfirmation;
    @FXML TextField usernameField;

    @FXML public void pressPlayAgainstComputer(ActionEvent event) {
        mainWindow.switchView(MainWindow.viewEnum.GAME);
        ((GenericGameMenuModel)model).setLocalGameManager();
    }

    @FXML public void pressOKUsername(ActionEvent event) {
        ((GenericGameMenuModel)model).setPlayerName(usernameField.getText());
        //TODO: setText() via model aanroepen ipv hier. (Bv als er foutmelding komt!)
        usernameConfirmation.setText("[TESTING] Username set to \""+usernameField.getText() +"\".");
    }

    public void pressPlayOnline(ActionEvent event) {
        ((GenericGameMenuModel)model).setOnlineGameManager();
        mainWindow.switchView(MainWindow.viewEnum.GAME);
    }

    @FXML public void pressBackToMainMenu(ActionEvent event) {
        mainWindow.switchView(MainWindow.viewEnum.MAINMENU);
    }
}
