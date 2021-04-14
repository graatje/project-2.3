package gui.view;

import gui.controller.Controller;
import gui.model.GameLobbyModel;
import javafx.scene.Parent;
import javafx.scene.control.ListView;

import java.util.List;

public class GameLobbyView extends View<GameLobbyModel> {

    public GameLobbyView(Parent parent, Controller controller, int windowWidth, int windowHeight) {
        super(parent, controller, windowWidth, windowHeight);
    }

    @Override
    public void update(GameLobbyModel model) {
        // een paar ms wachten hier fixt het probleem.. maar nee
        System.out.println("Op dit punt moet de lobby lijst al bekend zijn.");
        updateGameChallengeList(model.getLobbyPlayers());
    }

    private void updateGameChallengeList(List<String> playerNames) {
        //als lookup null teruggeeft, gebruik parameter net zoals showInfoText
        ListView challengerList = (ListView) lookup("#challengerList");
        // als dit null is: rip lol
        challengerList.getItems().clear();

        for(String playerName : playerNames) {
            challengerList.getItems().add(playerName);
        }
    }
}
