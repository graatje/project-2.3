package gui.model;

import framework.ConfigData;
import framework.GameManager;
import framework.board.Board;
import framework.board.BoardObserver;
import framework.board.BoardPiece;
import framework.player.LocalPlayer;
import framework.player.Player;
import gui.view.GameView;
import gui.view.View;
import javafx.application.Platform;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class GenericGameModel extends Model implements BoardObserver {

    private Board board;
    private GameManager gameManager;
    private double boardSize = 500; //TODO: opvragen van fxml?
    private ArrayList<Integer> colors;

    /**
     * Sets gameManager and board variables, and registers this model as observer.
     */
    public void setupGameManager() {
        gameManager = ConfigData.getInstance().getGameManager();
        board = gameManager.getBoard();
        board.registerObserver(this);
    }

    public void clickTile(double x, double y) {
        String infoMessage = "";
        double cellSize = boardSize / board.getWidth();
        int xTile = (int) Math.floor(x / cellSize);
        int yTile = (int) Math.floor(y / cellSize);

        //TODO: meegeven met framework
        Player player = gameManager.getBoard().getCurrentPlayer();

        if (player == null){
            return;
        }

        if (player instanceof LocalPlayer) {
            if (gameManager.getBoard().isValidMove(xTile, yTile)) {
                ((LocalPlayer) player).executeMove(xTile, yTile);
            } else {
                infoMessage = "Invalid move";
            }
        } else {
            infoMessage = "Wait for your turn";
        }
        if(!infoMessage.isEmpty()) {
            for (View view : observers) {
                ((GameView) view).setInfoText(infoMessage);
            }
        }

    }

    public Board getBoard() {
        return board;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    @Override
    public void onPlayerMoved(Player who, BoardPiece where) {
        // nothing here
    }

    @Override
    public void onPlayerMoveFinalized(Player previous, Player current) {
        Platform.runLater(this::updateView);
    }

    @Override
    public void onPlayerWon(Player who) {
        Platform.runLater(() -> {

            String message = null;

            if (who == null) {
                message = "It's a draw";
            } else {
                message = who.getName() + " has won!";
            }

            for (View view : observers) {
                view.showDialog(message);
            }
        });
    }

    public void clearBoard(){
        for (View view : observers) {
            ((GameView)view).clearBoard();
        }
    }

    @Override
    public void onGameStart(Player startingPlayer) {
        Platform.runLater(this::updateView);
    }

    public void setBackgroundColor(ArrayList<Integer> colors){
        this.colors = colors;
    }

    public ArrayList<Integer> getBackgroundColor(){
        return colors;
    }
}

