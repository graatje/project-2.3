package framework;

public class ConfigData {
    private static ConfigData instance;

    private String serverIP;
    private int serverPort;

    private String playerName;
    private String gameType;


    public static ConfigData getInstance() {
        if (instance == null) {
            instance = new ConfigData();
        }

        return instance;
    }

    private ConfigData() {
        serverIP = "main-vps.woutergritter.me";
        serverPort = 7789;
        playerName = "Group C4";
    }


    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) throws NumberFormatException {
        this.serverPort = Integer.parseInt(serverPort);
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        switch (gameType) {
            case "Tic-tac-toe":
            case "Othello":
                this.gameType = gameType;
                break;
            default:
                throw new RuntimeException("Invalid gametype! Please use \"Tic-tac-toe\" or \"Othello\"");
        }

    }
}
