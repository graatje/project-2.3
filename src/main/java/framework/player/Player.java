package framework.player;

import framework.board.Board;

/**
 * This abstract class stores a player, including the Board.
 */
public abstract class Player {
    protected final Board board;
    
    /**
     * constructor
     * @param board, the Board.
     */
    public Player(Board board) {
        this.board = board;
    }

    /**
     * getter for the board
     * @return Board board
     */
    public Board getBoard() {
        return board;
    }
}
