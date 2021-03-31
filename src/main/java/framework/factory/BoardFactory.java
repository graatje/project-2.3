package framework.factory;

import framework.GameManager;
import framework.board.Board;

public interface BoardFactory {
    /**
     * Creates a board.
     */
    Board createBoard(GameManager gameManager);
}
