package othello.board;

import framework.GameManager;
import framework.board.Board;
import framework.board.BoardPiece;
import framework.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OthelloBoard extends Board {

    public OthelloBoard(GameManager gameManager) {
        super(gameManager, 8, 8);
    }

    @Override
    public int getMinPlayers() {
        return 2;
    }

    @Override
    public int getMaxPlayers() {
        return 2;
    }

    @Override
    public boolean isShowValidMoves() {
        return true;
    }

    /**
     * get a list of valid moves.
     *
     * @return List<BoardPiece> , a list of valid moves.
     */
    @Override
    public List<BoardPiece> getValidMoves(Player asWho) {
        List<BoardPiece> validMoves = new ArrayList<BoardPiece>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                BoardPiece boardPiece = getBoardPiece(x, y);
                if (!boardPiece.hasOwner()) {
                    if (checkValidMove(boardPiece, asWho)) {
                        validMoves.add(boardPiece);
                    }
                }
            }
        }
        return validMoves;
    }

    /**
     * check if the specified boardpiece is a valid move.
     *
     * @param boardPiece the boardpiece you want to check of if it is a valid move.
     * @return boolean if it is a valid move.
     */
    private boolean checkValidMove(BoardPiece boardPiece, Player asWho) {
        // check
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                if (x == 0 && y == 0) {
                    continue;
                }
                if (checkLine(boardPiece, x, y, asWho)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * checks if you can capture a piece of the opponent.
     *
     * @param piece,  the boardpiece you want to check the line of.
     * @param xchange the horizontal direction the line goes in.
     * @param ychange the vertical direction the line goes in.
     * @return boolean, true if you can capture a piece of the opponent.
     */
    private boolean checkLine(BoardPiece piece, int xchange, int ychange, Player asWho) {
        int x = piece.getX() + xchange;
        int y = piece.getY() + ychange;
        boolean initialized = false;  // if it can be a valid move.
        if (x >= 0 && y >= 0 && x < width && y < height) {  // out of bounds check
            BoardPiece boardPiece = getBoardPiece(x, y);

            // check if it is the opponent.
            if (boardPiece.hasOwner() && boardPiece.getOwner() != asWho) {
                initialized = true;
            }
        }

        while (initialized && x + xchange >= 0 && y + ychange >= 0 && x + xchange < width && y + ychange < height) {  // out of bounds check
            x = x + xchange;
            y = y + ychange;
            BoardPiece boardPiece = getBoardPiece(x, y);
            if (boardPiece.getOwner() == asWho) {  // check if the tile is you.
                return true;
            } else if (!boardPiece.hasOwner()) {
                break;
            }
        }
        return false;
    }

    @Override
    public void _executeMove(Player asWho, BoardPiece piece) {
        if (!checkValidMove(piece, asWho)) {
            return;
        }
        // change a tile in all directions
        for (int y = -1; y <=1; y++) {
            for (int x = -1; x <=1; x++) {
            	if(x==0 && y==0) {
            		continue;
            	}
                changeMoveLine(piece, x, y);
            }
        }
        piece.setOwner(getCurrentPlayer());
    }

    private void changeMoveLine(BoardPiece piece, int xchange, int ychange) {
    	
        // temporary arraylist of captured opponents.
        ArrayList<BoardPiece> templist = new ArrayList<BoardPiece>();
        int x = piece.getX() + xchange;
        int y = piece.getY() + ychange;
        boolean initialized = false;
        if (x >= 0 && y >= 0 && x < width && y < height) {  // out of bounds check
            BoardPiece boardPiece = getBoardPiece(x, y);
            if (boardPiece.hasOwner() && boardPiece.getOwner() != getCurrentPlayer()) {
                initialized = true;
            }
        }
        // add the current boardPiece to captured opponents
        if (initialized) {
            templist.add(getBoardPiece(x, y));
        }
        boolean brokeOut = false;
        while (initialized && x + xchange >= 0 && y + ychange >= 0 && x + xchange < width && y + ychange < height) {
            x = x + xchange;
            y = y + ychange;
            BoardPiece boardPiece = getBoardPiece(x, y);
            if (!boardPiece.hasOwner()) {
                return;
            }else if(boardPiece.getOwner().getID() == getCurrentPlayer().getID())
            {
            	brokeOut = true;
            	break;
            }
            
            else {  // opponent
                templist.add(getBoardPiece(x, y));
            }
        }
        if(brokeOut) {
	        for (BoardPiece boardPiece : templist) {
	            boardPiece.setOwner(getCurrentPlayer());
	        }
        }
    }

    /**
     * a check for if the game is over.
     *
     * @return boolean true if game is over, false if not.
     */
    @Override
    public boolean calculateIsGameOver() {
    	return getValidMoves().isEmpty() &&
				getValidMoves(gameManager.getOtherPlayer(getCurrentPlayer())).isEmpty();
    }

    /**
     * check what player has won the game. null if draw.
     *
     * @return Player the player who won, or null if draw.
     */
    @Override
    public Player calculateWinner() {
        if (calculateIsGameOver()) {
            int player1count = 0;
            int player2count = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    BoardPiece tempBoardPiece = getBoardPiece(x, y);
                    if (!tempBoardPiece.hasOwner()) {
                        continue;
                    }
                    if (tempBoardPiece.getOwner().getID() == 0) {
                        player1count++;
                    } else {
                        player2count++;
                    }
                }
            }
            if (player1count > player2count) {  // player 1 won
                return gameManager.getPlayer(0);
            } else if (player2count > player1count) {  // player 2 won
                return gameManager.getPlayer(1);
            }
        }
        return null; // no winner (draw)
    }

	@Override
	public void prepareBoard(Player startPlayer) {
		getBoardPiece(3,3).setOwner(gameManager.getOtherPlayer(startPlayer));
		getBoardPiece(4,4).setOwner(gameManager.getOtherPlayer(startPlayer));
		getBoardPiece(3,4).setOwner(startPlayer);
		getBoardPiece(4,3).setOwner(startPlayer);
	}
}
