package gui.view;

import gui.model.Model;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import gui.controller.Controller;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.text.Text;

public abstract class View<T extends Model> extends Scene {

    protected Controller controller;
    private boolean displayingInfo = false;
    public static final int MESSAGE_CLEARING_DELAY_MS = 2000;

    @FXML private Text infoTextField;

    public View(Parent parent, Controller controller, int windowWidth, int windowHeight) {
        super(parent, windowWidth, windowHeight);
        this.controller = controller;
    }

    public abstract void update(T model);

    public void showDialog(String message) {
        if(message != null && !message.isBlank()) {
            //creating dialog
            Dialog<String> dialog = new Dialog<>();
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

    /**
     * if not displaying a message show a message when this method gets called.
     * @param message, the message to display.
     */
    public void showInfoText(String message, String fieldName) {
        if(!displayingInfo) {
            new Thread(() -> {
                displayingInfo = true;
                infoTextField = (Text) lookup(fieldName);
                //TODO: check of lookup wel gelukt is, anders error gooien?
                infoTextField.setText(message);
                try {
                    Thread.sleep(MESSAGE_CLEARING_DELAY_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                displayingInfo = false;
                infoTextField.setText("");
            }).start();
        }
    }

}
