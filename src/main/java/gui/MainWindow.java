package gui;

import framework.ConnectedGameManager;
import framework.GameManager;
import framework.player.LocalPlayer;
import gui.controller.*;
import gui.model.GenericGameConfigurationModel;
import gui.model.GenericGameMenuModel;
import gui.model.GenericGameModel;
import gui.model.MainMenuModel;
import gui.view.GameView;
import gui.view.GenericGameConfigurationView;
import gui.view.GenericGameMenuView;
import gui.view.MainMenuView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import ttt.board.TTTBoard;
import ttt.player.TTTAIPlayer;

import java.io.IOException;
import java.util.Arrays;

public class MainWindow extends Stage {
    private Stage stage;
    private GenericGameConfigurationView ggcView;
    private GameView ggView;
    private GenericGameMenuView ggmView;
    private MainMenuView mmView;
    //private Stack<viewEnum> viewStack = new Stack();

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
        GenericGameConfigurationModel ggcModel = new GenericGameConfigurationModel();
        GenericGameConfigurationController ggcController = new GenericGameConfigurationController();
        ggcController.setMainWindow(this);
        ggcController.setModel(ggcModel);
        ggcView = new GenericGameConfigurationView(
                getFXMLParent("GenericGameConfiguration.fxml", ggcController), ggcController, WINDOW_WIDTH, WINDOW_HEIGHT);
        ggcModel.registerView(ggcView);

        // TTT Gameboard
        //TODO: dit dynamisch doen als othello/ttt geselecteerd wordt
//        ConnectedGameManager gameManager;
//        try {
//            gameManager = new ConnectedGameManager(TTTBoard::new, "main-vps.woutergritter.me", 7789, b -> new TTTAIPlayer(b, 3));
//        } catch (IOException e) {
//            e.printStackTrace();
//
//            System.exit(-1);
//            return;
//        }
//        gameManager.setSelfName("2zqa");

        GameManager gameManager = new GameManager(TTTBoard::new);
        gameManager.addPlayer(new LocalPlayer(gameManager.getBoard(), "Kees"));
        gameManager.addPlayer(new TTTAIPlayer(gameManager.getBoard(), "Robot1", 2));

        GenericGameModel ggModel = new GenericGameModel(gameManager);
        GenericGameController ggController = new GenericGameController();
        ggController.setMainWindow(this);
        ggController.setModel(ggModel);
        ggView = new GameView(
                getFXMLParent("GenericGame.fxml", ggController),
                ggController,
                WINDOW_WIDTH,
                WINDOW_HEIGHT,
                Arrays.asList(
                        getClass().getResource("/boardPieces/x.png"),
                        getClass().getResource("/boardPieces/o.png")
                )
        );
        ggModel.registerView(ggView);

        gameManager.start(null);
//        gameManager.login();
//        gameManager.subscribe("Tic-tac-toe");

        // Game Menu
        GenericGameMenuModel ggmModel = new GenericGameMenuModel();
        GenericGameMenuController ggmController = new GenericGameMenuController();
        ggmController.setMainWindow(this);
        ggmController.setModel(ggmModel);
        ggmView = new GenericGameMenuView(
                getFXMLParent("GenericGameMenu.fxml", ggmController), ggmController, WINDOW_WIDTH, WINDOW_HEIGHT);
        ggmModel.registerView(ggmView);

        // Main Menu
        MainMenuModel mmModel = new MainMenuModel();
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
//    public void goBackView(){
//        if(!viewStack.isEmpty()){
//            switchView(viewStack.pop());
//        }
//    }

}