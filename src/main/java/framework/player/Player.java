package framework.player;

import framework.board.Board;

public abstract class Player {
    protected final Board board;

    public Player(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }
}
