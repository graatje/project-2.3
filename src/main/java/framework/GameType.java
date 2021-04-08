package framework;

public enum GameType {
    TTT("Tic-tac-toe", false),
    OTHELLO("Reversi", false),
    TTT_LOCAL("Tic-tac-toe", true),
    TTT_ONLINE("Tic-tac-toe", false),
    OTHELLO_LOCAL("Reversi", true), //Do you mean: Othello?
    OTHELLO_ONLINE("Reversi", false);

    public String gameName;
    public boolean isLocal;

    GameType(String gameName, boolean isLocal){
        this.gameName = gameName;
        this.isLocal = isLocal;
    }


}
