package framework;

public class Match {
    private final String opponentName;
    private final String gameType;
    private final int challengeNr;

    public Match(String opponentName, String gameType, int challengeNr) {
        this.opponentName = opponentName;
        this.gameType = gameType;
        this.challengeNr = challengeNr;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public String getGameType() {
        return gameType;
    }

    public int getChallengeNr() {
        return challengeNr;
    }
}
