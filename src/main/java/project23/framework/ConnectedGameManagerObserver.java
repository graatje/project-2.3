package project23.framework;

public interface ConnectedGameManagerObserver {
    void onServerError(String errorMessage);

    void onPlayerListReceive();

    void onChallengeRequestReceive(ChallengeRequest challengeRequest);

    void onPreGameStart();

    void onPostGameStart();
}
