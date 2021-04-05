package gui.model;

public class GenericGameMenuModel extends Model{

    public void setPlayerName(String username) {
        // TODO: moeten geldige usernames (leeg, gekke tekens, lengte) hier of in framework getest worden?
        System.out.println("Username will be set to: \""+username+"\".");
    }

    public void getPlayerName(){
    }

}
