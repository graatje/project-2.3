package ttt;

import Connection.Connection;
import framework.GameManager;
import ttt.factory.TTTBoardFactory;

public class TTTGameManager extends GameManager {
    public TTTGameManager(Connection connection) {
        super(connection, new TTTBoardFactory());
    }
}
