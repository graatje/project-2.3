package framework.board;

import framework.player.Player;

public class BoardPiece {
    private final int x, y;
    private Player owner = null;

    /**
     * Constructs a BoardPiece without an owner.
     *
     * @param x The X-coordinate of this piece.
     * @param y The X-coordinate of this piece.
     */
    public BoardPiece(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a BoardPiece with an owner.
     *
     * @param x The X-coordinate of this piece.
     * @param y The X-coordinate of this piece.
     * @param owner The current owner of this piece.
     */
    public BoardPiece(int x, int y, Player owner) {
        this.x = x;
        this.y = y;
        this.owner = owner;
    }

    /**
     * @return The X-coordinate of this piece.
     */
    public int getX() {
        return x;
    }

    /**
     * @return The Y-coordinate of this piece.
     */
    public int getY() {
        return y;
    }

    /**
     * @return The current owner or <code>null</code> if there is no owner.
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * @return Whether this piece has an owner or not.
     */
    public boolean hasOwner() {
        return owner != null;
    }

    /**
     * @param owner The player to change the owner to.
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    /**
     * Clears the owner of this piece. Equivalent to <code>boardPiece.setOwner(null)</code>
     */
    public void clearOwner() {
        this.owner = null;
    }
}
