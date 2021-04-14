package gui.model;

import framework.ConfigData;
import framework.GameManager;

public class GenericGameMenuModel extends Model {

    private final GenericGameModel gameModel;
    private final GameLobbyModel gameLobbyModel;

    public GenericGameMenuModel(GenericGameModel gameModel, GameLobbyModel gameLobbyModel){
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
        if(gm==null) {
            setDialogMessage("Could not connect to server.");
            updateView();
            return;
        }
        ConfigData.getInstance().setGameManager(gm);

        gameModel.prepareNewGame();
    }
}
