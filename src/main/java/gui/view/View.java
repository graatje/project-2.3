package gui.view;

import gui.model.Model;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import gui.controller.Controller;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public abstract class View<T extends Model> extends Scene {

    protected Controller controller;

    public View(Parent parent, Controller controller, int windowWidth, int windowHeight) {
        super(parent, windowWidth, windowHeight);
        this.controller = controller;
    }

    public abstract void update(T model);


    public void showDialog(String message){
        //creating dialog
        Dialog<String> dialog = new Dialog<String>();
        //add button
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        //setting dialog content
        dialog.setContentText(message);
        //add button to dialogpane
        dialog.getDialogPane().getButtonTypes().add(type);
        //show dialog
        dialog.show();
    }

}
