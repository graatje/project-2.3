package gui.model;

public class GenericGameConfigurationModel extends Model{

    public void setIP(String ip){
        // TODO: moeten geldige ip worden, dus geen !@# etc.
        //TODO: naar "data"-klasse?
        System.out.println("IP will be set to: \""+ip+"\".");
    }

    public void getIP(){
    }

}
