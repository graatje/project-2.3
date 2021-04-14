package gui.model;

import framework.ConfigData;
import framework.GameManager;
import framework.board.Board;
import framework.board.BoardObserver;
import framework.board.BoardPiece;
import framework.player.LocalPlayer;
import framework.player.Player;
import javafx.application.Platform;
import javafx.scene.paint.Color;

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
     * Sets gameManager and board variables, and registers this model as observer. Does not start the game yet.
     */
    public void prepareNewGame() {
        //TODO: dit via ConfigData aanvragen?
        gameManager = ConfigData.getInstance().getGameManager();
        board = gameManager.getBoard();
        board.registerObserver(this);
        updateView(); // Draws an empty board
    }

    public void clickTile(double x, double y) {
        double cellSize = boardSize / board.getWidth();
        int xTile = (int) Math.floor(x / cellSize);
        int yTile = (int) Math.floor(y / cellSize);

        //TODO: meegeven met framework (update: wat betekent dit? al gedaan?!)
        Player player = gameManager.getBoard().getCurrentPlayer();

        if (player == null){
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
        if(who.isShowValidMoves() && where == null) {
            setInfoMessage("Skipped a turn, no available moves");
            System.out.println("DEBUG: skipped!!!");
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

    public void clearBoard(){
        //TODO: deze methode is puur omdat anders het bord nog blijft staan tot de volgende wedstrijd begint (bv bij multiplayer)
        // herimplementeren? betere manier?
        //updateView();
    }

    @Override
    public void onGameStart(Player startingPlayer) {
        Platform.runLater(this::updateView);
    }

    public String getPlayerNames(List<Player> players){
        return players.get(0).getName() + " VS. " + players.get(1).getName();
    }

    public ArrayList<String> getPlayerInfo(Map<Player, Integer> playerInfo){
        ArrayList<String> playerInformation = new ArrayList<>();

        for (Player player : playerInfo.keySet()){
            boolean showPiecesCount = ConfigData.getInstance().getCurrentGame().showPiecesCount();

            String[] boardPieceNames = ConfigData.getInstance().getCurrentGame().getBoardPieceNames();

            String playerinfo = player.getName() + "\n" + boardPieceNames[player.getID()];
            if(showPiecesCount){
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