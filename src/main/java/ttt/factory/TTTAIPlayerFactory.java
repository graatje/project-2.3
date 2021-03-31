package ttt.factory;

import framework.board.Board;
import framework.factory.PlayerFactory;
import framework.player.Player;
import ttt.player.TTTRandomAIPlayer;

public class TTTAIPlayerFactory implements PlayerFactory {
    @Override
    public Player createPlayer(Board board, String name) {
        return new TTTRandomAIPlayer(board, name);
    }
}
