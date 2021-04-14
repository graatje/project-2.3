package gui.model;

import framework.ConfigData;

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

        //TODO: Zorg dat gameManager lobbyspelers kan vinden vóór op de volgende regel updateView aangeroepen wordt
        System.out.println("Game Lobby model updated vanaf hier (MenuModel)");
        gameLobbyModel.updateView();
    }

    private void prepareGameManager() {
        // Create new game, and set it in the config. (This should always be done together)
        ConfigData.getInstance().setGameManager(ConfigData.getInstance().getCurrentGame().createGameManager());
        gameModel.prepareNewGame();
    }
}
