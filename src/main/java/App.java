import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Get root
        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));

        // Misc
        primaryStage.setTitle("Bordspellen");

        // Run
        primaryStage.setScene(new Scene(root, 500,600));
        primaryStage.show();
    }
}