package project23.framework.player.network;

public class SimulationResponse {
    public int amount;
    public float average;
    public SimulationResponse(int amount, float average){
        this.amount = amount;
        this.average = average;
    }

    public String toString(){
        return "simulations: " + this.amount + ", average: " + this.average;
    }
}
