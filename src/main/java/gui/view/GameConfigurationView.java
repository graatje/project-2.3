package gui.view;

import gui.controller.Controller;
import gui.model.GameConfigurationModel;
import javafx.scene.Parent;

public class GameConfigurationView extends View<GameConfigurationModel> {
    public GameConfigurationView(Parent parent, Controller controller, int windowWidth, int windowHeight) {
        super(parent, controller, windowWidth, windowHeight);
    }

    @Override
    public void update(GameConfigurationModel model) {
        showDialog(model.getDialogMessage(), model.getDialogTitle());
        showInfoText(model.getInfoMessage(), model.getLabelNode());
    }
}
