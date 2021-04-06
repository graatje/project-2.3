package gui.model;

import framework.GameManager;
import framework.board.Board;
import framework.board.BoardObserver;
import framework.board.BoardPiece;
import framework.player.LocalPlayer;
import framework.player.Player;
import javafx.application.Platform;

public class GenericGameModel extends Model implements BoardObserver {

    private final Board board;
    private GameManager gameManager;
    private double boardSize = 500; //TODO: opvragen van fxml?

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
                System.out.println("Ongeldige zet");
            }
        } else {
            System.out.println("Wacht op je beurt");
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
        System.out.println("someone won :D");
    }
}
