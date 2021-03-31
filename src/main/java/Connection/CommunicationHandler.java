package Connection;


public class CommunicationHandler {

    private Client client;

    private CommunicationListener currentCommunicationListener;

    public void setClient(Client client) {
        this.client = client;
    }


    /**
     * Handles the messages that were given by the server
     *
     * @param input The message given by the server
     */
    public void handleServerInput(String input) {
        //TODO: Server gave a command, take action accordingly.

        if (input.equals("OK")) return;

        String[] split = input.split(" ");
        if (split[1].equals("GAME")) {
            switch (split[2]) {
                case "MATCH":
                    //A match was assigned to our client.
                    currentCommunicationListener.startMatch();
                    break;
                case "YOURTURN":
                    //It is our turn in the match, request a move.
                    currentCommunicationListener.yourTurn();
                    break;
                case "MOVE":
                    //The opponent has made a move
                    String theirMove = getRestOfString(input, 2);
                    break;
                case "CHALLENGE":
                    //There is new information regarding a challenge!
                    handleChallengeServerMessage(getRestOfString(input, 3));
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
     * Handles the server message for finished games
     *
     * @param result The last part of a SVR CHALLENGE server command
     */
    private void handleGameEndServerMessage(String result) {
        String sub = result.substring(0, 3);

        switch (sub) {
            case "WIN ":
                //We won the game! Notify the board.
                break;
            case "LOSS":
                //We lost the game, notify the board.
                break;
            case "DRAW":
                //The game ended in a draw, notify the board.
                break;
        }
    }

    /**
     * Handles CHALLENGE messages sent by the server
     *
     * @param message The message sent by the server
     */
    private void handleChallengeServerMessage(String message) {

        String params = getInbetween(message, "{", "}");

        if (message.startsWith("CANCELLED")) {
            //Match is cancelled
        } else {
            //TODO: Do something with those parameters
        }
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
     * @param full The full string of which to get the substring from
     * @param start The first appearance of this char is where the substring will start
     * @param end The last appearance of this char is where the substring will end
     * @return
     */
    private String getInbetween(String full, String start, String end) {
        int beginIndex = full.indexOf(start);
        int endIndex = full.lastIndexOf(end);

        String result = "";

        if (beginIndex < 0 || endIndex < 0){
            return full;
        }

        result = full.substring(beginIndex + 1, endIndex);

        return result;
    }

    public void setCurrentCommunicationListener(CommunicationListener currentCommunicationListener) {
        this.currentCommunicationListener = currentCommunicationListener;
    }
}
