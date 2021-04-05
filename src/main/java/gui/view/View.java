package gui.view;

import gui.model.Model;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import gui.controller.Controller;

public abstract class View<T extends Model> extends Scene {

    protected Controller controller;

    public View(Parent parent, Controller controller, int windowWidth, int windowHeight) {
        super(parent, windowWidth, windowHeight);
        this.controller = controller;
    }

    public abstract void update(T model);
}
