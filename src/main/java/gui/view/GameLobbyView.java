package gui.view;

import gui.controller.Controller;
import gui.model.GameLobbyModel;
import javafx.scene.Parent;

public class GameLobbyView extends View<GameLobbyModel> {

    public GameLobbyView(Parent parent, Controller controller, int windowWidth, int windowHeight) {
        super(parent, controller, windowWidth, windowHeight);
    }

    @Override
    public void update(GameLobbyModel model) {

    }
}
