package framework;

import java.util.List;

public interface ConnectedGameManagerObserver {
    void onServerError(String errorMessage);
    void onPlayerListReceive(List<String> latestPlayerList);
    void onChallengeReceive(Match match);
}
