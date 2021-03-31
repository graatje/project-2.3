package framework.player;

import Connection.ServerPlayerListener;
import framework.board.Board;

/**
 * This class is a subclass of Player and stores a ServerPlayer.
 */
public class ServerPlayer extends Player implements ServerPlayerListener {
    /**
     * constructor, calls constructor of superclass.
     *
     * @param board
     */
    public ServerPlayer(Board board, String name) {
        super(board, name);

        if (board.getGameManager().hasConnection()) {
            board.getGameManager().getConnection().getClient().getCommunicationHandler().setServerPlayerListener(this);
        }
    }

    @Override
    public void opponentTurn(String move) {

    }

    @Override
    public void endMatch(String result) {
        result = result.strip();

        switch (result) {
            case "WIN":
                board.forceWin(board.getGameManager().getPlayer(getID()));
                break;
            case "LOSS":
                board.forceWin(board.getGameManager().getOtherPlayer(this));
                break;
            case "DRAW":
                board.forceWin(null);
                break;
        }
    }
}
