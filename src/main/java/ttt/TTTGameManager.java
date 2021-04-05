package ttt;

import framework.GameManager;
import ttt.factory.TTTBoardFactory;

public class TTTGameManager extends GameManager {
    public TTTGameManager() {
        super(new TTTBoardFactory());
    }

    @Override
    public int getMinPlayers() {
        return 2;
    }

    @Override
    public int getMaxPlayers() {
        return 2;
    }
}
