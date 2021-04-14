package connection;

import java.util.List;

public interface GameManagerCommunicationListener {
    void startServerMatch(String opponent, String playerToBegin);

    void getMatchRequest(String opponent, String gametype, int challengeNr);

    void matchCancelled(int challengeNr);

    void updateLobbyPlayers(List<String> lobbyPlayers);

    void endMatch(String result);

    void onServerError(String errorMessage);
}
