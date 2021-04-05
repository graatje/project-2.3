package connection;

public interface GameManagerListener
{
    void startMatch(String opponent, String playerToBegin);
    void getMatchRequest(String opponent, String gametype, String challengeNR);
    void matchCancelled(String challengeNR);
    void ourTurn();

}
