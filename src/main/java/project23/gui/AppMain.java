package project23.gui;

import javafx.application.Platform;
import javafx.stage.Stage;

public class AppMain {
    public static void main(String[] args) {
        Platform.startup(() -> {
            try {
                new App().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
