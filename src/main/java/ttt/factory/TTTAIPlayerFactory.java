package ttt.factory;

import framework.board.Board;
import framework.factory.PlayerFactory;
import framework.player.Player;
import ttt.player.TTTAIPlayer;

public class TTTAIPlayerFactory implements PlayerFactory {
    private final int difficulty;

    public TTTAIPlayerFactory(int difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public Player createPlayer(Board board, String name) {
        return new TTTAIPlayer(board, name, difficulty);
    }
}
