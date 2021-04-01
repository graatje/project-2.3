package framework.player;

import Connection.ServerPlayerCommunicationListener;
import framework.ConnectedGameManager;
import framework.board.Board;
import framework.board.BoardObserver;
import framework.board.BoardPiece;

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
    }

    @Override
    public void turnReceive(String whoPlayer, String move) {
        if(whoPlayer.equals(getName())) {
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
    }

    @Override
    public void finalizeTurn() {
        board.finalizeRawMove();
    }
}
