package framework;

import framework.player.MinimaxAIPlayer;

public class ConfigData {
    private static ConfigData instance;

    private GameManager gameManager;

    private String serverIP;
    private int serverPort;

    private String playerName;
    private GameType gameType;

    private MinimaxAIPlayer.AIDifficulty difficulty;

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
        difficulty = MinimaxAIPlayer.AIDifficulty.HARD;
    }

    public MinimaxAIPlayer.AIDifficulty getAIDifficulty() {
        return difficulty;
    }

    public void setAIDifficulty(MinimaxAIPlayer.AIDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
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

    // TODO: moeten geldige usernames (leeg, gekke tekens, lengte) hier of in framework getest worden?
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }
}

