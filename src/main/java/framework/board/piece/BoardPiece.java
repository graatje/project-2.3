package framework.board.piece;

import framework.player.Player;

public class BoardPiece {
    private final int x, y;
    private Player owner = null;

    public BoardPiece(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Player getOwner() {
        return owner;
    }

    public boolean hasOwner() {
        return owner != null;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public void clearOwner() {
        this.owner = null;
    }
}
