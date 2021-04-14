package project23.framework;

public enum GameType {
    OTHELLO("Reversi", "Othello"),
    TIC_TAC_TOE("Tic-tac-toe", "Tic tac toe");

    public final String serverName;
    public final String displayName;

    GameType(String serverName, String displayName) {
        this.serverName = serverName;
        this.displayName = displayName;
    }

    public static GameType getByServerName(String serverName) {
        for (GameType gameType : values()) {
            if (gameType.serverName.equalsIgnoreCase(serverName)) {
                return gameType;
            }
        }

        return null;
    }
}
