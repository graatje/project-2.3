package framework.player;

import Connection.CommunicationListener;
import framework.board.Board;

public class ServerPlayer extends Player implements CommunicationListener {
    public ServerPlayer(Board board) {
        super(board);

        board.getGameManager().getConnection().getClient()
                .getCommunicationHandler().setCurrentCommunicationListener(this);
    }

    @Override
    public void startMatch() {
        //
    }

    @Override
    public void yourTurn() {
        board.requestPlayerMove();
    }
}
