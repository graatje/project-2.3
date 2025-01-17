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
    private int currentboardint = 0;
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
    public void checkClientPings(){
        for(MCTSClient client: clients){
            client.checkPing();
        }
    }

    public HashMap<BoardPiece, SimulationResponse> readClients(){
        HashMap<BoardPiece, SimulationResponse> simulations = new HashMap<>();
        for(MCTSClient client: clients){
            int boardint = currentboardint;
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

    public void killClosedClients(){
        for(int i = clients.size(); i >= 0; i--){
            try {
                MCTSClient client = clients.get(i);
                if(client != null && client.isClosed()){
                    clients.remove(i);
                    Logger.warning("removed client " + client.getName());
                }
            }catch (IndexOutOfBoundsException ignored){
            }
        }
    }
    public void sendBoard(Board board){
        currentboardint += 1;
        for(MCTSClient client: clients){
            client.sendBoard(board, currentboardint);
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
                    Logger.info("Client " + client.getName() + " added.");
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ignored) { }
                    client.checkPing();

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

            if(verificationmsg.has("name")){
                client.setName((String) verificationmsg.get("name"));
            }
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
