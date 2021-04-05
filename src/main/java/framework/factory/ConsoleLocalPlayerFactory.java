package framework.factory;

import framework.board.Board;
import framework.player.ConsoleLocalPlayer;
import framework.player.Player;

public class ConsoleLocalPlayerFactory implements PlayerFactory {
    @Override
    public Player createPlayer(Board board, String name) {
        return new ConsoleLocalPlayer(board, name);
    }
}
