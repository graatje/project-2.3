package framework.player;

import connection.ServerPlayerCommunicationListener;
import framework.board.Board;

/**
 * This class is a subclass of Player and stores a ServerPlayer.
 */
public class ServerPlayer extends Player implements ServerPlayerCommunicationListener {
    public ServerPlayer(Board board, String name) {
        super(board, name);
    }

    public ServerPlayer(Board board) {
        super(board);
    }

    @Override
    public void turnReceive(String whoPlayer, String move) {
        if (whoPlayer.equals(getName())) {
            try {
                int intMove = Integer.parseInt(move);

                if (intMove < 0 || intMove >= (board.getWidth() * board.getHeight())) {
                    System.err.println("ServerPlayer received an out of bounds move from the server! Move nr: " + intMove + ". Discarding move..");
                    return;
                }

                int x = intMove % getBoard().getWidth();
                int y = intMove / getBoard().getWidth();

                if (!board.isValidMove(x, y)) {
                    System.err.println("ServerPlayer received an invalid move from the server! Move coordinates: (" + x + ", " + y + "). Discarding move..");
                    System.err.println("Let's hope it's not our Board#getValidMoves implementation who's wrong :S");
                    return;
                }

                board.makeRawMove(this, x, y);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void finalizeTurn() {
        board.finalizeRawMove();
    }
}
