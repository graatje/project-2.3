package framework.board;

import framework.player.Player;

public interface BoardObserver {
    /**
     * A handler for when a player moves (aka when the board state gets updated).
     */
    void onPlayerMoved(Player who, BoardPiece where);

    /**
     * A handler for when a player move finalizes
     */
    void onPlayerMoveFinalized(Player previous, Player current);

    /**
     * A handler for when a player wins
     */
    void onPlayerWon(Player who);

    /**
     * A handler for when the game starts
     */
    void onGameStart(Player startingPlayer);
}
