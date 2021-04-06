package framework.player;

import framework.board.Board;

/**
 * This abstract class stores a player, including the Board.
 */
public abstract class Player {
    protected final Board board;
    private int id;
    private String name;

    /**
     * constructor
     *
     * @param board the Board.
     * @param name  the player-name.
     */
    public Player(Board board, String name) {
        this.name = name;
        this.board = board;
    }

    /**
     * constructor without a name
     *
     * @param board the Board.
     */
    public Player(Board board) {
        this(board, "UNNAMED");
    }

    /**
     * getter for the board
     *
     * @return Board board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * @return The player ID
     */
    public int getID() {
        return id;
    }

    /**
     * @param id The new player ID
     */
    public void setID(int id) {
        this.id = id;
    }

    /**
     * @return Name of this player
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for player name
     *
     * @param name The name of this player
     */
    public void setName(String name) {
        this.name = name;
    }
}
