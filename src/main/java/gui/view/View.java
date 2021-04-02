package gui.view;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import gui.controller.Controller;

abstract class View extends Scene {

    protected Controller controller;

    public View(Parent parent, Controller controller, int windowWidth, int windowHeight) {
        super(parent, windowWidth, windowHeight);
        this.controller = controller;
    }

}
