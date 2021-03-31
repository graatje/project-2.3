package framework.player;

import framework.board.Board;

/**
 * This abstract class is a subclass of Player and stores an AIPlayer. implements MoveRequestable.
 */
public abstract class AIPlayer extends Player implements MoveRequestable {
	/**
	 * 
	 * @param board, the playing board, calls constructor of superclass Player
	 */
    public AIPlayer(Board board, String name) {
        super(board, name);
    }
}
