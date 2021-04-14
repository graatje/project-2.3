package project23.gui.model;

import javafx.application.Platform;
import project23.framework.*;
import project23.gui.MainWindow;

import java.util.List;

public class GameLobbyModel extends Model implements ConnectedGameManagerObserver {

    private List<String> currentlyShowingPlayers;

    private ChallengeRequest lastChallengeRequest;
    private boolean isAI;
    private GameModel gameModel;
    private MainWindow mainWindow;
    private ConnectedGameManager cgm;

    public GameLobbyModel(GameModel gameModel, MainWindow mainWindow) {
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
        cgm.challengePlayer(playername);
        setInfoMessage("Challenged " + playername);
        updateView();
    }

    /**
     * Starts an online match, nothing to do with challenging
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
            setDialogMessageAndTitle(errorMessage, "Error");
            updateView();
        });
    }

    @Override
    public void onPlayerListReceive() {
        Platform.runLater(this::updateView);
    }

    @Override
    public void onChallengeRequestReceive(ChallengeRequest challengeRequest) {
        if (cgm.getBoard().getBoardState() != BoardState.PLAYING && challengeRequest.getGameType().equals(cgm.getGameType())) {
            this.lastChallengeRequest = challengeRequest;
            Platform.runLater(this::updateView); // zodat melding wordt weergegeven
        }
    }

    public ChallengeRequest getLastChallengeRequest() {
        ChallengeRequest lastChallengeRequestTmp = lastChallengeRequest;
        lastChallengeRequest = null;
        return lastChallengeRequestTmp;
    }

    public void acceptMatch(ChallengeRequest request) {
        cgm.acceptChallengeRequest(request);
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
        this.cgm = (ConnectedGameManager) ConfigData.getInstance().getGameManager();
        cgm.registerObserver(this);
    }
}
