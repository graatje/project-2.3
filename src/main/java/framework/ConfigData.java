package framework;

import framework.player.MinimaxAIPlayer;
import othello.OthelloGame;
import ttt.TTTGame;

import java.util.HashMap;

public class ConfigData {
    private static ConfigData instance;

    private GameManager gameManager;

    private String serverIP;
    private int serverPort;

    private String playerName;
    private GameType gameType;

    private HashMap<String, Game> games = new HashMap<>();
    private String currentGameName;

    private MinimaxAIPlayer.AIDifficulty difficulty;

    private int minimaxThinkingTime;

    public static ConfigData getInstance() {
        if (instance == null) {
            instance = new ConfigData();
        }

        return instance;
    }

    private ConfigData() {
        // Config
        serverIP = "145.33.225.170";
        serverPort = 7789;
        playerName = "GroupC4";
        difficulty = MinimaxAIPlayer.AIDifficulty.HARD;
        minimaxThinkingTime = 6000;

        // Games
        registerGame(new TTTGame());
        registerGame(new OthelloGame());
    }

    private void registerGame(Game game) {
        games.put(game.getGameName(), game);
    }

    public Game getCurrentGame() {
        if(!games.containsKey(currentGameName)) {
            throw new IllegalStateException("Game has not been set in the games list");
        }
        return games.get(currentGameName);
    }

    public void setCurrentGameName(String gameName) {
        this.currentGameName = gameName;
    }

    public String getCurrentGameName() {
        return currentGameName;
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

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getMinimaxThinkingTime(){ return minimaxThinkingTime; }

    public void setMinimaxThinkingTime(int newThinkingTime){ this.minimaxThinkingTime = newThinkingTime; }
}

