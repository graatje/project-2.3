package connection;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends Thread {
    private Socket clientSocket;
    private BufferedReader inputStream;
    private int availableData;
    private CommunicationHandler com;
    private PrintWriter outputStream;

    private Boolean running = true;
    private ArrayList<String> commandBuffer = new ArrayList<>();


    /**
     * @param clientSocket The clientsocket
     * @param com          Communication handler which handles the communicationprotocol.
     */
    public Client(Socket clientSocket, CommunicationHandler com) {
        this.clientSocket = clientSocket;
        this.com = com;

        com.setClient(this);

        try {
            this.inputStream = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            this.outputStream = new PrintWriter(this.clientSocket.getOutputStream());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        if(CommunicationHandler.DEBUG) {
            startConsolePassthroughThread();
        }
    }

    public void startConsolePassthroughThread() {
        Thread thread = new Thread(this::consolePassthrough, "ConsoleServerPassthrough");
        thread.setDaemon(true);
        thread.start();
    }

    private void consolePassthrough() {
        if(!CommunicationHandler.DEBUG) {
            System.err.println("Warning! Starting console <> server command passthrough, but CommunicationHandler.DEBUG is set to false! You won't receive any feedback!");
        }else{
            System.out.println("Console <> server command passthrough started.");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (running) {
                String line = reader.readLine();
                sendCommandToServer(line + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Console <> server command passthrough stopped.");
    }

    /**
     * Send a command to the server
     *
     * @param command The command to send to the server
     */
    public void sendCommandToServer(String command) {
        if (CommunicationHandler.DEBUG) {
            System.out.println("DEBUG: to server   = " + command.trim());
        }

        outputStream.print(command);
        outputStream.flush();
    }


    /**
     * Reads server messages and sends them off to be handled
     */
    @Override
    public void run() {
        running = true;

        String input;
        try {
            while (running && (input = inputStream.readLine()) != null) {
                //There was input, handle it
                if (!input.equals("")) {
                    try {
                        com.handleServerInput(input);
                    } catch (JSONException e) {
                        //TODO: handle this shiz
                    }
                }
            }
        } catch (IOException ignored) {}

        System.out.println("Closed connection to the server.");
    }

    /**
     * Cleans up the client before closing down the thread.
     */
    public void close() {
        com.sendLogoutMessage();
        running = false;

        try {
            clientSocket.close();
        } catch (Exception ignored) {}
    }

    public CommunicationHandler getCommunicationHandler() {
        return com;
    }

    public void sendForfeitMessage() {
        com.sendForfeitMessage();
    }

    public void sendAcceptChallengeMessage(String challengeNumber) {
        com.sendAcceptChallengeMessage(challengeNumber);
    }

    public void sendMoveMessage(int move) {
        com.sendMoveMessage(move);
    }

    public void sendSubscribeMessage(String gameType) {
        com.sendSubscribeMessage(gameType);
    }

    public void sendLogoutMessage() {
        com.sendLogoutMessage();
    }

    public void sendLoginMessage(String playerName) {
        com.sendLoginMessage(playerName);
    }

    public void sendChallengeMessage(String playerToChallenge, String gameType) {
        com.sendChallengeMessage(playerToChallenge, gameType);
    }

    public void acceptChallenge(int challengeNr) {
        com.sendAcceptChallengeMessage("" + challengeNr);
    }
}
