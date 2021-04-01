import Controller.GenericGameConfigurationController;
import Controller.GenericGameController;
import Controller.GenericGameMenuController;
import Controller.MainMenuController;
import Model.GenericGameConfigurationModel;
import Model.GenericGameMenuModel;
import Model.GenericGameModel;
import Model.MainMenuModel;
import View.GenericGameConfigurationView;
import View.GenericGameMenuView;
import View.GenericGameView;
import View.MainMenuView;
import javafx.stage.Stage;

public class MainWindow extends Stage {
    GenericGameConfigurationView ggcView;
    GenericGameView ggView;
    GenericGameMenuView ggmView;
    MainMenuView mmView;

    enum viewEnum {
        MAINMENU,
        GAME_MENU,
        GAME_CONFIGURATION,
        GAME
    }

    public MainWindow(){
        //Controllers en views maken
        GenericGameConfigurationController ggcController = new GenericGameConfigurationController();
        GenericGameConfigurationModel ggcModel = new GenericGameConfigurationModel();
        ggcView = new GenericGameConfigurationView();

        GenericGameController ggController = new GenericGameController();
        GenericGameModel ggModel = new GenericGameModel();
        ggView = new GenericGameView();

        GenericGameMenuController ggmController = new GenericGameMenuController();
        GenericGameMenuModel ggmModel = new GenericGameMenuModel();
        ggmView = new GenericGameMenuView();

        MainMenuController mmController = new MainMenuController();
        MainMenuModel mmModel = new MainMenuModel();
        mmView = new MainMenuView();
    }

    public void switchView(viewEnum view){
        switch(view){
            case MAINMENU:
                this.setScene(mmView);
            case GAME_MENU:
                this.setScene(ggmView);
            case GAME_CONFIGURATION:
                this.setScene(ggcView);
            case GAME:
                this.setScene(ggView);
            default:
                throw new IllegalArgumentException();
        }
    }
}
