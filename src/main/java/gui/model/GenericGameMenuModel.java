package gui.model;

import framework.ConfigData;

public class GenericGameMenuModel extends Model {

    private GenericGameModel gameModel;

    public GenericGameMenuModel(GenericGameModel gameModel){
        this.gameModel = gameModel;
    }

    public void setPlayerName(String username) {
        ConfigData.getInstance().setPlayerName(username);
    }

    public void setGameManager() {
        // Nu korter dankzij Game-klasse
        ConfigData.getInstance().setGameManager(ConfigData.getInstance().getCurrentGame().createGameManager());
        gameModel.setBackgroundColor(ConfigData.getInstance().getCurrentGame().getBoardBackgroundColor());
        gameModel.setupGameManager();

        gameModel.updateView();
    }
}
