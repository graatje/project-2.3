package Connection;

public interface GameManagerCommunicationListener {
    void startServerMatch(String opponent, String playerToBegin);

    void getMatchRequest(String opponent, String gametype, String challengeNR);

    void matchCancelled(String challengeNR);

}
