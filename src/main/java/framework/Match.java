package framework;

public class Match {
    private final String opponentName;
    private final String gameType;
    private final int challengeNr;

    public Match(String opponentName, String gameType, String challengeNrStr) {
        this.opponentName = opponentName;
        this.gameType = gameType;

        int challengeNr = -1;
        try {
            challengeNr = Integer.parseInt(challengeNrStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        this.challengeNr = challengeNr;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public String getGameType() {
        return gameType;
    }

    public int getChallengeNR() {
        return challengeNr;
    }
}
