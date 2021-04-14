package gui.model;

import framework.ConfigData;
import framework.ConnectedGameManager;
import framework.GameManager;

import java.util.ArrayList;
import java.util.List;

public class GameLobbyModel extends Model {

    private List<String> currentlyShowingPlayers;

    private GenericGameModel gameModel;

    public GameLobbyModel(GenericGameModel gameModel){
        this.gameModel = gameModel;
    }

    public List<String> getLobbyPlayers() {
        GameManager tmpGm = ConfigData.getInstance().getGameManager();
        List<String> players = new ArrayList<>();
        if(tmpGm instanceof ConnectedGameManager) {
            ConnectedGameManager gameManager = (ConnectedGameManager) tmpGm;
            players = gameManager.getLobbyPlayers();
        }
        this.currentlyShowingPlayers = players;
        return players;
    }

    public void challengePlayer(int playerListIndex) {
        System.out.println("Player to be challenged: "+currentlyShowingPlayers.get(playerListIndex));
    }

    /**
     * Starts an online match, nothing to do with challenging
     * @param isAI
     */
    public void prepareOnlineGame(boolean isAI) {
        ConnectedGameManager cgm = (ConnectedGameManager) ConfigData.getInstance().getGameManager();
        cgm.updateSelfPlayerSupplier(isAI ? ConfigData.getInstance().getCurrentGame().createAIPlayerFactory() :
                ConfigData.getInstance().getCurrentGame().createLocalPlayerFactory());

        gameModel.prepareNewGame();

        ConfigData.getInstance().getGameManager().requestStart();
    }
}
