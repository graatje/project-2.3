package ttt;

import Connection.Connection;
import framework.GameManager;
import framework.factory.PlayerFactory;
import ttt.factory.TTTBoardFactory;

public class TTTGameManager extends GameManager {
    public TTTGameManager(Connection connection, PlayerFactory selfPlayerFactory) {
        super(connection, new TTTBoardFactory(), selfPlayerFactory);
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

