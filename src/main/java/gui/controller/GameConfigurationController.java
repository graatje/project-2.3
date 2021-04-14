package gui.controller;

import framework.ConfigData;
import framework.player.MinimaxAIPlayer;
import gui.MainWindow;
import gui.model.GameConfigurationModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GameConfigurationController extends Controller<GameConfigurationModel> implements Initializable {

    @FXML private Label ipConfirmation;
    @FXML private TextField ipAddressField;
    @FXML private TextField portField;
    @FXML private ComboBox<String> comboBoxDifficulty;
    @FXML private TextField thinkingTimeField;

    @FXML public void pressBackToMainMenu(ActionEvent event){
        mainWindow.switchView(MainWindow.ViewEnum.MAINMENU);
    }

    ObservableList<String> options = FXCollections.observableArrayList(
            Arrays.stream(MinimaxAIPlayer.AIDifficulty.values())
                    .map(MinimaxAIPlayer.AIDifficulty::displayName)
                    .collect(Collectors.toList())
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model.setLabelNode(ipConfirmation);
        comboBoxDifficulty.setItems(options);
        ipAddressField.setText(ConfigData.getInstance().getServerIP());
        portField.setText(ConfigData.getInstance().getServerPort() + "");
        comboBoxDifficulty.setValue(ConfigData.getInstance().getAIDifficulty().displayName());
        thinkingTimeField.setText(String.valueOf(ConfigData.getInstance().getMinimaxThinkingTime()));
    }

    @FXML void pressOKip(ActionEvent event){
        model.setIPandPort(ipAddressField.getText(), portField.getText());
        model.setAIThinkingTime(thinkingTimeField.getText());
        model.setInfoMessage("Updated"); // Regression: not showing "henlo" anymore
        model.updateView();

        MinimaxAIPlayer.AIDifficulty difficulty = MinimaxAIPlayer.AIDifficulty.fromDisplayName(comboBoxDifficulty.getValue());
        ConfigData.getInstance().setAIDifficulty(difficulty);
    }
}