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
        //TODO: opvragen van framework
        int gridSize = 3;

        //drawTiles(Board board);
        drawBoard(gridSize,"#TTTBoard");
    }
}
