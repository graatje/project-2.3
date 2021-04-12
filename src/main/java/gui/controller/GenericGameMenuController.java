package gui.controller;

import framework.ConfigData;
import framework.GameType;
import gui.MainWindow;
import gui.model.GenericGameMenuModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

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
        model.setInfoMessage("Username set to \"" + usernameField.getText() + "\".");
        model.updateView();
    }

    @FXML
    public void pressPlayLocalOnline(ActionEvent event){
        switch (ConfigData.getInstance().getGameType()) {
            case TTT:
                ConfigData.getInstance().setGameType(GameType.TTT_LOCAL_ONLINE);
                break;
            case OTHELLO:
                ConfigData.getInstance().setGameType(GameType.OTHELLO_LOCAL_ONLINE);
                break;
        }
        ((GenericGameMenuModel) model).setGameManager();
        mainWindow.switchView(MainWindow.viewEnum.GAME);
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

    @FXML
    public void pressHelp(ActionEvent event){
        switch (ConfigData.getInstance().getGameType()) {
            case TTT:
                model.setDialogMessage("Rules Tic-Tac-Toe: \n"
                        + "1. The game is played on a grid that's 3 squares by 3 squares. \n"
                        + "2. You are X, your friend (or the computer in this case) is O. Players take turns putting their marks in empty squares. \n"
                        + "3. The first player to get 3 of her marks in a row (up, down, across, or diagonally) is the winner. \n"
                        + "4. When all 9 squares are full, the game is over. If no player has 3 marks in a row, the game ends in a tie."
                );
                break;
            case OTHELLO:
                model.setDialogMessage("Rules Othello: \n"
                        + "Othello is a strategy board game played between 2 players. One player plays black and the other white.\n"
                        + "Then the game alternates between white and black until:\n"
                        + "    * one player can not make a valid move to outflank the opponent.\n"
                        + "    * both players have no valid moves.\n"
                        + "When a player has no valid moves, he passes his turn and the opponent continues.\n"
                        + "A player can not voluntarily forfeit his turn.\n"
                        + "When both players can not make a valid move the game ends.\n"
                        + "\nValid Moves\n"
                        + "A move is made by placing a disc of the player's color on the board in a position that outflanks one or more of the opponent's discs.\n"
                        + "A disc or row of discs is outflanked when it is surrounded at the ends by discs of the opposite color.\n"
                        + "A disc may outflank any number of discs in one or more rows in any direction (horizontal, vertical, diagonal).\n"
                        + "All the discs which are outflanked will be flipped, even if it is to the player's advantage not to flip them.\n"
                        + "Discs may only be outflanked as a direct result of a move and must fall in the direct line of the disc being played.\n"
                        + "If you can't outflank and flip at least one opposing disc, you must pass your turn. However, if a move is available to you, you can't forfeit your turn.\n"
                        + "Once a disc has been placed on a square, it can never be moved to another square later in the game.\n"
                        + "When a player runs out of discs, but still has opportunities to outflank an opposing disc, then the opponent must give the player a disc to use.\n"
                );
                break;
        }
        model.updateView();
    }
}