package project23.gui;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    /**
     * First method that gets called, runs the application
     *
     * @param primaryStage, the primarystage, scenes will be loaded in this
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        MainWindow mainWindow = new MainWindow(primaryStage);
        mainWindow.switchView(MainWindow.ViewEnum.MAINMENU);
    }
}