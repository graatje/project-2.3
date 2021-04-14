package project23;

import javafx.application.Platform;
import javafx.stage.Stage;
import project23.gui.App;

public class Main {
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
