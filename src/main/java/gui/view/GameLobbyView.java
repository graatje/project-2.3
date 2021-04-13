package gui.view;

import framework.player.LocalPlayer;
import framework.player.Player;
import gui.controller.Controller;
import gui.model.GameLobbyModel;
import javafx.scene.Parent;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class GameLobbyView extends View<GameLobbyModel> {

    public GameLobbyView(Parent parent, Controller controller, int windowWidth, int windowHeight) {
        super(parent, controller, windowWidth, windowHeight);
    }

    @Override
    public void update(GameLobbyModel model) {
//        ArrayList<String> playerNames = new ArrayList<>(Arrays.asList("2zqe", "Pluficorny", "C4Bomb", "Yay it's Mike :D", "WGBBounty", "xxX_Kevverboy69_Xxx"));
//        updateGameChallengeList(playerNames);
    }

    private void updateGameChallengeList(ArrayList<String> playerNames) {
        //als lookup null teruggeeft, gebruik parameter net zoals showInfoText
        ListView challengerList = (ListView) lookup("#challengerList");
        // als dit null is: rip lol
        challengerList.getItems().clear();

        for(String playerName : playerNames) {
            challengerList.getItems().add(playerName);
        }
    }
}
