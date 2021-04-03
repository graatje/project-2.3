package gui.controller;

import gui.MainWindow;
import gui.model.GenericGameConfigurationModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class GenericGameConfigurationController extends Controller {

    @FXML Text ipConfirmation;
    @FXML TextField ipAddressField;

    @FXML public void pressBackToMainMenu(ActionEvent event){
        mainWindow.switchView(MainWindow.viewEnum.MAINMENU);
    }

    @FXML void pressOKip(ActionEvent event){
        ((GenericGameConfigurationModel)model).setIP(ipAddressField.getText());
        ipConfirmation.setText("[TESTING] IP set to \""+ipAddressField.getText() +"\".");
    }

}

