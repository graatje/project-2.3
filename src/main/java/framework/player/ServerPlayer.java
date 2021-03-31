package framework.player;

import Connection.CommunicationListener;
import framework.board.Board;

public class ServerPlayer extends Player implements CommunicationListener {
    public ServerPlayer(Board board) {
        super(board);

        board.getGameManager().getConnection().getClient()
                .getCommunicationHandler().setComListener(this);
    }

    @Override
    public void startMatch(String opponent, String playerToBegin) {
        //
    }

    @Override
    public void ourTurn() {
        board.requestPlayerMove();
    }

    @Override
    public void opponentTurn(String move) {

    }

    @Override
    public void getMatchRequest(String opponent, String gametype, String challengeNR) {

    }

    @Override
    public void matchCancelled(String challengeNR) {

    }
}
