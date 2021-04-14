package project23.gui.model;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import project23.framework.ConfigData;
import project23.framework.GameManager;
import project23.framework.board.Board;
import project23.framework.board.BoardObserver;
import project23.framework.board.BoardPiece;
import project23.framework.player.LocalPlayer;
import project23.framework.player.Player;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameModel extends Model implements BoardObserver {

    private Board board;
    private GameManager gameManager;
    private double boardSize;
    private Color color;
    private List<URL> playerIconFileURLs;

    /**
     * Sets gameManager and board variables, and registers this model as observer. This method does not start the match.
     */
    public void prepareNewGame() {
        gameManager = ConfigData.getInstance().getGameManager();
        board = gameManager.getBoard();
        board.registerObserver(this);
        updateView(); // Draws an empty board
    }

    public void clickTile(double x, double y) {
        double cellSize = boardSize / board.getWidth();
        int xTile = (int) Math.floor(x / cellSize);
        int yTile = (int) Math.floor(y / cellSize);

        Player player = gameManager.getBoard().getCurrentPlayer();

        if (player == null) {
            return;
        }

        if (player instanceof LocalPlayer) {
            if (gameManager.getBoard().isValidMove(xTile, yTile)) {
                ((LocalPlayer) player).executeMove(xTile, yTile);
            } else {
                setInfoMessage("Invalid move.");
            }
        } else {
            setInfoMessage("Please wait for your move");
        }
        updateView();
    }

    public Board getBoard() {
        return board;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    @Override
    public void onPlayerMoved(Player who, BoardPiece where) {

        //TODO klok in view aanzetten?
        //setClockReset(true);

        //TODO: show info message on skipping
        if (who.isShowValidMoves() && where == null) {
            setInfoMessage("Skipped a turn, no available moves");
            System.err.println("DEBUG: skipping..! (hoera, dit werkt?)");
            updateView();
        }
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
            setDialogMessageAndTitle(message, "Info");
            updateView();
        });
    }

    @Override
    public void onGameStart(Player startingPlayer) {
        Platform.runLater(this::updateView);
    }

    public String getPlayerNames(List<Player> players) {
        return players.get(0).getName() + " VS. " + players.get(1).getName();
    }

    public ArrayList<String> getPlayerInfo(Map<Player, Integer> playerInfo) {
        ArrayList<String> playerInformation = new ArrayList<>();

        for (Player player : playerInfo.keySet()) {
            boolean showPiecesCount = ConfigData.getInstance().getCurrentGame().showPiecesCount();

            String[] boardPieceNames = ConfigData.getInstance().getCurrentGame().getBoardPieceNames();

            String playerinfo = player.getName() + "\n" + boardPieceNames[player.getID()];
            if (showPiecesCount) {
                playerinfo += "\n" + playerInfo.get(player);
            }
            playerInformation.add(playerinfo);
        }
        return playerInformation;
    }

    public void setBoardSize(double boardSize) {
        this.boardSize = boardSize;
    }

    public void restartGame() {
        gameManager.reset();
        gameManager.requestStart();
    }
}