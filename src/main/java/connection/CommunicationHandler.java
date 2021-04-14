package connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommunicationHandler {

    protected static final boolean DEBUG = true;

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
        if (DEBUG) {
            System.out.println("DEBUG: from server = " + input);
        }

        if (input.equals("OK")) return;

        JSONObject json = extractJson(input);

        String[] split = input.split(" ");

        switch(split[0].toUpperCase(Locale.ROOT)) {
            case "SVR":
                switch (split[1].toUpperCase(Locale.ROOT)) {
                    case "GAME":
                        switch (split[2].toUpperCase(Locale.ROOT)) {
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
                                //There is new information regarding a challenge!
                                int challengeNumber = Integer.parseInt(json.getString("CHALLENGENUMBER"));

                                if (input.contains("CANCELLED")) {
                                    gameManagerCommunicationListener.matchCancelled(challengeNumber);
                                }else{
                                    String challenger = json.getString("CHALLENGER");
                                    String gametype = json.getString("GAMETYPE");
                                    gameManagerCommunicationListener.getMatchRequest(challenger, gametype, challengeNumber);
                                }
                                break;
                            case "WIN":
                            case "DRAW":
                            case "LOSS":
                                gameManagerCommunicationListener.endMatch(split[2]);
                                break;
                        }
                        break;
                    case "PLAYERLIST":
                        JSONArray playerList = extractJsonPlayerlist(input);

                        List<String> lobbyPlayers = new ArrayList<>();
                        for (int i = 0; i < playerList.length(); i++) {
                            lobbyPlayers.add(playerList.getString(i));
                        }

                        gameManagerCommunicationListener.updateLobbyPlayers(lobbyPlayers);
                        break;
                }
                break;
            case "ERR":
                String message = input.substring("ERR ".length());
                System.err.println("Received error from server: " + message);

                gameManagerCommunicationListener.onServerError(message);
                break;
        }
    }

    /**
     * @param input The message sent by the server
     * @return JSONArray containing the message parameters
     */
    private JSONArray extractJsonPlayerlist(String input) {
        if (input.contains("[")) {
            int startIndex = input.indexOf("[");
            String json = input.substring(startIndex);

            try {
                return new JSONArray(json);
            } catch (JSONException ignored) {
            }
        }
        return null;
    }

    /**
     * @param input The message sent by the server
     * @return JSONObject containing the message parameters
     */
    private JSONObject extractJson(String input) {
        if (input.contains("{")) {
            int startIndex = input.indexOf("{");
            String json = input.substring(startIndex);

            try {
                return new JSONObject(json);
            } catch (JSONException ignored) {
            }
        }

        return null;
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


    /**
     * Challenges a player in the lobby
     *
     * @param playerToChallenge The other player to challenge
     * @param gameType          The game to play
     */
    public void sendChallengeMessage(String playerToChallenge, String gameType) {
        client.sendCommandToServer("challenge \"" + playerToChallenge + "\" \"" + gameType + "\"\n");
    }

    public void sendGetPlayerlistMessage() {
        client.sendCommandToServer("get playerlist\n");
    }
}
