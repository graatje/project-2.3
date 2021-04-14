package gui.view;

import gui.controller.Controller;
import gui.model.GameMenuModel;
import javafx.scene.Parent;

public class GameMenuView extends View<GameMenuModel> {
    public GameMenuView(Parent parent, Controller controller, int windowWidth, int windowHeight) {
        super(parent, controller, windowWidth, windowHeight);
    }

    @Override
    public void update(GameMenuModel model) {
        showInfoText(model.getInfoMessage(), model.getLabelNode());
        showDialog(model.getDialogMessage(), model.getDialogTitle());
    }
}
