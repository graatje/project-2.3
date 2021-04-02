package gui;

import gui.controller.*;
import gui.model.GenericGameConfigurationModel;
import gui.model.GenericGameMenuModel;
import gui.model.GenericGameModel;
import gui.model.MainMenuModel;
import gui.view.GenericGameConfigurationView;
import gui.view.GenericGameMenuView;
import gui.view.GenericGameView;
import gui.view.MainMenuView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

    public enum viewEnum {
        MAINMENU,
        GAME_MENU,
        GAME_CONFIGURATION,
        GAME
    }

    public MainWindow(Stage primaryStage) throws IOException {
        setupMVC();

        this.stage = primaryStage;
        switchView(viewEnum.MAINMENU);
    }

    private void setupMVC() throws IOException {
        // Game settings
        GenericGameConfigurationController ggcController = new GenericGameConfigurationController();
        ggcController.setMainWindow(this);
        GenericGameConfigurationModel ggcModel = new GenericGameConfigurationModel();
        ggcView = new GenericGameConfigurationView(
                getFXMLParent("GenericGameConfiguration.fxml", ggcController), ggcController, WINDOW_WIDTH, WINDOW_HEIGHT);
        //TODO: fxml bestanden hoofdletters of niet?

        // Gameboard
        GenericGameController ggController = new GenericGameController();
        ggController.setMainWindow(this);
        GenericGameModel ggModel = new GenericGameModel();
        ggView = new GenericGameView(
                getFXMLParent("GenericGame.fxml", ggController), ggController, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Game Menu
        GenericGameMenuController ggmController = new GenericGameMenuController();
        ggmController.setMainWindow(this);
        GenericGameMenuModel ggmModel = new GenericGameMenuModel();
        ggmView = new GenericGameMenuView(
                getFXMLParent("GenericGameMenu.fxml", ggmController), ggmController, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Main Menu
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml_example.fxml"));
        MainMenuController mmController = new MainMenuController();
        mmController.setMainWindow(this);
        //MainMenuModel mmModel = new MainMenuModel(); // not needed?
        mmView = new MainMenuView(
                getFXMLParent("MainMenu.fxml", mmController), mmController, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    /**
     * Returns a Parent object with specified controller set
     * @param fxmlFile the fxml file
     * @param controller the controller
     * @return Parent object of the fxml file
     * @throws IOException when the fxml file isn't found
     */
    private Parent getFXMLParent(String fxmlFile, Controller controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/"+fxmlFile));
        fxmlLoader.setController(controller);
        return fxmlLoader.load();
    }

    /**
     * Sets the view to one of the specified views
     * @param view which view to show
     */
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
