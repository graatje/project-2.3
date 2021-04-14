package gui;

import framework.ConfigData;
import gui.controller.*;
import gui.model.*;
import gui.view.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainWindow {
    private Stage stage;
    private GenericGameConfigurationView ggcView;
    private GameView ggView;
    private GenericGameMenuView ggmView;
    private MainMenuView mmView;
    private GameLobbyView glView;

    public static final int WINDOW_WIDTH = 650;
    public static final int WINDOW_HEIGHT = 750;

    public enum ViewEnum {
        GAME_LOBBY,
        MAINMENU,
        GAME_MENU,
        GAME_CONFIGURATION,
        GAME
    }

    public MainWindow(Stage primaryStage) throws IOException {
        this.stage = primaryStage;
        setupMVC();

        stage.setOnCloseRequest(windowEvent -> this.quit());

        //stage = primaryStage;
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/icon.png")).toExternalForm()));
        stage.setTitle("C4Games");
        switchView(ViewEnum.MAINMENU);
    }

    public void quit() {
        if(ConfigData.getInstance().getGameManager() != null) {
            ConfigData.getInstance().getGameManager().destroy();
        }
    }

    private void setupMVC() throws IOException {
        // Game settings
        GenericGameConfigurationModel ggcModel = new GenericGameConfigurationModel();
        GenericGameConfigurationController ggcController = new GenericGameConfigurationController();
        ggcController.setMainWindow(this);
        ggcController.setModel(ggcModel);
        ggcView = new GenericGameConfigurationView(
                getFXMLParent("GenericGameConfiguration.fxml", ggcController), ggcController, WINDOW_WIDTH, WINDOW_HEIGHT);
        ggcModel.registerView(ggcView);

        // Gameboard
        GenericGameModel ggModel = new GenericGameModel();
        GenericGameController ggController = new GenericGameController();
        ggController.setMainWindow(this);
        ggController.setModel(ggModel);
        ggView = new GameView(
                getFXMLParent("GenericGame.fxml", ggController),
                ggController,
                WINDOW_WIDTH,
                WINDOW_HEIGHT
        );
        ggModel.registerView(ggView);

        // Game lobby
        GameLobbyModel glModel = new GameLobbyModel(ggModel, this);
        GameLobbyController glController = new GameLobbyController();
        glController.setMainWindow(this);
        glController.setModel(glModel);
        glView = new GameLobbyView(
                getFXMLParent("GameLobby.fxml", glController), glController, WINDOW_WIDTH, WINDOW_HEIGHT);
        glModel.registerView(glView);

        // Game Menu
        GenericGameMenuModel ggmModel = new GenericGameMenuModel(ggModel, glModel);
        GenericGameMenuController ggmController = new GenericGameMenuController();
        ggmController.setMainWindow(this);
        ggmController.setModel(ggmModel);
        ggmView = new GenericGameMenuView(
                getFXMLParent("GenericGameMenu.fxml", ggmController), ggmController, WINDOW_WIDTH, WINDOW_HEIGHT);
        ggmModel.registerView(ggmView);

        // Main Menu
        //MainMenuModel mmModel = new MainMenuModel();
        MainMenuController mmController = new MainMenuController();
        mmController.setMainWindow(this);
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
    public void switchView(ViewEnum view) {
        switch (view) {
            case MAINMENU:
                stage.setScene(mmView);
                break;
            case GAME_MENU:
                stage.setScene(ggmView);
                break;
            case GAME_CONFIGURATION:
                stage.setScene(ggcView);
                break;
            case GAME:
                stage.setScene(ggView);
                break;
            case GAME_LOBBY:
                stage.setScene(glView);
                break;
            default:
                throw new IllegalArgumentException();
        }
        stage.show();
    }
}