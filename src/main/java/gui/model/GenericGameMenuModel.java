package gui.model;

import framework.ConfigData;
import framework.ConnectedGameManager;
import framework.GameManager;
import framework.player.LocalPlayer;
import framework.player.MinimaxAIPlayer;
import framework.player.ServerPlayer;
import gui.view.View;
import othello.board.OthelloBoard;
import ttt.board.TTTBoard;

import java.io.IOException;

public class GenericGameMenuModel extends Model{

    public void setPlayerName(String username) {
        // TODO: moeten geldige usernames (leeg, gekke tekens, lengte) hier of in framework getest worden?
        System.out.println("Username will be set to: \""+username+"\".");
    }

    public void setLocalGameManager() {
        GameManager gameManager = null;

        //TODO: factory pattern hier?
        switch(ConfigData.getInstance().getGameType()) {
            case "Tic-tac-toe":
                gameManager = new GameManager(TTTBoard::new);
                break;
            case "Othello":
                gameManager = new GameManager(OthelloBoard::new);
                break;
        }

        // Add players
        gameManager.addPlayer(new LocalPlayer(gameManager.getBoard(), ConfigData.getInstance().getPlayerName()));
        gameManager.addPlayer(new MinimaxAIPlayer(gameManager.getBoard(), "Robot1", 6, ConfigData.getInstance().getAIDifficulty()));

        startGame(gameManager);
    }

    public void setOnlineGameManager() {
        ConnectedGameManager gameManager = null;

        String ip = ConfigData.getInstance().getServerIP();
        int port = ConfigData.getInstance().getServerPort();
        MinimaxAIPlayer.AIDifficulty difficulty = ConfigData.getInstance().getAIDifficulty();

        //TODO: factory pattern hier?
        try {
            switch (ConfigData.getInstance().getGameType()) {
                case "Tic-tac-toe":
                    gameManager = new ConnectedGameManager(TTTBoard::new, ip, port, b -> new MinimaxAIPlayer(b, ConfigData.MINIMAX_DEPTH, difficulty));
                    break;
                case "Othello":
                    gameManager = new ConnectedGameManager(OthelloBoard::new, ip, port, b -> new MinimaxAIPlayer(b, ConfigData.MINIMAX_DEPTH, difficulty));
                    break;
            }
        } catch (IOException e) {
            for(View view : observers) {
                view.showDialog("Couldn't connect to server.");
            }
        }

        //TODO: voeg players toe: server- en AIplayer
        //gameManager.addPlayer(new MinimaxAIPlayer(gameManager.getBoard(), "Robot1Online", 6, ConfigData.getInstance().getAIDifficulty()));


        startGame(gameManager);

    }

    private void startGame(GameManager gameManager) {
        if(gameManager == null) {
            throw new IllegalStateException("This shouldnt happen?!");
        }
        ConfigData.getInstance().setGameManager(gameManager);

        //Local: user vs AI
        //Online: AI vs onlinePlayer

        //TODO: update GameView - maar hoe?
        gameManager.start(null);
    }

}
