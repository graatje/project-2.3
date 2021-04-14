package gui.view;

import gui.controller.Controller;
import gui.model.GenericGameConfigurationModel;
import gui.model.Model;
import javafx.scene.Parent;

public class GenericGameConfigurationView extends View<GenericGameConfigurationModel> {
    public GenericGameConfigurationView(Parent parent, Controller controller, int windowWidth, int windowHeight) {
        super(parent, controller, windowWidth, windowHeight);
    }

    @Override
    public void update(GenericGameConfigurationModel model) {
        showDialog(model.getDialogMessage(), model.getDialogTitle());
        showInfoText(model.getInfoMessage(), model.getLabelNode());
    }
}
