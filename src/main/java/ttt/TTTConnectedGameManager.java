package ttt;

import framework.ConnectedGameManager;
import framework.factory.PlayerFactory;
import ttt.factory.TTTBoardFactory;

import java.io.IOException;

public class TTTConnectedGameManager extends ConnectedGameManager {
    public TTTConnectedGameManager(String serverIP, int serverPort, PlayerFactory selfPlayerFactory) throws IOException {
        super(serverIP, serverPort, new TTTBoardFactory(), selfPlayerFactory);
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

