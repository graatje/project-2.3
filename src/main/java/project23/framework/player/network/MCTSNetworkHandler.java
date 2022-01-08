package project23.framework.player.network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import project23.framework.board.Board;
import project23.framework.board.BoardPiece;
import project23.util.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * this class handles multiple clients.
 */
public class MCTSNetworkHandler {
    private ServerSocket serverSocket;
    private ArrayList<MCTSClient> clients = new ArrayList<MCTSClient>();
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
     * @return
     */
    public HashMap<BoardPiece, SimulationResponse> readClients(){
        HashMap<BoardPiece, SimulationResponse> simulations = new HashMap<>();
        for(MCTSClient client: clients){
            try {
                JSONObject response = client.read();
                if(response == null){
                    continue;
                }
                if(response.get("type").equals("reportResult")){
                    JSONArray arr = (JSONArray) response.get("results");
                    for(int i=0; i < arr.length(); ++i){
                        JSONObject resp = (JSONObject) arr.get(i);
                        JSONObject move = (JSONObject) resp.get("move");
                        BoardPiece piece = new BoardPiece(Integer.parseInt((String)move.get("x")),
                                Integer.parseInt((String)move.get("y")));
                        int trials = Integer.parseInt((String)resp.get("trials"));
                        float value = Float.parseFloat((String) resp.get("value"));
                        if(simulations.get(piece) != null){
                            simulations.get(piece).average =
                                    (simulations.get(piece).average * simulations.get(piece).amount +
                                    trials * value) /
                                    (simulations.get(piece).amount + trials);
                            simulations.get(piece).amount += simulations.get(piece).amount + trials;
                        }
                        else{
                            simulations.put(piece, new SimulationResponse(trials, value));
                        }
                    }
                    Logger.info("received simulation from client.");
                }
            } catch(SocketException e){
                Logger.warning("closed dead socket.");
                client.close();
            }
            catch (IOException | JSONException | NullPointerException e) {
                e.printStackTrace();
                System.out.println("failed to read.");
            }
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
