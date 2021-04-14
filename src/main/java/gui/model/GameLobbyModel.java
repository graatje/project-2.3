package gui.model;

import framework.*;
import gui.MainWindow;
import javafx.application.Platform;

import java.util.List;

public class GameLobbyModel extends Model implements ConnectedGameManagerObserver {

    private List<String> currentlyShowingPlayers;

    private String challengeMessage;
    private Match match;

    private GenericGameModel gameModel;

    private MainWindow mainWindow;
    private ConnectedGameManager cgm;

    public GameLobbyModel(GenericGameModel gameModel, MainWindow mainWindow){
        this.gameModel = gameModel;
        this.mainWindow = mainWindow;

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
    public void prepareOnlineGame(boolean isAI) {
        cgm.updateSelfPlayerSupplier(isAI ? ConfigData.getInstance().getCurrentGame().createAIPlayerFactory() :
                ConfigData.getInstance().getCurrentGame().createLocalPlayerFactory());

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
            logout();
            mainWindow.switchView(MainWindow.viewEnum.GAME_MENU);
        });
    }

    @Override
    public void onPlayerListReceive() {
        Platform.runLater(this::updateView);
    }

    @Override
    public void onChallengeReceive(Match match) {
        setChallenge(match, match.getOpponentName()+" is challenging you to a game of "+match.getGameType()+"! Do you accept?");
        Platform.runLater(this::updateView); // zodat melding wordt weergegeven
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
    public void onGameStarted() {
        Platform.runLater(() -> mainWindow.switchView(MainWindow.viewEnum.GAME));
    }

    public void prepareGameManager() {
        this.cgm = (ConnectedGameManager)ConfigData.getInstance().getGameManager();
        cgm.registerObserver(this);
    }
}
