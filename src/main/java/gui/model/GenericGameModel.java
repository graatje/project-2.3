package gui.model;

import framework.GameManager;
import framework.board.Board;
import framework.board.BoardObserver;
import framework.board.BoardPiece;
import framework.player.LocalPlayer;
import framework.player.Player;
import gui.view.GameView;
import gui.view.View;
import javafx.application.Platform;

public class GenericGameModel extends Model implements BoardObserver {

    private final Board board;
    private GameManager gameManager;
    private double boardSize = 500; //TODO: opvragen van fxml?
    private String infoMessage;

    public GenericGameModel(GameManager gameManager) {
        this.gameManager = gameManager;
        this.board = gameManager.getBoard();
        gameManager.getBoard().registerObserver(this);
    }

    public void clickTile(double x, double y) {
        double cellSize = boardSize/board.getWidth();
        int xTile = (int) Math.floor(x/cellSize);
        int yTile = (int) Math.floor(y/cellSize);

        //TODO: meegeven met framework
        Player player = gameManager.getBoard().getCurrentPlayer();
        if(player instanceof LocalPlayer) {
            if(gameManager.getBoard().isValidMove(xTile, yTile)) {
                ((LocalPlayer) player).executeMove(xTile, yTile);
            } else {
                infoMessage = "Invalid move";
            }
            } else {
            infoMessage = "Wait for your turn";
            }
        for(View view : observers) {
            ((GameView)view).setInfoText(infoMessage);
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
        String message = null;

        if(who == null){
            message="Helaas, het is gelijkspel";
        }else{
            message= who.getName() + " heeft gewonnen!";
        }

        for(View view : observers) {
            view.showDialog(message);
        }
    }
}

