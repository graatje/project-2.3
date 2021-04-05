package gui.view;

import gui.controller.Controller;
import gui.model.Model;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.text.Text;

public class GenericGameMenuView extends View {
    public GenericGameMenuView(Parent parent, Controller controller, int windowWidth, int windowHeight) {
        super(parent, controller, windowWidth, windowHeight);
    }

    @Override
    public void update(Model model) {

    }
}
