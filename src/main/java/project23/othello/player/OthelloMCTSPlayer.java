package project23.othello.player;
import project23.framework.board.Board;
import project23.framework.player.MCTSPlayer;
import project23.framework.player.MinimaxAIPlayer;
import project23.framework.player.Player;
import project23.util.Logger;


public class OthelloMCTSPlayer extends MCTSPlayer{
    private static final float[][] PIECE_WEIGHTS = {  // do we even want this? to be continued.
            { 4, -3,  2,  2,  2,  2, -3,  4},
            {-3, -4, -1, -1, -1, -1, -4, -3},
            { 2, -1,  1,  0,  0,  1, -1,  2},
            { 2, -1,  0,  1,  1,  0, -1,  2},
            { 2, -1,  0,  1,  1,  0, -1,  2},
            { 2, -1,  1,  0,  0,  1, -1,  2},
            {-3, -4, -1, -1, -1, -1, -4, -3},
            { 4, -3,  2,  2,  2,  2, -3,  4}
    };

    public OthelloMCTSPlayer(Board board, int id, String name) {
        super(board, id, name);
    }

    public OthelloMCTSPlayer(Board board, int id) {
        super(board, id);
    }

    @Override
    protected float evaluateBoard(Board board) {
        return evaluateBoardMethod4(board);
    }

    private float evaluateBoardMethod1(Board board){
        int selfPieces = 0, otherPieces = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Player owner = board.getBoardPiece(x, y).getOwner();
                if (owner == this) {
                    selfPieces += PIECE_WEIGHTS[x][y];
                } else if (owner != null) {
                    otherPieces += PIECE_WEIGHTS[x][y];
                }
            }
        }
        float value = 0;
        if (selfPieces + otherPieces != 0) {
            value = (float) (selfPieces - otherPieces) / (selfPieces + otherPieces);
        }

        if (selfPieces + otherPieces != 0) {
            value += (float) (selfPieces - otherPieces) / (selfPieces + otherPieces);
        }
        return value;
    }

    private float evaluateBoardMethod2(Board board){
        float selfPieces = 0, otherPieces = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Player owner = board.getBoardPiece(x, y).getOwner();
                if (owner == this) {
                    selfPieces += PIECE_WEIGHTS[x][y];
                } else if (owner != null) {
                    otherPieces += PIECE_WEIGHTS[x][y];
                }
            }
        }

        return selfPieces - otherPieces;
    }

    private float evaluateBoardMethod3(Board board){
        float selfPieces = 0, otherPieces = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Player owner = board.getBoardPiece(x, y).getOwner();
                if (owner == this) {
                    selfPieces += 1;
                } else if (owner != null) {
                    otherPieces += 1;
                }
            }
        }

        return selfPieces - otherPieces;
    }

    private float evaluateBoardMethod4(Board board){
        float selfPieces = 0, otherPieces = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Player owner = board.getBoardPiece(x, y).getOwner();
                if (owner == this) {
                    selfPieces += 1;
                } else if (owner != null) {
                    otherPieces += 1;
                }
            }
        }
        if(selfPieces - otherPieces > 0){
            return 1;
        }
        else if(selfPieces -otherPieces < 0){
            return -1;
        }
        return 0;
    }
}
