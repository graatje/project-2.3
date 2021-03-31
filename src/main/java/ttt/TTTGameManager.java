package ttt;

import Connection.Connection;
import framework.GameManager;
import ttt.factory.TTTBoardFactory;

public class TTTGameManager extends GameManager {
    public TTTGameManager(Connection connection) {
        super(connection, new TTTBoardFactory());
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
    public void startMatch(String opponent, String playerToBegin) {

    }

    @Override
    public void getMatchRequest(String opponent, String gametype, String challengeNR) {

    }

    @Override
    public void matchCancelled(String challengeNR) {

    }

    @Override
    public void ourTurn() {

    }
}
