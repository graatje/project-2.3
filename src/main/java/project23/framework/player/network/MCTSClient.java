package project23.framework.player.network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import project23.framework.ConfigData;
import project23.framework.board.Board;
import project23.framework.board.BoardPiece;
import project23.util.Logger;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Objects;

import static java.lang.System.currentTimeMillis;

/**
 * this class handles communication to a single client.
 */
public class MCTSClient {
    private BufferedWriter out;
    private BufferedReader in;
    private final Socket clientSocket;
    private boolean closed;
    private int boardInt = 0;
    private int thinkingtime = ConfigData.getInstance().getMinimaxThinkingTime() - 100;
    public HashMap<Integer, Long> responsetimes = new HashMap<>();
    HashMap<Integer, HashMap<BoardPiece, SimulationResponse>> simulations = new HashMap<>();

    public MCTSClient(Socket socket){
        this.closed = false;
        clientSocket = socket;
        try {
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<BoardPiece, SimulationResponse> getSimulationResponse(int boardInt){
        HashMap<BoardPiece, SimulationResponse> sim = simulations.get(boardInt);
        if(sim == null && getBoardInt() <= boardInt){
            // send slowdown? @todo
            sendSlowDown();
        }
        return sim;
    }

    public void removeSimulation(int boardInt){
        simulations.remove(boardInt);
    }

    public void sendSlowDown(){
        if(closed){
            return;
        }
        this.thinkingtime -= 100;
        Logger.warning("thinking time of client reduced to " + thinkingtime + " milliseconds");
    }
    public void setBoardint(int boardInt){
        this.boardInt = boardInt;
    }

    public int getBoardInt(){
        return boardInt;
    }

    /**
     * sends the board and whose turn it is.
     * @param board the board to send.
     */


    public void sendBoard(Board board, int boardInt){
        if(closed){
            return;
        }
        BoardPiece boardPiece;
        int owner;
        JSONObject msg = new JSONObject();
        JSONObject jsonboard = new JSONObject();
        try {
            msg.put("type", "sendboard");
            setBoardint(boardInt);
            msg.put("boardint", boardInt);
            msg.put("thinkingtime", thinkingtime);
            for(int y = 0; y < board.getHeight(); y++){
                for(int x = 0; x < board.getWidth(); x++){
                    boardPiece = board.getBoardPiece(x, y);
                    if(boardPiece.getOwner() != null) {
                        owner = boardPiece.getOwner().getID();
                    }
                    else{
                        owner = -1;  // no owner.
                    }
                    jsonboard.put(Integer.toString(x + board.getWidth() * y),
                            owner);
                }
            }
            msg.put("board", jsonboard);
            msg.put("turn", board.getCurrentPlayer().getID());
            Logger.info("sent a board to a client.");
            write(msg);
            responsetimes.put(boardInt, currentTimeMillis());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * reads while data is available. non-blocking socket.
     * @return null or JSONObject with the received data.
     * @throws IOException when failing to read it can throw an IOException.
     */
    public JSONObject read() throws IOException {
        if(closed){
            return null;
        }
        String msg = null;
        try {
            msg = in.readLine();
        }
        catch (SocketException e){
            e.printStackTrace();
            Logger.warning("closed a client cause of an exception when reading.");
            close();
        }
        if(msg == null){
            return null;
        }
        Logger.info("received:" + msg);
        if(msg.length() == 0){
            return null;
        }
        try {
            return new JSONObject(msg);
        } catch (JSONException e) {
            Logger.warning("received non-json data. " + msg);
            return null;
        }
    }

    /**
     * writes a json object to the client.
     * @param message the json object to write to the client.
     */
    private void write(JSONObject message) {
        if(closed){
            return;
        }
        try {
            out.write(message.toString());
            out.flush();
        }catch (IOException e){
            Logger.warning("failed to send message.");
        }
    }

    /**
     * sends the message on success of verification.
     */
    public void verificationSuccess(){
        try {
            JSONObject successMsg = new JSONObject();
            successMsg.put("type", "initialize");
            successMsg.put("msg", "success!");
            write(successMsg);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * sends the message on failing the verification.
     */
    public void verificationFail(){
        try {
            JSONObject successMsg = new JSONObject();
            successMsg.put("type", "initialize");
            successMsg.put("msg", "verification failed.");
            write(successMsg);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public boolean isClosed(){
        return this.closed;
    }

    public void close(){
        try {
            out.close();
            in.close();
            clientSocket.close();
            this.closed = true;
            ConfigData.getInstance().getNetworkHandler().killClosedClients();
        } catch (IOException ignored) {}
    }

    /**
     * method to handle server input. use this in a thread.
     */
    public void handleClientInput(){
        JSONObject msg;
        while(true){
            if(isClosed()){
                break;
            }
            try {
                msg = read();
                if(msg == null  || msg.get("type") == null) continue;

                switch ((String)(msg.get("type"))){
                    case "reportResult":
                        reportResult(msg);
                        break;
                    default:
                        break;
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void reportResult(JSONObject msg) throws JSONException {

        if(msg.get("boardint") == null) return;
        JSONArray arr = (JSONArray) msg.get("results");
        if(arr == null) return;
        HashMap<BoardPiece, SimulationResponse> receivedSimulations = new HashMap<>();
        for(int i=0; i < arr.length(); ++i) {
            JSONObject resp = (JSONObject) arr.get(i);
            JSONObject move = (JSONObject) resp.get("move");
            BoardPiece boardPiece = new BoardPiece(move.getInt("x"), move.getInt("y"));
            int trials = resp.getInt("trials");
            float value = Float.parseFloat((String) resp.get("value"));
            receivedSimulations.put(boardPiece, new SimulationResponse(trials, value));
        }
        simulations.put(msg.getInt("boardint"), receivedSimulations);
        Logger.info("response for board " + msg.get("boardint") + " received after " +
                (currentTimeMillis() - responsetimes.get(msg.getInt("boardint"))) + "milliseconds.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MCTSClient that = (MCTSClient) o;
        return Objects.equals(out, that.out) && Objects.equals(in, that.in) && Objects.equals(clientSocket, that.clientSocket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(out, in, clientSocket);
    }
}
