package framework.player;

import framework.board.Board;

/**
 * this class is a subclass of Player and stores a local player.
 */
public class LocalPlayer extends Player {
    public LocalPlayer(Board board, String name) {
        super(board, name);
    }

    public LocalPlayer(Board board) {
        super(board);
    }

    /**
     * Executes a move as the local player.
     *
     * @param x The X-coordinate to move to
     * @param y The Y-coordinate to move to
     */
    public void executeMove(int x, int y) {
        board.makeMove(this, x, y);
    }
}
