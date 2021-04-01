package ttt.player;

import framework.board.Board;
import framework.player.AIPlayer;
import framework.player.Player;

import java.util.HashMap;

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

        int[] move = getBestMove(board);
        board.makeMove(board.getCurrentPlayer(), move[0], move[1]);
    }

    public int miniMax(Board board, int depth, boolean isMax) {
        int boardVal = evaluateBoard(board, depth);
        // Terminal node (win/lose/draw) or max depth reached.
        if (Math.abs(boardVal) > 0 || depth == 0 || board.getValidMoves().isEmpty()) {
            return boardVal;
        }

        // Maximising player, find the maximum attainable value.
        if (isMax) {
            int highestVal = Integer.MIN_VALUE;
            for (int row = 0; row < board.getWidth(); row++) {
                for (int col = 0; col < board.getWidth(); col++) {
                    if (!board.getBoardPiece(row, col).hasOwner()) {
                        board.getBoardPiece(row, col).setOwner(this); // temporary set piece
                        highestVal = Math.max(highestVal, miniMax(board, depth - 1, false));
                        board.getBoardPiece(row, col).clearOwner();
                    }
                }
            }
            return highestVal;
            // Minimising player, find the minimum attainable value;
        } else {
            Player opponent = board.getGameManager().getOtherPlayer(this);
            int lowestVal = Integer.MAX_VALUE;
            for (int row = 0; row < board.getWidth(); row++) {
                for (int col = 0; col < board.getWidth(); col++) {
                    if (!board.getBoardPiece(row, col).hasOwner()) {
                        board.getBoardPiece(row, col).setOwner(opponent);
                        lowestVal = Math.min(lowestVal, miniMax(board, depth - 1, true));
                        board.getBoardPiece(row, col).clearOwner();
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
    public int[] getBestMove(Board board) {
        int[] bestMove = new int[]{-1, -1};
        int bestValue = Integer.MIN_VALUE;

        Player opponent = board.getGameManager().getOtherPlayer(this);
        for (int row = 0; row < board.getWidth(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                if (!board.getBoardPiece(row, col).hasOwner()) {
                    board.getBoardPiece(row, col).setOwner(opponent);
                    int moveValue = miniMax(board, 6, false);
                    board.getBoardPiece(row, col).clearOwner();
                    if (moveValue > bestValue) {

                        bestMove[0] = row;
                        bestMove[1] = col;
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
     * @param treedepth depth of the game tree the board configuration is at
     * @return value of the board
     */
    private int evaluateBoard(Board board, int treedepth) {
        int rowSum = 0;
        int opponentPlayerid = board.getCurrentPlayer().getID() == 1 ? 0 : 1;

        HashMap<Integer, Integer> points = new HashMap<Integer, Integer>();
        points.put(board.getCurrentPlayer().getID(), 88);
        points.put(opponentPlayerid, 79);
        int player1win = points.get(board.getCurrentPlayer().getID()) * board.getWidth();

        int player2win = points.get(opponentPlayerid) * board.getWidth(); // amount of points needed in a row to win for
        // player2.

        // from top to bottom
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getWidth(); y++) {
                if (board.getBoardPiece(x, y).hasOwner()) {
                    rowSum += points.get(board.getBoardPiece(x, y).getOwner().getID());
                }
            }
            if (rowSum == player1win) {
                return 10 + treedepth;
            } else if (rowSum == player2win) {
                return -10 - treedepth;
            }
            rowSum = 0;
        }
        rowSum = 0; // just in case
        // from left to right
        for (int y = 0; y < board.getWidth(); y++) {
            for (int x = 0; x < board.getHeight(); x++) {
                if (board.getBoardPiece(x, y).hasOwner()) {
                    rowSum += points.get(board.getBoardPiece(x, y).getOwner().getID());
                }
            }
            if (rowSum == player1win) {
                return 10 + treedepth;
            } else if (rowSum == player2win) {
                return -10 - treedepth;
            }
            rowSum = 0;
        }

        // top left to bottom-right
        for (int i = 0; i < board.getWidth(); i++) {
            if (board.getBoardPiece(i, i).hasOwner()) {
                rowSum += points.get(board.getBoardPiece(i, i).getOwner().getID());
            }
        }
        if (rowSum == player1win) {
            return 10 + treedepth;
        } else if (rowSum == player2win) {
            return -10 - treedepth;
        }

        rowSum = 0;
        // top right to left-bottom
        int indexMax = board.getWidth() - 1 - 1;
        for (int i = 0; i <= indexMax; i++) {
            if (board.getBoardPiece(i, indexMax - i).hasOwner()) {
                rowSum += points.get(board.getBoardPiece(i, indexMax - i).getOwner().getID());
            }
        }
        if (rowSum == player1win) {
            return 10 + treedepth;
        } else if (rowSum == player2win) {
            return -10 - treedepth;
        }
        return 0;
    }
}
