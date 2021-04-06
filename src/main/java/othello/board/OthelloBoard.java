package othello.board;

import framework.GameManager;
import framework.board.Board;
import framework.board.BoardPiece;
import framework.player.Player;

import java.util.ArrayList;
import java.util.List;

public class OthelloBoard extends Board {

    public OthelloBoard(GameManager gameManager) {
        super(gameManager, 8, 8);
    }

    /**
     * get a list of valid moves.
     *
     * @return List<BoardPiece> , a list of valid moves.
     */

    public List<BoardPiece> getValidMoves(Player currentPlayer) {
        List<BoardPiece> validMoves = new ArrayList<BoardPiece>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; y < width; x++) {
                BoardPiece boardPiece = getBoardPiece(x, y);
                if (!boardPiece.hasOwner()) {
                    if (checkValidMove(boardPiece, currentPlayer)) {
                        validMoves.add(boardPiece);
                    }
                }
            }
        }
        return validMoves;
    }

    @Override
    public List<BoardPiece> getValidMoves() {
        return getValidMoves(getCurrentPlayer());
    }

    /**
     * check if the specified boardpiece is a valid move.
     *
     * @param boardPiece the boardpiece you want to check of if it is a valid move.
     * @return boolean if it is a valid move.
     */
    private boolean checkValidMove(BoardPiece boardPiece, Player currentPlayer) {
        // check
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                if (x == 0 && y == 0) {
                    continue;
                }
                if (checkLine(boardPiece, x, y, currentPlayer)) {
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
    private boolean checkLine(BoardPiece piece, int xchange, int ychange, Player currentPlayer) {
        int x = piece.getX() + xchange;
        int y = piece.getY() + ychange;
        boolean initialized = false;  // if it can be a valid move.
        if (x >= 0 && y >= 0 && x < width && y < height) {  // out of bounds check
            BoardPiece boardPiece = getBoardPiece(x, y);

            // check if it is the opponent.
            if (boardPiece.hasOwner() && boardPiece.getOwner() != currentPlayer) {
                initialized = true;
            }
        }

        while (initialized && x + xchange >= 0 && y + ychange >= 0 && x + xchange < width && y + ychange < height) {  // out of bounds check
            x = x + xchange;
            y = y + xchange;
            BoardPiece boardPiece = getBoardPiece(x, y);
            if (boardPiece.getOwner() == currentPlayer) {  // check if the tile is you.
                return true;
            } else if (!boardPiece.hasOwner()) {
                break;
            }
        }
        return false;
    }

    /**
     *
     */
    @Override
    public int getMinPlayers() {
        return 2;
    }


    @Override
    public int getMaxPlayers() {
        return 2;
    }

    /**
     *
     */
    @Override
    protected void executeMove(Player player, BoardPiece piece) {
        if (!checkValidMove(piece, player)) {
            return;
        }
        // change a tile in all directions
        for (int y = 0; y < height; y++) {
            for (int x = 0; y < width; x++) {
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

        while (initialized && x + xchange >= 0 && y + ychange >= 0 && x + xchange < width && y + ychange < height) {
            x = x + xchange;
            y = y + xchange;
            BoardPiece boardPiece = getBoardPiece(x, y);
            if (!boardPiece.hasOwner() || boardPiece.getOwner().getID() == getCurrentPlayer().getID()) {
                break;
            } else {  // opponent
                templist.add(getBoardPiece(x, y));
            }
        }
        for (BoardPiece boardPiece : templist) {
            boardPiece.setOwner(getCurrentPlayer());
        }
    }

    /**
     * a check for if the game is over.
     *
     * @return boolean true if game is over, false if not.
     */
    @Override
    protected boolean calculateIsGameOver() {
        if (getValidMoves().isEmpty()) {
            Player originalPlayer = getCurrentPlayer();

            // peek if the opponent has no valid moves either.
            Player opponent = gameManager.getOtherPlayer(getCurrentPlayer());

            if (getValidMoves(opponent).isEmpty()) {
                return true;  // game is over.
            }
        }
        return false;
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
}
