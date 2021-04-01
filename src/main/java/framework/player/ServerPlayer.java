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
        try{
            int intMove = Integer.parseInt(move);

            int x = intMove % getBoard().getWidth();
            int y = intMove / getBoard().getWidth();

            board.makeRawMove(this, x, y );
        }catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
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
