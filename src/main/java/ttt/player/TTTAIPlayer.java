package ttt.player;

import framework.board.Board;
import framework.board.BoardPiece;
import framework.player.AIPlayer;
import framework.player.Player;

public class TTTAIPlayer extends AIPlayer {
    public TTTAIPlayer(Board board, String name) {
        super(board, name);
    }

    @Override
    public void requestMove() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {
        }

        BoardPiece bestMove = getBestMove(board);
        board.makeMove(board.getCurrentPlayer(), bestMove);
    }

    private int miniMax(Board board, int depth, boolean isMax) {
        int boardVal = evaluateBoard(board, depth);

        if (Math.abs(boardVal) > 0 || depth == 0 || board.getValidMoves().isEmpty()) {
            // Terminal node (win/lose/draw) or max depth reached.
            return boardVal;
        }

        if (isMax) {
            // Maximising player, find the maximum attainable value.
            int highestVal = Integer.MIN_VALUE;

            for (int x = 0; x < board.getWidth(); x++) {
                for (int y = 0; y < board.getHeight(); y++) {
                    BoardPiece piece = board.getBoardPiece(x, y);

                    if (!piece.hasOwner()) {
                        piece.setOwner(this); // temporarily set piece
                        highestVal = Math.max(highestVal, miniMax(board, depth - 1, false));
                        piece.clearOwner();
                    }
                }
            }

            return highestVal;
        } else {
            // Minimising player, find the minimum attainable value;
            Player opponent = board.getGameManager().getOtherPlayer(this);
            int lowestVal = Integer.MAX_VALUE;

            for (int x = 0; x < board.getWidth(); x++) {
                for (int y = 0; y < board.getHeight(); y++) {
                    BoardPiece piece = board.getBoardPiece(x, y);

                    if (!piece.hasOwner()) {
                        piece.setOwner(opponent);
                        lowestVal = Math.min(lowestVal, miniMax(board, depth - 1, true));
                        piece.clearOwner();
                    }
                }
            }

            return lowestVal;
        }
    }

    /**
     * Evaluate every legal move on the board and return the best one.
     *
     * @param board Board to evaluate
     * @return Coordinates of best move
     */
    private BoardPiece getBestMove(Board board) {
        BoardPiece bestMove = null;
        int bestValue = Integer.MIN_VALUE;

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                BoardPiece piece = board.getBoardPiece(x, y);

                if (!piece.hasOwner()) {
                    piece.setOwner(this);
                    int moveValue = miniMax(board, 6, false);
                    piece.clearOwner();

                    if (moveValue > bestValue) {
                        bestMove = piece;
                        bestValue = moveValue;
                    }
                }
            }
        }
        return bestMove;
    }

    /**
     * Evaluate the given board from the perspective of the X player, return 10 if a
     * winning board configuration is found, -10 for a losing one and 0 for a draw,
     * weight the value of a win/loss/draw according to how many moves it would take
     * to realise it using the depth of the game tree the board configuration is at.
     *
     * @param board     Board to evaluate
     * @param treeDepth depth of the game tree the board configuration is at
     * @return value of the board
     */
    private int evaluateBoard(Board board, int treeDepth) {
        Player winner = board.calculateWinner();

        if (winner == this) {
            // Win for self
            return 10 + treeDepth;
        } else if (winner != null) {
            // Win for other
            return -10 - treeDepth;
        } else {
            // Draw or no win
            return 0;
        }
    }
}
