package framework.player;

import framework.board.Board;
import framework.board.BoardPiece;

import java.util.List;

public class MinimaxAIPlayer extends AIPlayer {
    private int depth;
    private AIDifficulty difficulty;

    public MinimaxAIPlayer(Board board, String name, int depth, AIDifficulty difficulty) {
        super(board, name);

        this.depth = depth;
        this.difficulty = difficulty;
    }

    public MinimaxAIPlayer(Board board, int depth, AIDifficulty difficulty) {
        super(board);

        this.depth = depth;
        this.difficulty = difficulty;
    }

    @Override
    public void requestMove() {
        BoardPiece move;
        switch(difficulty) {
            case EASY:
                move = getRandomMove();
                break;
            case MEDIUM:
                move = Math.random() > 0.5 ? getRandomMove() : getMinimaxMove();
                break;
            case HARD:
                move = getMinimaxMove();
                break;
            default:
                throw new IllegalStateException("Invalid AI difficulty '" + difficulty + "'!");
        }

        board.makeMove(this, move);
    }

    public BoardPiece getRandomMove() {
        List<BoardPiece> validMoves = board.getValidMoves();
        if(validMoves.isEmpty()) {
            return null;
        }

        return validMoves.get((int) (Math.random() * validMoves.size()));
    }

    /**
     * check all valid moves. return the best move of those.
     *
     * @return the boardpiece with the best move.
     */
    public BoardPiece getMinimaxMove() {
        BoardPiece bestMove = null;
        int bestValue = Integer.MIN_VALUE;

        for(BoardPiece piece : board.getValidMoves()) {
            piece.setOwner(this);
            int moveValue = miniMax(board, 6, false);
            piece.clearOwner();

            if (moveValue > bestValue) {
                bestMove = piece;
                bestValue = moveValue;
            }
        }

        return bestMove;
    }

    /**
     * returns the highest value move when the end is reached because either a lack of valid moves,
     * the end of a node or the maximum search depth is reached.
     *
     * @param board a playing board.
     * @param depth depth of the nodes to look into.
     * @param isMax mini(malising) or maxi(malising)
     * @return int value of the board.
     */
    private int miniMax(Board board, int depth, boolean isMax) {
        int boardVal = evaluateBoard(depth);

        if (Math.abs(boardVal) > 0 || depth == 0 || board.getValidMoves().isEmpty()) {
            // end reached.
            return boardVal;
        }

        if (isMax) {
            // Maximising player, find the maximum attainable value.
            int highestVal = Integer.MIN_VALUE;

            for(BoardPiece piece : board.getValidMoves()) {
                piece.setOwner(this); // temporarily set piece
                int val = miniMax(board, depth - 1, false);
                piece.clearOwner();

                // see if miniMax with the adjusted board is higher than the current highest val.
                if(val > highestVal) {
                    highestVal = val;
                }
            }

            return highestVal;
        } else {  // isMin
            // Minimising player, find the minimum attainable value;
            Player opponent = board.getGameManager().getOtherPlayer(this);
            int lowestVal = Integer.MAX_VALUE;

            for(BoardPiece piece : board.getValidMoves()) {
                piece.setOwner(opponent); // temporary set piece
                int val = miniMax(board, depth - 1, true);
                piece.clearOwner();  // setting the piece to its previous state

                // see if miniMax with the adjusted board is lower than the current highest val.
                if(val < lowestVal) {
                    lowestVal = val;
                }
            }

            return lowestVal;
        }
    }

    /**
     * Evaluate the given board from the perspective of the current player, return 10 if a
     * winning board configuration is found, -10 for a losing one and 0 for a draw,
     * weight the value of a win/loss/draw according to how many moves it would take
     * to realise it using the depth of the game tree the board configuration is at.
     *
     * @param treeDepth depth of the game tree the board configuration is at
     * @return value of the board
     */
    private int evaluateBoard(int treeDepth) {
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

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public AIDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(AIDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public enum AIDifficulty {
        EASY,
        MEDIUM,
        HARD,
    }
}
