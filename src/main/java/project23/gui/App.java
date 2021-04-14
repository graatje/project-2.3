package project23.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import project23.util.Logger;

import java.io.File;
import java.io.IOException;

public class App extends Application {
    private static final File LOG_DIRECTORY = new File("logs/");

    @Override
    public void start(Stage primaryStage) throws IOException {
        if (!LOG_DIRECTORY.exists()) {
            LOG_DIRECTORY.mkdirs();
        }

        File logFile = new File(LOG_DIRECTORY, "log.txt");
        Logger.setWriteToFile(logFile);

        Logger.info("Logging console output to " + logFile.getAbsolutePath());
        Logger.info("Starting app..");

        MainWindow mainWindow = new MainWindow(primaryStage);
        mainWindow.switchView(MainWindow.ViewEnum.MAINMENU);
    }
}