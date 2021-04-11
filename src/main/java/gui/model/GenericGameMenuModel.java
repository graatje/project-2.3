package gui.model;

import framework.ConfigData;
import framework.ConnectedGameManager;
import framework.GameManager;
import framework.player.LocalPlayer;
import framework.player.MinimaxAIPlayer;
import framework.player.ServerPlayer;
import gui.view.View;
import othello.board.OthelloBoard;
import othello.player.OthelloMinimaxAIPlayer;
import ttt.board.TTTBoard;
import ttt.player.TTTMinimaxAIPlayer;

import java.io.IOException;
import java.io.ObjectInputFilter;
import java.util.ArrayList;

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
        ArrayList<Integer> colorsOthello = new ArrayList<>();
        colorsOthello.add(0);
        colorsOthello.add(153);
        colorsOthello.add(0);
        ArrayList<Integer> colorsTTT = new ArrayList<>();
        colorsTTT.add(245);
        colorsTTT.add(245);
        colorsTTT.add(245);

        GameManager gameManager = null;

        String ip = ConfigData.getInstance().getServerIP();
        int port = ConfigData.getInstance().getServerPort();
        MinimaxAIPlayer.AIDifficulty difficulty = ConfigData.getInstance().getAIDifficulty();

        try {
            switch (ConfigData.getInstance().getGameType()) {
                case TTT_LOCAL:
                    gameManager = new GameManager(TTTBoard::new);
                    gameModel.setBackgroundColor(colorsTTT);
                    break;
                case OTHELLO_LOCAL:
                    gameManager = new GameManager(OthelloBoard::new);
                    gameModel.setBackgroundColor(colorsOthello);
                    break;
                case TTT_ONLINE:
                    gameManager = new ConnectedGameManager(TTTBoard::new, ip, port, b -> new TTTMinimaxAIPlayer(b, difficulty));
                    gameModel.setBackgroundColor(colorsTTT);
                    break;
                case TTT_LOCAL_ONLINE:
                    gameManager = new ConnectedGameManager(TTTBoard::new, ip, port, b -> new LocalPlayer(b));
                    gameModel.setBackgroundColor(colorsTTT);
                    break;
                case OTHELLO_ONLINE:
                    gameManager = new ConnectedGameManager(OthelloBoard::new, ip, port, b -> new OthelloMinimaxAIPlayer(b, difficulty));
                    gameModel.setBackgroundColor(colorsOthello);
                    break;
                case OTHELLO_LOCAL_ONLINE:
                    gameManager = new ConnectedGameManager(OthelloBoard::new, ip, port, b -> new LocalPlayer(b));
                    gameModel.setBackgroundColor(colorsOthello);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ConfigData.getInstance().setGameManager(gameManager);
        gameModel.setupGameManager();

        if (!(gameManager instanceof ConnectedGameManager)){
            gameManager.addPlayer(new LocalPlayer(gameManager.getBoard(), ConfigData.getInstance().getPlayerName()));

            // TODO: Change this with a factory pattern (same for the switch case above ^)
            switch(ConfigData.getInstance().getGameType()) {
                case TTT_LOCAL:
                    gameManager.addPlayer(new TTTMinimaxAIPlayer(gameManager.getBoard(), "Computer", ConfigData.getInstance().getAIDifficulty()));
                    break;
                case OTHELLO_LOCAL:
                    gameManager.addPlayer(new OthelloMinimaxAIPlayer(gameManager.getBoard(), "Computer", ConfigData.getInstance().getAIDifficulty()));
                    break;
            }

            gameManager.start(null);
        } else{
            ((ConnectedGameManager) gameManager).setSelfName(ConfigData.getInstance().getPlayerName());
            ((ConnectedGameManager) gameManager).login();
            ((ConnectedGameManager) gameManager).subscribe(ConfigData.getInstance().getGameType().gameName);
        }
        gameModel.updateView();
    }
}
