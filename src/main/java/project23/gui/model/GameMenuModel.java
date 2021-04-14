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

    public void prepareOfflineGame() {
        ConfigData.getInstance().getCurrentGame().setOnline(false);

        // Bereid de gamemodel voor
        prepareGameManager();

        // Start het potje onmiddellijk
        ConfigData.getInstance().getGameManager().requestStart();
    }

    public void prepareLobby() {
        ConfigData.getInstance().getCurrentGame().setOnline(true);
        prepareGameManager();

        gameLobbyModel.prepareGameManager();
    }

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
