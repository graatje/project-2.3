package gui.model;

import framework.ConfigData;
import framework.ConnectedGameManager;
import framework.GameManager;
import framework.GameType;
import framework.board.Board;
import framework.board.BoardObserver;
import framework.board.BoardPiece;
import framework.player.LocalPlayer;
import framework.player.Player;
import gui.view.GameView;
import gui.view.View;
import javafx.application.Platform;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GenericGameModel extends Model implements BoardObserver {

    private Board board;
    private GameManager gameManager;
    private double boardSize;
    private ArrayList<Integer> colors;
    private List<URL> playerIconFileURLs;

    /**
     * Sets gameManager and board variables, and registers this model as observer.
     */
    public void setupGameManager() {
        gameManager = ConfigData.getInstance().getGameManager();
        board = gameManager.getBoard();
        board.registerObserver(this);

        // Load boardpiece images
        GameType gameType = ConfigData.getInstance().getGameType();
        switch (gameType) {
            case TTT:
            case TTT_LOCAL:
            case TTT_LOCAL_ONLINE:
            case TTT_ONLINE:
                setPlayerIconFileURLs(Arrays.asList(getClass().getResource("/boardPieces/ttt_o.png"), getClass().getResource("/boardPieces/ttt_x.png")));
                break;
            case OTHELLO:
            case OTHELLO_LOCAL:
            case OTHELLO_LOCAL_ONLINE:
            case OTHELLO_ONLINE:
                setPlayerIconFileURLs(Arrays.asList(getClass().getResource("/boardPieces/othello_black.png"), getClass().getResource("/boardPieces/othello_white.png")));
                break;
        }
    }

    public List<URL> getPlayerIconFileURLs() {
        return playerIconFileURLs;
    }

    public void setPlayerIconFileURLs(List<URL> playerIconFileURLs) {
        this.playerIconFileURLs = playerIconFileURLs;
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
            setDialogMessage(message);

            gameManager.reset();

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

    public void setBackgroundColor(ArrayList<Integer> colors){
        this.colors = colors;
    }

    public ArrayList<Integer> getBackgroundColor(){
        return colors;
    }

    public String getPlayerNames(List<Player> players){
        return players.get(0).getName() + " VS. " + players.get(1).getName();
    }

    public ArrayList<String> getPlayerInfo(Map<Player, Integer> playerInfo){
        ArrayList<String> playerInformation = new ArrayList<>();

        for (Player player : playerInfo.keySet()){
            String color = null;
            String p1 = null;
            String p2 = null;
            boolean showPiecesCount = true;

            switch(ConfigData.getInstance().getGameType()){
                case TTT:
                case TTT_LOCAL:
                case TTT_LOCAL_ONLINE:
                case TTT_ONLINE:
                    p1 = "Noughts (O)";
                    p2 = "Crosses (X)";
                    showPiecesCount = false;
                    break;

                case OTHELLO:
                case OTHELLO_LOCAL:
                case OTHELLO_LOCAL_ONLINE:
                case OTHELLO_ONLINE:
                    p1 = "Black";
                    p2 = "White";
                    break;
            }
            if(player.getID() == 0){
                color = p1;
            }else if (player.getID() == 1){
                color = p2;
            }

            String playerinfo = player.getName() + "\n" + color;
            if(showPiecesCount == true){
                playerinfo += "\n" + playerInfo.get(player);
            }
            playerInformation.add(playerinfo);
        }
        return playerInformation;
    }

    public void setBoardSize(double boardSize) {
        this.boardSize = boardSize;
    }
}