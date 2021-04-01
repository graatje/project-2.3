package framework.factory;

import framework.board.Board;
import framework.player.Player;

public interface PlayerFactory {
    /**
     * Creates a Player.
     */
    Player createPlayer(Board board, String name);
}
