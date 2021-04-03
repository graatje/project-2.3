package gui.controller;

import gui.MainWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class GenericGameConfigurationController extends Controller {

    @FXML
    public void pressBackToMainMenu(ActionEvent event){
        mainWindow.switchView(MainWindow.viewEnum.MAINMENU);
    }
    }
