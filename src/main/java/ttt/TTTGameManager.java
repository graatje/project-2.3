package ttt;

import framework.GameManager;
import ttt.factory.TTTBoardFactory;

public class TTTGameManager extends GameManager {
    public TTTGameManager() {
        super(new TTTBoardFactory());
    }
}
