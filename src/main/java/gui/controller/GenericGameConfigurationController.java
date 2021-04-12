package gui.controller;

import framework.ConfigData;
import framework.player.MinimaxAIPlayer;
import gui.MainWindow;
import gui.model.GenericGameConfigurationModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class GenericGameConfigurationController extends Controller implements Initializable {

    @FXML private Label ipConfirmation;
    @FXML private TextField ipAddressField;
    @FXML private TextField portField;
    @FXML private ComboBox<String> comboBoxDifficulty;
    @FXML private TextField thinkingTimeField;

    @FXML public void pressBackToMainMenu(ActionEvent event){
        mainWindow.switchView(MainWindow.viewEnum.MAINMENU);
    }

    ObservableList<String> options =
            FXCollections.observableArrayList("Easy","Medium", "Hard");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model.setLabelNode(ipConfirmation);
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
        thinkingTimeField.setText(String.valueOf(ConfigData.getInstance().getMinimaxThinkingTime()));
    }

    @FXML void pressOKip(ActionEvent event){
        ((GenericGameConfigurationModel)model).setIPandPort(ipAddressField.getText(), portField.getText());
        ((GenericGameConfigurationModel)model).setAIThinkingTime(thinkingTimeField.getText());
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