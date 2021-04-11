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
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static java.lang.System.currentTimeMillis;

public abstract class View<T extends Model> extends Scene {

    protected Controller controller;
    public static final int MESSAGE_CLEARING_DELAY_MS = 2000;
    private long expireTimeMessage = 0;

    public View(Parent parent, Controller controller, int windowWidth, int windowHeight) {
        super(parent, windowWidth, windowHeight);
        this.controller = controller;
    }

    public abstract void update(T model);

    public void showDialog(String message, String dialogTitle) {
        if(message != null && !message.isBlank()) {
            //creating dialog
            Dialog<String> dialog = new Dialog<>();
            //add button
            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            //setting dialog content
            dialog.setContentText(message);
            //add button to dialogpane
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            dialog.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
            dialog.getDialogPane().getStylesheets().add(getClass().getResource("/dialogStyle.css").toExternalForm());
            dialog.setTitle(dialogTitle);
            Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
            dialogStage.getIcons().add(new Image(getClass().getResource("/icon.png").toExternalForm()));

            //show dialog
            dialog.show();
        }
    }

    /**
     * if not displaying a message show a message when this method gets called.
     * @param message, the message to display.
     */
    public void showInfoText(String message, Text node) {
        if(!message.isBlank()) {
            this.expireTimeMessage = currentTimeMillis() + MESSAGE_CLEARING_DELAY_MS;

            new Thread(() -> {
                node.setText(message);
                try {
                    Thread.sleep(MESSAGE_CLEARING_DELAY_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(currentTimeMillis() >= expireTimeMessage) {
                    node.setText("");
                }
            }).start();
        }
    }

}
