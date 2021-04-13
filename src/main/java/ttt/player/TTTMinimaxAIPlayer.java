package ttt.player;

import framework.board.Board;
import framework.player.MinimaxAIPlayer;
import framework.player.Player;

public class TTTMinimaxAIPlayer extends MinimaxAIPlayer {
    public TTTMinimaxAIPlayer(Board board, String name, AIDifficulty difficulty) {
        super(board, name, difficulty);
    }

    public TTTMinimaxAIPlayer(Board board, AIDifficulty difficulty) {
        super(board, difficulty);
    }

    @Override
    protected float evaluateBoard(Board board, int treeDepth) {
        Player winner = board.calculateWinner();
        if (winner == this) {
            // Win for self
            return 10 + treeDepth;
        } else if (winner != null) {
            // Win for other
            return -10 - treeDepth;
        } else {
            // Draw or no win
            return 0;
        }
    }

    @Override
    public int getStartDepth() {
        return 7;
    }
}
