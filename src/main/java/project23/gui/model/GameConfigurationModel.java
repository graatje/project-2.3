package project23.gui.model;

import project23.framework.ConfigData;

public class GameConfigurationModel extends Model {

    public void setIPandPort(String ip, String port) {
        ConfigData.getInstance().setServerIP(ip);
        try {
            ConfigData.getInstance().setServerPort(port);
        } catch (NumberFormatException e) {
            setDialogMessageAndTitle("Invalid port", "Error");
            updateView();
        }
    }

    public void setAIThinkingTime(String aiThinkingTime) {
        try {
            int thinkingTime = Integer.parseInt(aiThinkingTime);
            ConfigData.getInstance().setMinimaxThinkingTime(thinkingTime);
        } catch (NumberFormatException e) {
            setDialogMessageAndTitle("AI thinking time must be a number!", "Error");
            updateView();
        }
    }
}
