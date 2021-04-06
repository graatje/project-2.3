package gui.model;

import framework.ConfigData;
import gui.view.View;

public class GenericGameConfigurationModel extends Model{

    public void setIPandPort(String ip, String port) {
        System.err.println("DEBUG: IP will be set to: \""+ip+"\".");
        try {
            ConfigData.getInstance().setServerIP(ip);
            ConfigData.getInstance().setServerPort(port);
        } catch(NumberFormatException e) {
            for(View view : observers) {
                view.showDialog("Please enter a valid port.");
            }
        }
    }
}
