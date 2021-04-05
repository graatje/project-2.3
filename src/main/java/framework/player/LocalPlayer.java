package framework.player;

import framework.board.Board;

/**
 * this class is a subclass of Player and stores a local player.
 */
public class LocalPlayer extends Player {
    /**
     * constructor, calls constructor of superclass.
     *
     * @param board Board
     */
    public LocalPlayer(Board board, String name) {
        super(board, name);
    }

    public void executeMove(int x, int y) {
        board.makeMove(this, x, y);
    }
}
