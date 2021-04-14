package project23.gui.view;

import javafx.scene.Parent;
import project23.gui.controller.Controller;
import project23.gui.model.GameMenuModel;

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
