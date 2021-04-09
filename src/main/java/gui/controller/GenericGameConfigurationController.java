package gui.controller;

import framework.ConfigData;
import framework.player.MinimaxAIPlayer;
import gui.MainWindow;
import gui.model.GenericGameConfigurationModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class GenericGameConfigurationController extends Controller {

    @FXML Text ipConfirmation;
    @FXML TextField ipAddressField;
    @FXML TextField portField;
    @FXML ComboBox<String> comboBoxDifficulty;

    @FXML public void pressBackToMainMenu(ActionEvent event){
        mainWindow.switchView(MainWindow.viewEnum.MAINMENU);
    }

    ObservableList<String> options =
            FXCollections.observableArrayList("Easy","Medium", "Hard");

    public void initialize() {
        comboBoxDifficulty.setItems(options);
        ipAddressField.setText(ConfigData.getInstance().getServerIP());
        portField.setText(ConfigData.getInstance().getServerPort() + "");
        String value;

        // Could be done with toString(), but this is not as hacky
        switch (ConfigData.getInstance().getAIDifficulty()) {
            case EASY:
                value = "Easy";
                break;
            case MEDIUM:
                value = "Medium";
                break;
            case HARD:
                value = "Hard";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + ConfigData.getInstance().getAIDifficulty());
        }
        comboBoxDifficulty.setValue(value);

    }

    @FXML void pressOKip(ActionEvent event){
        ((GenericGameConfigurationModel)model).setIPandPort(ipAddressField.getText(), portField.getText());
        model.setInfoMessage("Updated"); // Regression: not showing "henlo" anymore
        model.updateView();

        if(comboBoxDifficulty.getValue().equals("Easy")){
            ConfigData.getInstance().setAIDifficulty(MinimaxAIPlayer.AIDifficulty.EASY);
            //System.out.println("EASY");
        }else if (comboBoxDifficulty.getValue().equals("Medium")){
            ConfigData.getInstance().setAIDifficulty(MinimaxAIPlayer.AIDifficulty.MEDIUM);
            //System.out.println("MEDIUM");
        }else if (comboBoxDifficulty.getValue().equals("Hard")){
            ConfigData.getInstance().setAIDifficulty(MinimaxAIPlayer.AIDifficulty.HARD);
            //System.out.println("HARD");
        }
    }
}