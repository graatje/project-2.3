package gui.model;

import framework.*;
import gui.MainWindow;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

public class GameLobbyModel extends Model implements ConnectedGameManagerObserver {

    private List<String> currentlyShowingPlayers;

    private GenericGameModel gameModel;

    private MainWindow mainWindow;

    public GameLobbyModel(GenericGameModel gameModel, MainWindow mainWindow){
        this.gameModel = gameModel;
        this.mainWindow = mainWindow;

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


    @Override
    public void onServerError(String errorMessage) {
        //TODO: implement met platform.runlater
    }

    @Override
    public void onPlayerListReceive() {
        Platform.runLater(this::updateView);
    }

    @Override
    public void onChallengeReceive(Match match) {
        //TODO accept/ignore challenge
        // platform
    }

    @Override
    public void onGameStarted() {
        Platform.runLater(() -> mainWindow.switchView(MainWindow.viewEnum.GAME));
    }

    public void prepareGameManager() {
        ((ConnectedGameManager)ConfigData.getInstance().getGameManager()).registerObserver(this);
    }
}
