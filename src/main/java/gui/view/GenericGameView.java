package gui.view;

import gui.controller.Controller;
import gui.model.Model;
import javafx.scene.Parent;

public class GenericGameView extends GameView {
    public GenericGameView(Parent parent, Controller controller, int windowWidth, int windowHeight) {
        super(parent, controller, windowWidth, windowHeight);
    }

    @Override
    public void update(Model model) {
        drawBoard(3,"#TTTBoard");
    }
}
