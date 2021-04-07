package gui.controller;

import framework.ConfigData;
import framework.GameType;
import gui.MainWindow;
import gui.model.GenericGameMenuModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class GenericGameMenuController extends Controller {

    @FXML
    Text usernameConfirmation;
    @FXML
    TextField usernameField;

    @FXML
    public void pressPlayAgainstComputer(ActionEvent event) {
        switch (ConfigData.getInstance().getGameType()) {
            case TTT:
                ConfigData.getInstance().setGameType(GameType.TTT_LOCAL);
                break;
            case OTHELLO:
                ConfigData.getInstance().setGameType(GameType.OTHELLO_LOCAL);
                break;
        }

        ((GenericGameMenuModel) model).setGameManager();
        mainWindow.switchView(MainWindow.viewEnum.GAME);
    }

    @FXML
    public void pressOKUsername(ActionEvent event) {
        ((GenericGameMenuModel) model).setPlayerName(usernameField.getText());
        //TODO: setText() via model aanroepen ipv hier. (Bv als er foutmelding komt!)
        usernameConfirmation.setText("[TESTING] Username set to \"" + usernameField.getText() + "\".");
    }

    public void pressPlayOnline(ActionEvent event) {
        switch (ConfigData.getInstance().getGameType()) {
            case TTT:
                ConfigData.getInstance().setGameType(GameType.TTT_ONLINE);
                break;
            case OTHELLO:
                ConfigData.getInstance().setGameType(GameType.OTHELLO_ONLINE);
                break;
        }
        ((GenericGameMenuModel) model).setGameManager();
        mainWindow.switchView(MainWindow.viewEnum.GAME);
    }

    @FXML
    public void pressBackToMainMenu(ActionEvent event) {
        mainWindow.switchView(MainWindow.viewEnum.MAINMENU);
    }
}
