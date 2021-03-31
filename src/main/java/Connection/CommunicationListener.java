package Connection;

public interface CommunicationListener {
    void startMatch(String opponent, String playerToBegin);
    void ourTurn();
    void opponentTurn(String move);
    void getMatchRequest(String opponent, String gametype, String challengeNR);
    void matchCancelled(String challengeNR);
}
