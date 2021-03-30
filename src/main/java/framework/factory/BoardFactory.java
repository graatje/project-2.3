package framework.factory;

import framework.GameManager;
import framework.board.Board;

public interface BoardFactory {
    Board createBoard(GameManager gameManager);
}
