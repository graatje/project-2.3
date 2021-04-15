package project23.gui.model;

import project23.framework.ConfigData;
import project23.framework.GameManager;

public class GameMenuModel extends Model {

    private final GameModel gameModel;
    private final GameLobbyModel gameLobbyModel;

    public GameMenuModel(GameModel gameModel, GameLobbyModel gameLobbyModel) {
        this.gameModel = gameModel;
        this.gameLobbyModel = gameLobbyModel;
    }

    public void setPlayerName(String username) {
        ConfigData.getInstance().setPlayerName(username);
    }

    /**
     * Prepares an offline match.
     * Since there is no waiting for a player like in multiplayer,
     * it can immediately start the match.
     */
    public void prepareOfflineGame() {
        ConfigData.getInstance().getCurrentGame().setOnline(false);
        prepareGameManager();
        ConfigData.getInstance().getGameManager().requestStart();
    }

    /**
     * Prepares an online match. Cannot immediately start, unlike {@link #prepareOfflineGame()}.
     * Relevant: {@link GameLobbyModel#prepareGameManager()}
     */
    public void prepareLobby() {
        ConfigData.getInstance().getCurrentGame().setOnline(true);
        prepareGameManager();
        gameLobbyModel.prepareGameManager();
    }

    /**
     * Creates a gamemanager. Notifies gameModel to set gamemanager as well. See: {@link GameModel#prepareNewGame()}
     */
    private void prepareGameManager() {
        // Create new game, and set it in the config. (This should always be done together)
        GameManager gm = ConfigData.getInstance().getCurrentGame().createGameManager();
        if (gm == null) {
            setDialogMessageAndTitle("Could not connect to server.", "Error");
            updateView();
            return;
        }
        ConfigData.getInstance().setGameManager(gm);

        gameModel.prepareNewGame();
    }
}
