package project23.framework.player.network;

import org.json.JSONException;
import org.json.JSONObject;
import project23.framework.board.Board;
import project23.framework.board.BoardPiece;
import project23.util.Logger;

import java.io.*;
import java.net.Socket;

/**
 * this class handles communication to a single client.
 */
public class MCTSClient {
    private BufferedWriter out;
    private DataInputStream in;
    private final Socket clientSocket;
    public MCTSClient(Socket socket){
        clientSocket = socket;
        try {
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sends the board and whose turn it is.
     * @param board the board to send.
     */
    public void sendBoard(Board board){
        BoardPiece boardPiece;
        int owner;
        JSONObject msg = new JSONObject();
        JSONObject jsonboard = new JSONObject();
        try {
            msg.put("type", "sendboard");
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
                            Integer.toString(owner));
                }
            }
            msg.put("board", jsonboard);
            msg.put("turn", board.getCurrentPlayer().getID());
            write(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * reads while data is available. non-blocking socket.
     * @return null or JSONObject with the received data.
     * @throws IOException
     */
    public JSONObject read() throws IOException {
        StringBuilder msg = new StringBuilder();
        while (in.available() > 0){
            int character = in.read();
            msg.append((char)character);
        }
        if(msg.length() == 0){
            return null;
        }
        try {
            return new JSONObject(msg.toString());
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

    public void close(){
        try {
            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException ignored) {}
    }
}
