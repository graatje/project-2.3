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
        List<BoardPiece> validMoves = board.getValidMoves(this);
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

        for(BoardPiece boardPiece : board.getValidMoves(this)) {
            int x = boardPiece.getX();
            int y = boardPiece.getY();

            Board clonedBoard;
            try{
                clonedBoard = board.clone();
            }catch(CloneNotSupportedException e) {
                e.printStackTrace();
                return null;
            }

            clonedBoard._executeMove(this, clonedBoard.getBoardPiece(x, y));
            int moveValue = miniMax(clonedBoard, 6, false);

            if (moveValue > bestValue) {
                bestMove = boardPiece;
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
        int boardVal = evaluateBoard(board, depth);

        Player playerToMove = isMax ? this : board.getGameManager().getOtherPlayer(this);

        if (Math.abs(boardVal) > 0 || depth == 0 || board.getValidMoves(playerToMove).isEmpty()) {
            // end reached.
            return boardVal;
        }

        int extremeVal = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for(BoardPiece boardPiece : board.getValidMoves(playerToMove)) {
            int x = boardPiece.getX();
            int y = boardPiece.getY();

            Board clonedBoard;
            try{
                clonedBoard = board.clone();
            }catch(CloneNotSupportedException e) {
                e.printStackTrace();
                return 0;
            }

            clonedBoard._executeMove(playerToMove, clonedBoard.getBoardPiece(x, y));
            int val = miniMax(clonedBoard, depth - 1, !isMax);

            if(isMax) {
                if(val > extremeVal) extremeVal = val;
            }else{
                if(val < extremeVal) extremeVal = val;
            }
        }

        return extremeVal;
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
