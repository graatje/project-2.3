package framework;

import framework.board.Board;
import framework.factory.BoardFactory;
import framework.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class manages a game. Including connection, board and players.
 */
public class GameManager {

    protected final Board board;

    protected final List<Player> players = new ArrayList<>();

    /**
     * constructor, initializes connection, board and players.
     *
     * @param boardFactory
     */
    public GameManager(BoardFactory boardFactory) {
        this.board = boardFactory.createBoard(this);
    }

    /**
     * this method requests a playermove from the board if all players have been initialized.
     */
    public void start(Player startingPlayer, boolean requestFirstPlayerMove) {
        if (players.size() < board.getMinPlayers() || players.size() > board.getMaxPlayers()) {
            throw new IllegalStateException("The number of players must be between " + board.getMinPlayers() + " and " + board.getMaxPlayers() + ", and is currently " + players.size() + "!");
        }

        if (startingPlayer == null) {
            startingPlayer = players.get((int) (Math.random() * players.size()));
        }

        board.setCurrentPlayerID(startingPlayer.getID());

        // Request a move from the first player
        if (requestFirstPlayerMove) {
            board.requestPlayerMove();
        }
    }

    public void start(Player startingPlayer) {
        start(startingPlayer, true);
    }

    /**
     * getter for board.
     *
     * @return Board
     */
    public Board getBoard() {

        return board;
    }

    /**
     * Getter for the array players
     *
     * @return List<Player>
     */
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public Player getPlayer(int id) {
        /**
         * getter for an individual player from the array players.
         * @param int id, the index of the player in the arraylist players.
         * @return Player player.
         */
        if (id < 0 || id >= players.size()) {
            throw new IllegalArgumentException("Invalid player ID!");
        }

        return players.get(id);
    }

    /**
     * Adds a player to the game.
     *
     * @param player, the player you want to add.
     * @return the ID of the added player.
     */
    public int addPlayer(Player player) {
        players.add(player);

        int id = players.size() - 1; // When adding an item on the end of the list, the index is n-1.
        player.setID(id);

        return id;
    }

    /**
     * Removes a player from the game.
     *
     * @param player
     */
    public void removePlayer(Player player) {
        players.remove(player);

        // Recalculate ID's of all other players
        for (int id = 0; id < players.size(); id++) {
            players.get(id).setID(id);
        }
    }

    public Player getOtherPlayer(Player notThis) {
        if (players.size() > 2) {
            throw new IllegalStateException("There are more than 2 players, please use GameManager#getOtherPlayers instead!");
        }

        for (Player other : players) {
            if (other != notThis) {
                return other;
            }
        }

        return null;
    }

    public List<Player> getOtherPlayers(Player notThis) {
        List<Player> result = new ArrayList<>(this.players);
        result.removeIf(other -> other == notThis);

        return result;
    }
}
