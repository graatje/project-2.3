package Connection;


public class CommunicationHandler {


    public void handleServerInput(String input) {
        //TODO: Server gave a command, take action accordingly.

        if (input.equals("OK")) return;

        String[] split = input.split(" ");
        if (split[1].equals("GAME")) {
            switch (split[2]) {
                case "MATCH":
                    //A match was assigned to our client.
                    break;
                case "YOURTURN":
                    //It is our turn in the match, request a move.
                    break;
                case "MOVE":
                    //The opponent has made a move
                    String theirMove = getRestOfString(input, 2);
                    break;
                case "CHALLENGE":
                    dissectChallengeCommand(split[3]);
                    break;
            }
        } else {
            System.out.println(input);
        }
    }

    /**
     * @param result The last part of a SVR CHALLENGE server command
     */
    private void dissectChallengeCommand(String result) {
        switch (result) {
            case "WIN":
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
}
