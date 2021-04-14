package gui.model;

import framework.*;
import gui.MainWindow;
import javafx.application.Platform;

import java.util.List;

public class GameLobbyModel extends Model implements ConnectedGameManagerObserver {

    private List<String> currentlyShowingPlayers;

    private String challengeMessage;
    private Match match;
    private boolean isAI;

    private GenericGameModel gameModel;

    private MainWindow mainWindow;
    private ConnectedGameManager cgm;

    public GameLobbyModel(GenericGameModel gameModel, MainWindow mainWindow){
        this.gameModel = gameModel;
        this.mainWindow = mainWindow;
    }

    public void setAI(boolean AI) {
        isAI = AI;
    }

    public void refreshPlayerList() {
        cgm.getClient().sendGetPlayerlistMessage();
    }

    public List<String> getLobbyPlayers() {
        this.currentlyShowingPlayers = cgm.getLobbyPlayers();
        return currentlyShowingPlayers;
    }

    public void challengePlayer(int playerListIndex) {
        String playername = currentlyShowingPlayers.get(playerListIndex);
        cgm.challengePlayer(playername, ConfigData.getInstance().getCurrentGameName());
    }

    /**
     * Starts an online match, nothing to do with challenging
     * @param isAI
     */
    public void prepareOnlineGame() {
        gameModel.prepareNewGame();
        cgm.requestStart();
    }

    public void logout() {
        cgm.destroy();
    }

    @Override
    public void onServerError(String errorMessage) {
        Platform.runLater(() -> {
            setDialogMessage(errorMessage);
            updateView();
        });
    }

    @Override
    public void onPlayerListReceive() {
        Platform.runLater(this::updateView);
    }

    @Override
    public void onChallengeReceive(Match match) {
        if(match.getGameType().equals(ConfigData.getInstance().getCurrentGameName())) {
            setChallenge(match, match.getOpponentName() + " is challenging you to a game of " + match.getGameType() + "! Do you accept?");
            Platform.runLater(this::updateView); // zodat melding wordt weergegeven
        }
    }

    public String getChallengeMessage() {
        String challengeMessageTmp = challengeMessage;
        challengeMessage = null;
        return challengeMessageTmp;
    }

    public void setChallenge(Match match, String challengeMessage) {
        this.match = match;
        this.challengeMessage = challengeMessage;
    }

    public void acceptMatch() {
        cgm.acceptChallenge(match);
    }

    @Override
    public void onPostGameStart() {
        Platform.runLater(() -> mainWindow.switchView(MainWindow.ViewEnum.GAME));
    }

    /**
     * Sets whether AI should be used
     */
    @Override
    public void onPreGameStart() {
        cgm.updateSelfPlayerSupplier(isAI ? ConfigData.getInstance().getCurrentGame().createAIPlayerFactory() :
                ConfigData.getInstance().getCurrentGame().createLocalPlayerFactory());
    }

    public void prepareGameManager() {
        this.cgm = (ConnectedGameManager)ConfigData.getInstance().getGameManager();
        cgm.registerObserver(this);
    }
}
