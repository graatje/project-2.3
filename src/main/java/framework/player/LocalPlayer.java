package framework.player;

import framework.board.Board;
import framework.board.BoardPiece;

import java.util.List;

/**
 * this class is a subclass of Player and stores a local player.
 */
public class LocalPlayer extends Player implements MoveRequestable {
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

    @Override
    public void requestMove() {
        // TODO: Maybe make this a button in the GUI?
        List<BoardPiece> validMoves = board.getValidMoves();
        if(validMoves.isEmpty()) {
            board.makeMove(this, null);
        }
    }
}
