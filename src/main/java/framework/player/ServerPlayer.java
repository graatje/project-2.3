package framework.player;

import Connection.ServerPlayerCommunicationListener;
import framework.board.Board;

/**
 * This class is a subclass of Player and stores a ServerPlayer.
 */
public class ServerPlayer extends Player implements ServerPlayerCommunicationListener {
    /**
     * constructor, calls constructor of superclass.
     *
     * @param board
     */
    public ServerPlayer(Board board, String name) {
        super(board, name);

        if (board.getGameManager().hasConnection()) {
            board.getGameManager().getConnection().getClient().getCommunicationHandler().setServerPlayerCommunicationListener(this);
        }
    }

    @Override
    public void opponentTurn(String move) {
        // TODO: MOVE WITH MOVE 'move'!
        // Call Board#makeRawMove because this splits making the move and finalizing the move in two.
        // See the finalizeTurn method below.
    }

    @Override
    public void endMatch(String result) {
        result = result.strip();

        switch (result) {
            case "WIN":
                board.forceWin(this);
                break;
            case "LOSS":
                board.forceWin(board.getGameManager().getOtherPlayer(this));
                break;
            case "DRAW":
                board.forceWin(null);
                break;
        }
    }

    @Override
    public void finalizeTurn() {
        board.finalizeRawMove();
    }
}
