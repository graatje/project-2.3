package project23.gui.view;

import javafx.scene.Parent;
import project23.gui.controller.Controller;
import project23.gui.model.GameConfigurationModel;

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
