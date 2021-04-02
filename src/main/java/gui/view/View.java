package gui.view;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import gui.controller.Controller;

abstract class View extends Scene {

    Controller controller;

    public View(Parent parent, Controller controller, int windowWidth, int windowHeight) {
        super(parent, windowWidth, windowHeight);
        this.controller = controller;
    }

    /**
     * Looks up an element in the current scene.
     *
     * This method is redundant?
     * @param fxId the id of the element in the fxml file
     * @return the node (may be casted)
     */
    public Node getElement(String fxId) {
        return this.lookup('#'+fxId);
    }


}
