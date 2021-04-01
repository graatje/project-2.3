package Connection;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
    public void handleServerInput(String input) {
        if (input.equals("OK")) return;

        String[] split = input.split(" ");
        if (split[1].equals("GAME")) {
            switch (split[2]) {
                case "MATCH":
                    //A match was assigned to our client.
                    Map<String, String> match = dissectMatchMessage(input);
                    gameManagerCommunicationListener.startServerMatch(match.get("OPPONENT"), match.get("PLAYERTOMOVE"));
                    break;
                case "YOURTURN":
                    //It is our turn in the match, request a move.
                    gameManagerCommunicationListener.ourTurn();
                    break;
                case "MOVE":
                    //The opponent has made a move
                    String theirMove = getRestOfString(input, 2);
                    serverPlayerCommunicationListener.opponentTurn(dissectMoveMessage(theirMove));
                    break;
                case "CHALLENGE":
                    if (input.contains("CANCELLED")) {
                        gameManagerCommunicationListener.matchCancelled(handleChallengeCancelled(input));
                    }
                    //There is new information regarding a challenge!
                    Map<String, String> challenge = dissectChallengeMessage(input);
                    gameManagerCommunicationListener.getMatchRequest(challenge.get("OPPONENT"), challenge.get("GAMETYPE"), challenge.get("CHALLENGENUMBER"));
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
     * Extracts the move made by the online opponent from the server message
     *
     * @param message The message given by the server
     * @return The move the opponent made
     */
    private String dissectMoveMessage(String message) {
        String result = "";

        message = getInbetween(message, "MOVE:", "}");
        result = message.strip();

        return result;
    }


    /**
     * Extracts the Opponent's name, game type and challenge number from the server message
     *
     * @param serverMessage The message sent by the server
     * @return A map containing "OPPONENT", "GAMETYPE" and "CHALLENGENUMBER" with corresponding values
     */
    private Map<String, String> dissectChallengeMessage(String serverMessage) {
        Map<String, String> result = new HashMap<String, String>();

        int offset = 0;

        result.put("OPPONENT", getInbetween(serverMessage, "CHALLENGER: ", ","));
        offset = serverMessage.indexOf(",");
        serverMessage = getRestOfString(serverMessage, offset);

        result.put("GAMETYPE", getInbetween(serverMessage, "GAMETYPE: ", ","));
        offset = serverMessage.indexOf(",");
        serverMessage = getRestOfString(serverMessage, offset);

        result.put("CHALLENGENUMBER", getInbetween(serverMessage, "CHALLENGENUMBER: ", "}"));

        return result;
    }


    /**
     * Gets the challenge number of the cancelled challenge
     *
     * @param serverMessage The message sent by the server
     * @return The challenge number
     */
    private String handleChallengeCancelled(String serverMessage) {
        return getInbetween(serverMessage, "CHALLENGENUMBER: ", "}");
    }

    /**
     * Extracts the Opponent's name and beginning player's name from the message
     *
     * @param serverMessage The message sent by the server
     * @return A Map containing "OPPONENT" and "PLAYERTOMOVE" keys with corresponding values
     */
    private Map<String, String> dissectMatchMessage(String serverMessage) {
        Map<String, String> result = new HashMap<String, String>();

        result.put("OPPONENT", getInbetween(serverMessage, "OPPONENT: ", "}"));
        result.put("PLAYERTOMOVE", getInbetween(serverMessage, "PLAYERTOMOVE: ", ","));

        return result;
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
     * @param input  The full input string
     * @param offset The amount of sub-commands to ignore at the beginning of the string
     * @return returns the rest of the command as 1 single String
     */
    private String getRestOfString(String input, int offset) {
        String[] split = input.split(" ");
        String result = "";

        for (int i = offset; i < split.length; i++) {
            result += split[i];
        }

        return result;
    }

    /**
     * Returns a substring of "full"
     *
     * @param full  The full string of which to get the substring from
     * @param start The first appearance of this char is where the substring will start
     * @param end   The last appearance of this char is where the substring will end
     * @return
     */
    private String getInbetween(String full, String start, String end) {
        int beginIndex = full.indexOf(start);
        int endIndex = full.lastIndexOf(end);

        String result = "";

        if (beginIndex < 0 || endIndex < 0) {
            return full;
        }

        result = full.substring(beginIndex + 1, endIndex);

        return result;
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
    public void sendMoveMessage(String move) {
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
