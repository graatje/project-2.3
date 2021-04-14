package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Thread {
    private static final long KEEP_ALIVE_INTERVAL = 30 * 1000;

    private final Socket clientSocket;
    private BufferedReader inputStream;
    private final CommunicationHandler communicationHandler;
    private PrintWriter outputStream;

    private boolean running = true;


    /**
     * @param clientSocket The clientsocket
     * @param communicationHandler          Communication handler which handles the communicationprotocol.
     */
    public Client(Socket clientSocket, CommunicationHandler communicationHandler) {
        this.clientSocket = clientSocket;
        this.communicationHandler = communicationHandler;

        communicationHandler.setClient(this);

        try {
            this.inputStream = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            this.outputStream = new PrintWriter(this.clientSocket.getOutputStream());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        if(CommunicationHandler.DEBUG) {
            startConsolePassthroughThread();
        }

        startKeepAliveThread();
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

    public void startKeepAliveThread() {
        Thread thread = new Thread(this::keepAlive, "ServerKeepAlive");
        thread.setDaemon(true);
        thread.start();
    }

    private void keepAlive() {
        System.out.println("Keep alive thread started.");

        while(running) {
            try {
                Thread.sleep(KEEP_ALIVE_INTERVAL);
            } catch (InterruptedException ignored) {}

            sendKeepAlive();
        }

        System.out.println("Keep alive thread stopped.");
    }

    private void sendKeepAlive() {
        System.out.println("Sending keep-alive message..");
        sendGetPlayerlistMessage();
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
                        communicationHandler.handleServerInput(input);
                    } catch (Exception e) {
                        System.err.println("Could not handle server input '" + input + "': " + e.toString());
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
        communicationHandler.sendLogoutMessage();
        running = false;

        try {
            clientSocket.close();
        } catch (Exception ignored) {}
    }

    public CommunicationHandler getCommunicationHandler() {
        return communicationHandler;
    }

    public void sendForfeitMessage() {
        communicationHandler.sendForfeitMessage();
    }

    public void sendAcceptChallengeMessage(String challengeNumber) {
        communicationHandler.sendAcceptChallengeMessage(challengeNumber);
    }

    public void sendMoveMessage(int move) {
        communicationHandler.sendMoveMessage(move);
    }

    public void sendSubscribeMessage(String gameType) {
        communicationHandler.sendSubscribeMessage(gameType);
    }

    public void sendLogoutMessage() {
        communicationHandler.sendLogoutMessage();
    }

    public void sendLoginMessage(String playerName) {
        communicationHandler.sendLoginMessage(playerName);
    }

    public void sendChallengeMessage(String playerToChallenge, String gameType) {
        communicationHandler.sendChallengeMessage(playerToChallenge, gameType);
    }

    public void sendGetPlayerlistMessage() {
        communicationHandler.sendGetPlayerlistMessage();
    }

    public void acceptChallenge(int challengeNr) {
        communicationHandler.sendAcceptChallengeMessage("" + challengeNr);
    }
}
