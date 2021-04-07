package framework;

public enum GameType {
    TTT("Tic-tac-toe"),
    OTHELLO("Reversi"),
    TTT_LOCAL("Tic-tac-toe"),
    TTT_ONLINE("Tic-tac-toe"),
    OTHELLO_LOCAL("Reversi"), //Do you mean: Othello?
    OTHELLO_ONLINE("Reversi");

    public String gameName;

    GameType(String gameName){
        this.gameName = gameName;
    }

}
