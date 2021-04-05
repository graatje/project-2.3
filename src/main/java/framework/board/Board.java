package framework.board;

import framework.GameManager;
import framework.player.MoveRequestable;
import framework.player.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Board {
    private final GameManager gameManager;
    protected final int width, height;
    protected final BoardPiece[] pieces;

    private int currentPlayerId;
    private boolean isGameOver = false;
    private Player winner;

    private final Set<BoardObserver> observers = new HashSet<>();

    /**
     * Constructs a new Board
     *
     * @param gameManager The GameManager which owns this board.
     * @param width       The width (in tiles) of this board.
     * @param height      The height (in tiles) of this board.
     */
    public Board(GameManager gameManager, int width, int height) {
        this.gameManager = gameManager;
        this.width = width;
        this.height = height;

        this.pieces = new BoardPiece[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pieces[x + y * width] = new BoardPiece(x, y);
            }
        }
    }

    /**
     * An implementation-specific getter for calculating all valid moves.
     *
     * @return All valid moves.
     */
    public abstract List<BoardPiece> getValidMoves();

    /**
     * An implementation-specific method for executing a move on the board.
     * No argument-checking needs to be done in this method, because it is protected and only gets called internally from the super-class Board.
     *
     * @param player The player which executed the move.
     * @param piece  The piece the player wants to affect.
     */
    protected abstract void executeMove(Player player, BoardPiece piece);

    /**
     * An implementation-specific method for calculating if the game is currently over.
     *
     * @return Whether the game is currently over or not.
     */
    protected abstract boolean calculateIsGameOver();

    /**
     * An implementation-specific method for calculating the winner of the game.
     *
     * @return The winner of the game, or <code>null</code> if the game ended in a draw.
     */
    public abstract Player calculateWinner();


    /**
     * @return The width of the board in tiles
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return The height of the board in tiles
     */
    public int getHeight() {
        return height;
    }

    /**
     * Executes a RAW MOVE on the board. This does NOT finalize the raw turn. The caller of this method
     * should finalize their own turn by executing {@link Board#finalizeRawMove()}.
     *
     * @param player The player which executed the move.
     * @param piece  The piece the player wants to affect.
     */
    public void makeRawMove(Player player, BoardPiece piece) {
        if (getCurrentPlayer() != player) {
            throw new IllegalArgumentException("It's not that player's turn yet!");
        }

        if (!getValidMoves().contains(piece)) {
            throw new IllegalArgumentException("That is not a valid move!");
        }

        // All good, now actually execute the move on the board!
        executeMove(player, piece);

        // Make sure all observers know of this state-change!
        observers.forEach(o -> o.onPlayerMoved(player, piece));
    }

    /**
     * See {@link Board#makeRawMove(Player, BoardPiece)}
     */
    public void makeRawMove(Player player, int x, int y) {
        makeRawMove(player, getBoardPiece(x, y));
    }

    /**
     * Finalizes the current raw move by:
     * 1. Updating the current player
     * 2. Calculating whether the game is over or not
     * 3. Requesting a move from the new current player if the game is not over
     */
    public void finalizeRawMove() {
        if (isGameOver) {
            return;
        }

        Player previousPlayer = getCurrentPlayer();

        // Update currentPlayerId because it's now the next player's turn.
        currentPlayerId++;
        if (currentPlayerId >= gameManager.getPlayers().size()) {
            currentPlayerId = 0;
        }

        // See if the game is over after this move
        if (calculateIsGameOver()) {
            // The game is over! Calculate a winner and set the flag!

            Player winner = calculateWinner();
            forceWin(winner);
        }

        // Make sure all observers know of this state-change!
        observers.forEach(o -> o.onPlayerMoveFinalized(previousPlayer, getCurrentPlayer()));

        if (!isGameOver) {
            // The game is not yet over. Request the next move from the new current player.
            requestPlayerMove();
        }
    }

    /**
     * This method attempts to execute a move from a given player, and immediately finalizes the move.
     * See {@link Board#makeRawMove(Player, BoardPiece)}
     * See {@link Board#finalizeRawMove()}
     *
     * @param player The player which executed the move.
     * @param piece  The piece the player wants to affect.
     */
    public void makeMove(Player player, BoardPiece piece) {
        // Make the raw turn, and finalize it immediately!
        makeRawMove(player, piece);
        finalizeRawMove();
    }

    /**
     * See {@link Board#makeMove(Player, BoardPiece)}
     */
    public void makeMove(Player player, int x, int y) {
        makeMove(player, getBoardPiece(x, y));
    }

    /**
     * Calls {@link MoveRequestable#requestMove()} on the current player, if the current player implements {@link MoveRequestable}
     */
    public void requestPlayerMove() {
        Player currentPlayer = gameManager.getPlayer(currentPlayerId);
        if (currentPlayer instanceof MoveRequestable) {
            ((MoveRequestable) currentPlayer).requestMove();
        }
    }

    /**
     * Forces a win for a specific player.
     *
     * @param winner The player who should be considered the winner, or <code>null</code> to indicate a draw.
     */
    public void forceWin(Player winner) {
        this.isGameOver = true;
        this.winner = winner;

        new ArrayList<>(observers).forEach(o -> o.onPlayerWon(winner));
    }

    /**
     * Gets the board piece on specific coordinates.
     *
     * @param x The X-coordinate
     * @param y The Y-coordinate
     * @return The board piece on the specified coordinates.
     */
    public BoardPiece getBoardPiece(int x, int y) {
        return pieces[x + y * width];
    }

    /**
     * Gets the board piece neighbors (including diagonals) of specific coordinates, excluding the piece on the coordinates itself.
     *
     * @param centerX The X-coordinate.
     * @param centerY The Y-coordinate.
     * @return The neighbors of (centerX, centerY) including diagonals, excluding itself.
     */
    public List<BoardPiece> getBoardPieceNeighbors(int centerX, int centerY) {
        List<BoardPiece> result = new ArrayList<>();

        for (int y = centerY - 1; y <= centerY + 1; y++) {
            if (y < 0 || y >= height) continue; // Make sure we don't get out of bounds

            for (int x = centerX - 1; x <= centerX + 1; x++) {
                if (x < 0 || x >= width) continue; // Make sure we don't get out of bounds
                if (x == 0 && y == 0) continue;

                result.add(getBoardPiece(x, y));
            }
        }

        return result;
    }

    /**
     * @return The GameManager which owns this board.
     */
    public GameManager getGameManager() {
        return gameManager;
    }

    /**
     * @return The player who is currently expected to make a move.
     */
    public Player getCurrentPlayer() {
        return gameManager.getPlayer(currentPlayerId);
    }

    public int getCurrentPlayerID() {
        return currentPlayerId;
    }

    public void setCurrentPlayerID(int startingPlayer) {
        currentPlayerId = startingPlayer;
    }

    /**
     * @return Whether the game is over or not.
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * @return The winner of the game, or <code>null</code> if the game ended in a draw.
     * @throws IllegalStateException when the game is not over yet.
     */
    public Player getWinner() {
        if (!isGameOver) {
            throw new IllegalStateException("The game isn't over yet!");
        }

        return winner;
    }

    /**
     * Registers a BoardObserver.
     *
     * @param observer The BoardObserver to register.
     */
    public void registerObserver(BoardObserver observer) {
        observers.add(observer);
    }

    /**
     * Unregisters a BoardObserver.
     *
     * @param observer The BoardObserver to unregister.
     */
    public void unregisterObserver(BoardObserver observer) {
        observers.remove(observer);
    }
}
