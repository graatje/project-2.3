package Connection;

import java.util.Locale;

import org.json.*;

public class CommunicationHandler {

    private Client client;

    private GameManagerCommunicationListener gameManagerCommunicationListener;
    private ServerPlayerCommunicationListener serverPlayerCommunicationListener;


    /**
     * Sets the current server client
     *
     * @param client The current active Client
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Sets the board listener
     *
     * @param comListener The current BoardListener
     */
    public void setGameManagerCommunicationListener(GameManagerCommunicationListener comListener) {
        this.gameManagerCommunicationListener = comListener;
    }

    public void setServerPlayerCommunicationListener(ServerPlayerCommunicationListener listener) {
        this.serverPlayerCommunicationListener = listener;
    }

    /**
     * Handles the messages that were given by the server
     *
     * @param input The message given by the server
     */
    public void handleServerInput(String input) throws JSONException {
        if (input.equals("OK")) return;

        System.out.println("input = " + input);

        JSONObject json = extractJson(input);

        String[] split = input.split(" ");
        if (split[1].equals("GAME")) {
            switch (split[2]) {
                case "MATCH":
                    //A match was assigned to our client.
                    gameManagerCommunicationListener.startServerMatch(json.getString("OPPONENT"), json.getString("PLAYERTOMOVE"));
                    break;
                case "YOURTURN":
                    //It is our turn in the match, so finalize the turn of the ServerPlayer
                    serverPlayerCommunicationListener.finalizeTurn();
                    break;
                case "MOVE":
                    //The opponent has made a move
                    serverPlayerCommunicationListener.turnReceive(json.getString("PLAYER"), json.getString("MOVE"));
                    break;
                case "CHALLENGE":
                    if (input.contains("CANCELLED")) {
                        gameManagerCommunicationListener.matchCancelled(json.getString("CHALLENGENUMBER"));
                    }
                    //There is new information regarding a challenge!
                    gameManagerCommunicationListener.getMatchRequest(json.getString("CHALLENGER"), json.getString("GAMETYPE"), json.getString("CHALLENGENUMBER"));
                    break;
                default:
                    handleGameEndServerMessage(split[3]);
                    break;
            }
        } else {
            System.out.println(input);
        }
    }


    /**
     * @param input The message sent by the server
     * @return JSONObject containing the message parameters
     */
    private JSONObject extractJson(String input) {
        if(input.contains("{")) {
            int startIndex = input.indexOf("{");
            String json = input.substring(startIndex);

            try {
                return new JSONObject(json);
            } catch (JSONException ignored) {}
        }

        return null;
    }

    /**
     * Handles the server message for finished games
     *
     * @param result The last part of a SVR CHALLENGE server command
     */
    private void handleGameEndServerMessage(String result) {
        String sub = result.substring(0, 3);
        serverPlayerCommunicationListener.endMatch(sub);
    }

    /**
     * Send a login message to the server
     *
     * @param playerName Our player name
     */
    public void sendLoginMessage(String playerName) {
        client.sendCommandToServer("login " + playerName + "\n");
    }

    /**
     * Send a logout message to the server
     */
    public void sendLogoutMessage() {
        client.sendCommandToServer("logout \n");
    }

    /**
     * Send the server a message to appear on the challengeboard for a game type
     *
     * @param gameType The type of game to subscribe for
     */
    public void sendSubscribeMessage(String gameType) {
        gameType = gameType.toUpperCase(Locale.ROOT);

        client.sendCommandToServer("subscribe " + gameType + "\n");
    }

    /**
     * Send our chosen move to the server
     *
     * @param move Our chose move
     */
    public void sendMoveMessage(int move) {

        client.sendCommandToServer("move " + move + "\n");
    }

    /**
     * Send the server a message to accept a challenge
     *
     * @param challengeNumber The challenge number to accept
     */
    public void sendAcceptChallengeMessage(String challengeNumber) {
        client.sendCommandToServer("challenge accept " + challengeNumber + "\n");
    }

    /**
     * Let the server know we forfeited the match
     */
    public void sendForfeitMessage() {
        client.sendCommandToServer("forfeit \n");
    }


}
