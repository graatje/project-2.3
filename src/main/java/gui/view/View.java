package gui.view;

import gui.model.Model;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import gui.controller.Controller;

public abstract class View extends Scene {

    protected Controller controller;

    public View(Parent parent, Controller controller, int windowWidth, int windowHeight) {
        super(parent, windowWidth, windowHeight);
        this.controller = controller;
    }

    public abstract void update(Model model);

    //TODO: vragen: TTTView/Othello View maken (of generiek houden en drawTile meerdere parameters meegeven?!)
    //TODO: Board-parameter instellen
    public abstract void drawTiles();
}
