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
import java.io.ObjectInputFilter;

public class GenericGameMenuModel extends Model {

    private GenericGameModel gameModel;

    public GenericGameMenuModel(GenericGameModel gameModel){
        this.gameModel = gameModel;
    }

    public void setPlayerName(String username) {
        //System.out.println("Username will be set to: \"" + username + "\".");
        ConfigData.getInstance().setPlayerName(username);
    }

    public void setGameManager() {
        GameManager gameManager = null;

        String ip = ConfigData.getInstance().getServerIP();
        int port = ConfigData.getInstance().getServerPort();
        MinimaxAIPlayer.AIDifficulty difficulty = ConfigData.getInstance().getAIDifficulty();

        try {
            switch (ConfigData.getInstance().getGameType()) {
                case TTT_LOCAL:
                    gameManager = new GameManager(TTTBoard::new);
                    break;
                case OTHELLO_LOCAL:
                    gameManager = new GameManager(OthelloBoard::new);
                    break;
                case TTT_ONLINE:
                    gameManager = new ConnectedGameManager(TTTBoard::new, ip, port, b -> new MinimaxAIPlayer(b, 6, difficulty));
                    break;
                case OTHELLO_ONLINE:
                    gameManager = new ConnectedGameManager(OthelloBoard::new, ip, port, b -> new MinimaxAIPlayer(b, 6, difficulty));
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ConfigData.getInstance().setGameManager(gameManager);
        gameModel.setupGameManager();

        if (!(gameManager instanceof ConnectedGameManager)){
            gameManager.addPlayer(new LocalPlayer(gameManager.getBoard(), ConfigData.getInstance().getPlayerName()));
            gameManager.addPlayer(new MinimaxAIPlayer(gameManager.getBoard(), "Computer", 6, ConfigData.getInstance().getAIDifficulty()));
            gameManager.start(null);
        } else{
            ((ConnectedGameManager) gameManager).setSelfName(ConfigData.getInstance().getPlayerName());
            ((ConnectedGameManager) gameManager).login();
            ((ConnectedGameManager) gameManager).subscribe(ConfigData.getInstance().getGameType().gameName);
        }
    }
}
