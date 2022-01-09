package project23.framework.player.network;

import org.json.JSONException;
import org.json.JSONObject;
import project23.framework.board.Board;
import project23.framework.board.BoardPiece;
import project23.util.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * this class handles multiple clients.
 */
public class MCTSNetworkHandler {
    private ServerSocket serverSocket;
    private final ArrayList<MCTSClient> clients = new ArrayList<>();
    private final String PASSWORD = "HelloWorld";
    public MCTSNetworkHandler(int port){
        try {
            serverSocket = new ServerSocket(port);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * checks if a clients have simulated matches, and returns the simulation if they have.
     * @return hashmap of the average value of a board and the amount of times there was a simulation to get the average value.
     */
    public HashMap<BoardPiece, SimulationResponse> readClients(){
        HashMap<BoardPiece, SimulationResponse> simulations = new HashMap<>();
        for(MCTSClient client: clients){
            int boardint = client.getBoardInt();
            HashMap<BoardPiece, SimulationResponse> simresponses = client.getSimulationResponse(boardint);
            if(simresponses == null){
                continue;
            }
            for(BoardPiece piece: simresponses.keySet()){
                if(simulations.get(piece) != null){
                    simulations.get(piece).average = (simulations.get(piece).average * simulations.get(piece).amount +
                            simresponses.get(piece).amount * simresponses.get(piece).average) /
                            (simulations.get(piece).amount + simresponses.get(piece).amount);
                    simulations.get(piece).amount += simulations.get(piece).amount + simresponses.get(piece).amount;
                }
                else{
                    simulations.put(piece, new SimulationResponse(simresponses.get(piece).amount, simresponses.get(piece).average));
                }
            }
            client.removeSimulation(boardint);
        }
        return simulations;
    }

    public void sendBoard(Board board){
        for(MCTSClient client: clients){
            client.sendBoard(board);
        }
    }

    /**
     * This constantly searches for new clients to connect to.
     */
    public void connectClients(){
        while(true){
            try {

                MCTSClient client = new MCTSClient(serverSocket.accept());
                if(verifyClient(client)) {
                    client.verificationSuccess();
                    clients.add(client);
                    Thread t = new Thread(client::handleClientInput);
                    t.start();
                }else{
                    System.out.println("client failed to verify");
                    client.verificationFail();
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * verifies if the socket is able to communicate with the server properly.
     * @param client, a client to verify if it is a client useable for the messaging.
     */
    private boolean verifyClient(MCTSClient client){
        try {
            Logger.info("trying to connect a client.");
            JSONObject verificationmsg = client.read();
            if(verificationmsg == null){ return false; }
            if(verificationmsg.get("type").equals("initialize")){
                return verificationmsg.get("password").equals(PASSWORD);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

}
