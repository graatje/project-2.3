package framework;

public interface ConnectedGameManagerObserver {
    void onServerError(String errorMessage);
    void onPlayerListReceive();
    void onChallengeReceive(Match match);
    void onPreGameStart();
    void onPostGameStart();
}
