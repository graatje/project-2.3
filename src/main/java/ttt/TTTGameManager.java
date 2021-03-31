package ttt;

import Connection.Connection;
import framework.GameManager;
import ttt.factory.TTTAIPlayerFactory;
import ttt.factory.TTTBoardFactory;

public class TTTGameManager extends GameManager {
    public TTTGameManager(Connection connection) {
        super(connection, new TTTBoardFactory(), new TTTAIPlayerFactory());
    }

    @Override
    public int getMinPlayers() {
        return 2;
    }

    @Override
    public int getMaxPlayers() {
        return 2;
    }

    @Override
    public void ourTurn() {

    }
}

