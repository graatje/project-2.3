import controller.GenericGameConfigurationController;
import controller.GenericGameController;
import controller.GenericGameMenuController;
import controller.MainMenuController;
import model.GenericGameConfigurationModel;
import model.GenericGameMenuModel;
import model.GenericGameModel;
import model.MainMenuModel;
import view.GenericGameConfigurationView;
import view.GenericGameMenuView;
import view.GenericGameView;
import view.MainMenuView;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import java.io.IOException;

public class MainWindow extends Stage {
    private Stage stage;
    private GenericGameConfigurationView ggcView;
    private GenericGameView ggView;
    private GenericGameMenuView ggmView;
    private MainMenuView mmView;

    public static final int WINDOW_WIDTH = 750;
    public static final int WINDOW_HEIGHT = 750;

    enum viewEnum {
        MAINMENU,
        GAME_MENU,
        GAME_CONFIGURATION,
        GAME
    }

    public MainWindow(Stage primaryStage) throws IOException {

        //Controllers, views en models maken
        GenericGameConfigurationController ggcController = new GenericGameConfigurationController();
        GenericGameConfigurationModel ggcModel = new GenericGameConfigurationModel();
        ggcView = new GenericGameConfigurationView(
                FXMLLoader.load(getClass().getResource("GenericGameConfiguration.fxml")), ggcController, WINDOW_WIDTH, WINDOW_HEIGHT);
        //TODO: fxml bestanden hoofdletters of niet?

        GenericGameController ggController = new GenericGameController();
        GenericGameModel ggModel = new GenericGameModel();
        ggView = new GenericGameView(
                FXMLLoader.load(getClass().getResource("GenericGame.fxml")), ggController, WINDOW_WIDTH, WINDOW_HEIGHT);

        GenericGameMenuController ggmController = new GenericGameMenuController();
        GenericGameMenuModel ggmModel = new GenericGameMenuModel();
        ggmView = new GenericGameMenuView(
                FXMLLoader.load(getClass().getResource("GenericGameMenu.fxml")), ggmController, WINDOW_WIDTH, WINDOW_HEIGHT);

        MainMenuController mmController = new MainMenuController();
        MainMenuModel mmModel = new MainMenuModel();
        mmView = new MainMenuView(
                FXMLLoader.load(getClass().getResource("MainMenu.fxml")), mmController, WINDOW_WIDTH, WINDOW_HEIGHT);

        this.stage = primaryStage;
        switchView(viewEnum.MAINMENU);
    }

    public void switchView(viewEnum view) {
        switch (view) {
            case MAINMENU:
                this.setScene(mmView);
                break;
            case GAME_MENU:
                this.setScene(ggmView);
                break;
            case GAME_CONFIGURATION:
                this.setScene(ggcView);
                break;
            case GAME:
                this.setScene(ggView);
                break;
            default:
                throw new IllegalArgumentException();
        }
        show();
    }
}
