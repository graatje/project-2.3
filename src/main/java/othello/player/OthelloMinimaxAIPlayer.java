package othello.player;

import framework.board.Board;
import framework.player.MinimaxAIPlayer;
import framework.player.Player;

public class OthelloMinimaxAIPlayer extends MinimaxAIPlayer {
    /**
     * Weights for every othello piece.
     * Credits: https://github.com/cgeopapa/Reversi-Othello-minimax/blob/6062b0da1348b648aeb358712cbcb8a77f80490c/Board.java#L174
     */
    private static final float[][] PIECE_WEIGHTS = {
            {20, -3, 11, 8,  8, 11, -3, 20},
            {-3, -7, -4, 1,  1, -4, -7, -3},
            {11, -4, 2,  2,  2,  2, -4, 11},
            { 8,  1, 2, -3, -3,  2,  1,  8},
            { 8,  1, 2, -3, -3,  2,  1,  8},
            {11, -4, 2,  2,  2,  2, -4, 11},
            {-3, -7, -4, 1,  1, -4, -7, -3},
            {20, -3, 11, 8,  8, 11, -3, 20}
    };

//    /**
//     * https://github.com/hylbyj/Alpha-Beta-Pruning-for-Othello-Game/blob/master/readme_alpha_beta.txt
//     */
//    private static final float[][] PIECE_WEIGHTS = {
//            { 4, -3,  2,  2,  2,  2, -3,  4},
//            {-3, -4, -1, -1, -1, -1, -4, -3},
//            { 2, -1,  1,  0,  0,  1, -1,  2},
//            { 2, -1,  0,  1,  1,  0, -1,  2},
//            { 2, -1,  0,  1,  1,  0, -1,  2},
//            { 2, -1,  1,  0,  0,  1, -1,  2},
//            {-3, -4, -1, -1, -1, -1, -4, -3},
//            { 4, -3,  2,  2,  2,  2, -3,  4}
//    };

    public OthelloMinimaxAIPlayer(Board board, int id, String name, AIDifficulty difficulty) {
        super(board, id, name, difficulty);
    }

    public OthelloMinimaxAIPlayer(Board board, int id, AIDifficulty difficulty) {
        super(board, id, difficulty);
    }

    @Override
    protected float evaluateBoard(Board board, int treeDepth) {
        int b = 0, w = 0;
        for(int x = 0; x < 8; x++) {
            for(int y = 0; y < 8; y++) {
                Player owner = board.getBoardPiece(x, y).getOwner();
                if(owner == this) {
                    b += PIECE_WEIGHTS[x][y];
                }else if(owner != null) {
                    w += PIECE_WEIGHTS[x][y];
                }
            }
        }

        float value = 0;
        if(b + w != 0) {
            value = (float) (b - w) / (b + w);
        }

        b += board.getValidMoves(this).size();
        w += board.getValidMoves(board.getGameManager().getOtherPlayer(this)).size();

        if(b + w != 0) {
            value += (float) (b - w) / (b + w);
        }

        return value;
    }

    @Override
    public int getStartDepth() {
        return 5;
    }
}
