package Connection;

import java.util.List;

public interface GameManagerCommunicationListener {
    void startServerMatch(String opponent, String playerToBegin);

    void getMatchRequest(String opponent, String gametype, String challengeNR);

    void matchCancelled(String challengeNR);

    void updateLobbyPlayers(List<String> lobbyPlayers);

    void endMatch(String result);
}
