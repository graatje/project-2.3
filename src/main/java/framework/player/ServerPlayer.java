package framework.player;

import framework.board.Board;

/**
 * This class is a subclass of Player and stores a ServerPlayer.
 */
public class ServerPlayer extends Player {
	/**
	 * constructor, calls constructor of superclass.
	 * @param Board board
	 */
    public ServerPlayer(Board board) {
        super(board);
    }
}
