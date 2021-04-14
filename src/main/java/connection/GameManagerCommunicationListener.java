package connection;

import java.util.List;

public interface GameManagerCommunicationListener {
    void startServerMatch(String opponent, String playerToBegin);

    void onChallengeRequestReceive(String opponent, String gameTypeServerName, int challengeNr);

    void challengeRequestCancelled(int challengeNr);

    void updateLobbyPlayers(List<String> lobbyPlayers);

    void endMatch(String result);

    void onServerError(String errorMessage);
}
