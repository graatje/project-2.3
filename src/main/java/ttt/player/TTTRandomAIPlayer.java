package ttt.player;

import framework.board.Board;
import framework.board.piece.BoardPiece;
import framework.player.AIPlayer;

import java.util.List;

public class TTTRandomAIPlayer extends AIPlayer {
    public TTTRandomAIPlayer(Board board) {
        super(board);
    }

    @Override
    public void requestMove() {
        List<BoardPiece> validMoves = board.getValidMoves();
        BoardPiece move = validMoves.get((int) (Math.random() * validMoves.size()));

        board.makeMove(this, move);
    }
}
