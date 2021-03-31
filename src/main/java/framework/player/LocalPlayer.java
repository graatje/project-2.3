package framework.player;

import framework.board.Board;

/**
 * this class is a subclass of Player and stores a local player.
 */
public class LocalPlayer extends Player {
	/**
	 * constructor, calls constructor of superclass.
	 * @param board Board
	 */
    public LocalPlayer(Board board) {
        super(board);
    }
}
