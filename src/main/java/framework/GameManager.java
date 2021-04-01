package framework;

import Connection.Connection;
import Connection.GameManagerCommunicationListener;
import framework.board.Board;
import framework.factory.BoardFactory;
import framework.factory.PlayerFactory;
import framework.player.Player;
import framework.player.ServerPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * This class manages a game. Including connection, board and players.
 */
public abstract class GameManager implements GameManagerCommunicationListener {

    private final Connection connection;
    private final Board board;
    private final PlayerFactory selfPlayerFactory;

    private final List<Player> players = new ArrayList<>();

    private HashMap<Integer, Match> activeMatches;


    /**
     * constructor, initializes connection, board and players.
     *
     * @param connection
     * @param boardFactory
     */
    public GameManager(Connection connection, BoardFactory boardFactory, PlayerFactory selfPlayerFactory) {
        this.connection = connection;
        this.board = boardFactory.createBoard(this);
        this.selfPlayerFactory = selfPlayerFactory;

        if (connection != null) {
            connection.getClient().getCommunicationHandler().setGameManagerCommunicationListener(this);
        }
    }

    /**
     * An implementation-specific getter for the minimum number of players.
     *
     * @return The minimum number of players
     */
    public abstract int getMinPlayers();

    /**
     * An implementation-specific getter for the maximum number of players.
     *
     * @return The maximum number of players
     */
    public abstract int getMaxPlayers();

    /**
     * this method requests a playermove from the board if all players have been initialized.
     */
    public void start(Player startingPlayer) {
        if (players.size() < getMinPlayers() || players.size() > getMaxPlayers()) {
            throw new IllegalStateException("The number of players must be between " + getMinPlayers() + " and " + getMaxPlayers() + ", and is currently " + players.size() + "!");
        }

        for (Player player : players) {
            if (player == null) {
                throw new IllegalStateException("Not all players have been initialized yet!");
            }
        }

        board.setCurrentPlayerID(startingPlayer.getID());

        // Request a move from the first player
        board.requestPlayerMove();
    }

    /**
     * getter for connection
     *
     * @return Connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * @return Whether the GameManager possesses a connection object or not
     */
    public boolean hasConnection() {
        return connection != null;
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

    @Override
    public void getMatchRequest(String opponent, String gametype, String challengeNR) {
        Match match = new Match(opponent, gametype, challengeNR);

        activeMatches.put(match.getChallengeNR(), match);
    }

    @Override
    public void startServerMatch(String opponentName, String playerToBegin) {
        Player opponent = new ServerPlayer(getBoard(), opponentName);
        Player self = selfPlayerFactory.createPlayer(board, "thisMachine");

        addPlayer(opponent);
        addPlayer(self);

        if (playerToBegin.equals(opponentName)) {
            start(opponent);
        } else {
            start(self);
        }
    }

    @Override
    public void matchCancelled(String challengeNR) {
        activeMatches.remove(Integer.parseInt(challengeNR));
    }
}
