package framework.player;

import framework.board.Board;

public abstract class AIPlayer extends Player implements MoveRequestable {
    public AIPlayer(Board board) {
        super(board);
    }
}
