import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        MainWindow mainWindow = new MainWindow(primaryStage);
        mainWindow.switchView(MainWindow.viewEnum.MAINMENU);
    }
}