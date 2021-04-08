package gui.model;

import framework.ConfigData;
import gui.view.View;

public class GenericGameConfigurationModel extends Model{

    public void setIPandPort(String ip, String port) {
        System.err.println("DEBUG: IP will be set to: \""+ip+"\".");
        ConfigData.getInstance().setServerIP(ip);
        try {
            ConfigData.getInstance().setServerPort(port);
        } catch(NumberFormatException e) { //TODO: Eigen exception maken, InvalidConfigDataException
            setDialogMessage("Invalid port");
            updateView();
        }
    }
}
