package framework.player;

import framework.board.Board;

/**
 * This abstract class is a subclass of Player and stores an AIPlayer. implements MoveRequestable.
 */
public abstract class AIPlayer extends Player implements MoveRequestable {
    public AIPlayer(Board board, String name) {
        super(board, name);
    }

    public AIPlayer(Board board) {
        super(board);
    }
}
