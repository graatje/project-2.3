package ttt.player;

import java.util.List;
import java.util.Random;

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

       // BoardPiece bestMove = getBestMove(board);
        board.makeMove(board.getCurrentPlayer(), getEasyMove(board));
    }

    /**
     * this picks a random move from the valid moves.
     * @param board, the playing board.
     * @return BoardPiece, a random BoardPiece from valid moves.
     */
    public BoardPiece getEasyMove(Board board){
    	List<BoardPiece> validMoves = board.getValidMoves();
    	Random rand = new Random();
    	return validMoves.get(rand.nextInt(validMoves.size()));
    }
    
    /**
     * this has a 50/50 chance of picking the best move and a move that is
     * a random move that is not the best move.
     * @param board, the playing board.
     * @return BoardPiece
     */
    public BoardPiece getMediumMove(Board board)
    {
    	BoardPiece bestMove = getBestMove(board);
    	List<BoardPiece> validMoves = board.getValidMoves();
    	Random rand = new Random();
    	if(rand.nextInt(100) >50){
    		// best move
    		return bestMove;
    	}
    	else { // random move that is not the best move.
    		if(validMoves.size() > 1){  // prevent getting empty list of moves.
    			validMoves.remove(validMoves.indexOf(bestMove));
    		}
    		return validMoves.get(rand.nextInt(validMoves.size()));
    	}
    }
    
    /**
     * returns the highest value move when the end is reached because either a lack of valid moves, 
     * the end of a node or the maximum search depth is reached.
     * @param board a playing board.
     * @param depth , depth of the nodes to look into.
     * @param Boolean isMax , mini(malising) or maxi(malising)
     * @return int value of the board.
     */
    private int miniMax(Board board, int depth, boolean isMax) {
        int boardVal = evaluateBoard(board, depth);

        if (Math.abs(boardVal) > 0 || depth == 0 || board.getValidMoves().isEmpty()) {
            // end reached.
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
                        // see if miniMax with the adjusted board is higher than the current highest val.
                        highestVal = Math.max(highestVal, miniMax(board, depth - 1, false));  
                        piece.clearOwner();
                    }
                }
            }

            return highestVal;
        } else {  // isMin
            // Minimising player, find the minimum attainable value;
            Player opponent = board.getGameManager().getOtherPlayer(this);
            int lowestVal = Integer.MAX_VALUE;

            for (int x = 0; x < board.getWidth(); x++) {
                for (int y = 0; y < board.getHeight(); y++) {
                    BoardPiece piece = board.getBoardPiece(x, y);

                    if (!piece.hasOwner()) {
                        piece.setOwner(opponent); // temporary set piece
                        // see if miniMax with the adjusted board is lower than the current highest val.
                        lowestVal = Math.min(lowestVal, miniMax(board, depth - 1, true));
                        piece.clearOwner();  // setting the piece to its previous state
                    }
                }
            }

            return lowestVal;
        }
    }

    /**
     * check all valid moves. return the best move of those.
     *
     * @param board Board, the board to check.
     * @return the boardpiece with the best move.
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
     * Evaluate the given board from the perspective of the current player, return 10 if a
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
