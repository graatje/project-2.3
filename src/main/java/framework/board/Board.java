package framework.board;

import framework.GameManager;
import framework.board.piece.BoardPiece;
import framework.player.MoveRequestable;
import framework.player.Player;

import java.util.*;

public abstract class Board {
    private final GameManager gameManager;
    protected final int width, height;
    protected final BoardPiece[] pieces;

    private int currentPlayerId;
    private boolean isGameOver = false;
    private Player winner;

    private final Set<BoardObserver> observers = new HashSet<>();

    public Board(GameManager gameManager, int width, int height) {
        this.gameManager = gameManager;
        this.width = width;
        this.height = height;

        this.pieces = new BoardPiece[width * height];
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                pieces[x + y * width] = new BoardPiece(x, y);
            }
        }
    }

    public abstract List<BoardPiece> getValidMoves();

    protected abstract void executeMove(Player player, BoardPiece piece);

    protected abstract boolean calculateIsGameOver();

    protected abstract Player calculateWinner();


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void makeMove(Player player, BoardPiece piece) {
        if(getCurrentPlayer() != player) {
            throw new IllegalArgumentException("It's not that player's turn yet!");
        }

        if(!getValidMoves().contains(piece)) {
            throw new IllegalArgumentException("That is not a valid move!");
        }

        // All good, now actually execute the move on the board!
        executeMove(player, piece);

        // Update currentPlayerId because it's now the next player's turn.
        currentPlayerId++;
        if(currentPlayerId >= gameManager.getPlayers().length) {
            currentPlayerId = 0;
        }

        // See if the game is over after this move
        if(calculateIsGameOver()) {
            // The game is over! Calculate a winner and set the flag!

            isGameOver = true;
            winner = calculateWinner();
        }

        // Make sure all observers know of this state-change!
        notifyObservers();

        if(!isGameOver) {
            // The game is not yet over. Request the next move from the new current player.
            requestPlayerMove();
        }
    }

    public void makeMove(Player player, int x, int y) {
        makeMove(player, getBoardPiece(x, y));
    }

    public void requestPlayerMove() {
        Player currentPlayer = gameManager.getPlayer(currentPlayerId);
        if(currentPlayer instanceof MoveRequestable) {
            ((MoveRequestable) currentPlayer).requestMove();
        }
    }

    public void forceWin(Player winner) {
        this.isGameOver = true;
        this.winner = winner;

        notifyObservers();
    }

    public BoardPiece getBoardPiece(int x, int y) {
        return pieces[x + y * width];
    }

    public List<BoardPiece> getBoardPieceNeighbors(int centerX, int centerY) {
        List<BoardPiece> result = new ArrayList<>();

        for(int y = centerY - 1; y <= centerY + 1; y++) {
            if(y < 0 || y >= height) continue; // Make sure we don't get out of bounds

            for(int x = centerX - 1; x <= centerX + 1; x++) {
                if(x < 0 || x >= width) continue; // Make sure we don't get out of bounds
                if(x == 0 && y == 0) continue;

                result.add(getBoardPiece(x, y));
            }
        }

        return result;
    }

    public Player getCurrentPlayer() {
        return gameManager.getPlayer(currentPlayerId);
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public Player getWinner() {
        if(!isGameOver) {
            throw new IllegalStateException("The game isn't over yet!");
        }

        return winner;
    }

    public void registerObserver(BoardObserver observer) {
        observers.add(observer);
    }

    public void unregisterObserver(BoardObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        observers.forEach(BoardObserver::boardUpdated);
    }
}
