package framework.player;

import Connection.ServerPlayerCommunicationListener;
import framework.board.Board;
import framework.board.BoardObserver;
import framework.board.BoardPiece;

/**
 * This class is a subclass of Player and stores a ServerPlayer.
 */
public class ServerPlayer extends Player implements ServerPlayerCommunicationListener, BoardObserver {
    /**
     * constructor, calls constructor of superclass.
     *
     * @param board
     */
    public ServerPlayer(Board board, String name) {
        super(board, name);

        board.getGameManager().getConnection().getClient().getCommunicationHandler().setServerPlayerCommunicationListener(this);
        board.registerObserver(this);
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

    @Override
    public void onPlayerMoved(Player who, BoardPiece where) {
        if(who != this) {
            // TODO: NOTIFY SERVER OF PLAYER MOVE BY US!
        }
    }

    @Override
    public void onPlayerMoveFinalized(Player previous, Player current) { /* Do nothing */ }

    @Override
    public void onPlayerWon(Player who) {
        // We're done! Unregister ourselves as a board observer and a server listener!
        board.getGameManager().getConnection().getClient().getCommunicationHandler().setServerPlayerCommunicationListener(null);
        board.unregisterObserver(this);
    }
}
