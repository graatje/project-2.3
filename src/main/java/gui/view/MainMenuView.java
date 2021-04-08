package gui.view;

import gui.controller.Controller;
import gui.model.MainMenuModel;
import gui.model.Model;
import javafx.scene.Parent;

public class MainMenuView extends View<MainMenuModel> {
    public MainMenuView(Parent parent, Controller controller, int windowWidth, int windowHeight) {
        super(parent, controller, windowWidth, windowHeight);
    }

    @Override
    public void update(MainMenuModel model) {

    }
}
