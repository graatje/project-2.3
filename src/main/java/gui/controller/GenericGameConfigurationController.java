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
    @FXML TextField portField;

    @FXML public void pressBackToMainMenu(ActionEvent event){
        mainWindow.switchView(MainWindow.viewEnum.MAINMENU);
    }

    @FXML void pressOKip(ActionEvent event){
        ((GenericGameConfigurationModel)model).setIPandPort(ipAddressField.getText(), portField.getText());
        ipConfirmation.setText("[TESTING] IP set to \""+ipAddressField.getText() +"\".");
    }

}

