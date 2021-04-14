package framework;

public enum GameType {
    OTHELLO("Reversi"),
    TIC_TAC_TOE("Tic-tac-toe");

    public final String serverName;

    GameType(String serverName) {
        this.serverName = serverName;
    }

    public static GameType getByServerName(String serverName) {
        for(GameType gameType : values()) {
            if(gameType.serverName.equalsIgnoreCase(serverName)) {
                return gameType;
            }
        }

        return null;
    }
}
