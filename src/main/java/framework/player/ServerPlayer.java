package framework.player;

import Connection.ServerPlayerListener;
import framework.board.Board;

public class ServerPlayer extends Player implements ServerPlayerListener {
    public ServerPlayer(Board board) {
        super(board);

        board.getGameManager().getConnection().getClient()
                .getCommunicationHandler().setServerPlayerListener(this);
    }

    @Override
    public void opponentTurn(String move) {

    }
}
