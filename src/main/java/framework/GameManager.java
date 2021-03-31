package framework;

import Connection.Connection;
import framework.board.Board;
import framework.factory.BoardFactory;
import framework.player.Player;
/**
 * This class manages a game. Including connection, board and players.
 */
public abstract class GameManager {

    private final Connection connection;
    private final Board board;
    private final Player[] players;
   	/**
	 * constructor, initializes connection, board and players.
	 * @param Connection connection
	 * @param BoardFactory boardFactory
	 */
    public GameManager(Connection connection, BoardFactory boardFactory) {
 
        this.connection = connection;
        this.board = boardFactory.createBoard(this);
        this.players = new Player[2];
    }
    
    /**
	 * this method requests a playermove from the board if all players have been initialized.
	 */
    public void start() {
    	
        for(Player player : players) {
            if(player == null) {
                throw new IllegalStateException("Not all players have been initialized yet!");
            }
        }

        // Request a move from the first player
        board.requestPlayerMove();
    }
    
	/**
	 * getter for connection
	 * @return Connection
	 */
    public Connection getConnection() {
    
        return connection;
    }
    
	/**
	 * getter for board.
	 * @return Board
	 */
    public Board getBoard() {

        return board;
    }
    
	/**
	 * Getter for the array players
	 * @return Player[]
	 */
    public Player[] getPlayers() {

        return players;
    }

    public Player getPlayer(int id) {
    	/**
    	 * getter for an individual player from the array players.
    	 * @param int id, the index of the player in the arraylist players.
    	 * @return Player player.
    	 */
        if(id < 0 || id >= players.length) {
            throw new IllegalArgumentException("Invalid player ID!");
        }

        return players[id];
    }

	/**
	 * Setter for player of the array players.
	 * @param int id, the id of the player
	 * @param Player player, the player you want on the index of the given id.
	 */
    public void setPlayer(int id, Player player) {

        if(id < 0 || id >= players.length) {
            throw new IllegalArgumentException("Invalid player ID!");
        }

        players[id] = player;
    }
}
