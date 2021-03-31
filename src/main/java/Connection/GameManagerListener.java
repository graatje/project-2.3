package Connection;

public interface GameManagerListener
{
    void startServerMatch(String opponent, String playerToBegin);
    void getMatchRequest(String opponent, String gametype, String challengeNR);
    void matchCancelled(String challengeNR);
    void ourTurn();

}
