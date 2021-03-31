package framework;

import Connection.Connection;
import framework.board.Board;
import framework.factory.BoardFactory;
import framework.player.Player;

public abstract class GameManager {
    private final Connection connection;
    private final Board board;
    private final Player[] players;

    public GameManager(Connection connection, BoardFactory boardFactory) {
        this.connection = connection;
        this.board = boardFactory.createBoard(this);
        this.players = new Player[2];
    }

    public void start() {
        for(Player player : players) {
            if(player == null) {
                throw new IllegalStateException("Not all players have been initialized yet!");
            }
        }

        // Request a move from the first player
        board.requestPlayerMove();
    }

    public Connection getConnection() {
        return connection;
    }

    public Board getBoard() {
        return board;
    }

    public Player[] getPlayers() {
        return players;
    }

    public Player getPlayer(int id) {
        if(id < 0 || id >= players.length) {
            throw new IllegalArgumentException("Invalid player ID!");
        }

        return players[id];
    }

    public void setPlayer(int id, Player player) {
        if(id < 0 || id >= players.length) {
            throw new IllegalArgumentException("Invalid player ID!");
        }

        players[id] = player;
    }
}
