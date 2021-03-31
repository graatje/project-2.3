package ttt.factory;

import framework.GameManager;
import framework.factory.BoardFactory;
import ttt.board.TTTBoard;

public class TTTBoardFactory implements BoardFactory {
    @Override
    public TTTBoard createBoard(GameManager gameManager) {
        return new TTTBoard(gameManager, 3, 3);
    }
}
