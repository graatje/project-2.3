package framework;

import connection.Client;
import connection.CommunicationHandler;
import connection.GameManagerCommunicationListener;
import framework.board.Board;
import framework.board.BoardObserver;
import framework.board.BoardPiece;
import framework.player.Player;
import framework.player.ServerPlayer;

import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.function.Function;

public class ConnectedGameManager extends GameManager implements GameManagerCommunicationListener, BoardObserver {
    private Client client;

    private final HashMap<Integer, Match> activeMatches = new HashMap<>();
    private final ArrayList<String> lobbyPlayers = new ArrayList<>();

    private ServerPlayer serverPlayerOpponent;
    private String selfName = "unknown-" + (int) (Math.random() * 100);

    /**
     * constructor, initializes connection, board and players.
     *
     * @param boardSupplier
     * @param serverIP
     * @param serverPort
     * @param selfPlayerSupplier
     * @throws IOException
     */
    public ConnectedGameManager(Function<GameManager, Board> boardSupplier, String serverIP, int serverPort, Function<Board, Player> selfPlayerSupplier) throws IOException {
        super(boardSupplier, selfPlayerSupplier, ServerPlayer::new);

        createClient(serverIP, serverPort);
        client.sendCommandToServer("get playerlist\n");

        client.getCommunicationHandler().setGameManagerCommunicationListener(this);
        board.registerObserver(this);
    }

    /**
     * Creates a Client to start handling communication
     */
    private void createClient(String serverIP, int serverPort) throws IOException {
        Socket clientSocket = new Socket(serverIP, serverPort);
        client = new Client(clientSocket, new CommunicationHandler());
        client.start();
    }

    /**
     * Closes the previous connection, and starts a new one
     *
     * @param serverIP
     * @param serverPort
     * @throws IOException
     */
    public void resetClient(String serverIP, int serverPort) throws IOException {
        client.sendLogoutMessage();
        client.close();

        createClient(serverIP, serverPort);
    }

    public void login() {
        client.sendLoginMessage(selfName);
    }

    public void subscribe(String gameName) {
        client.sendSubscribeMessage(gameName);
    }

    public Client getClient() {
        return client;
    }

    public void closeClient(){
        client.close();
    }

    public Map<Integer, Match> getActiveMatches() {
        return Collections.unmodifiableMap(activeMatches);
    }

    public List<String> getLobbyPlayers() {
        return Collections.unmodifiableList(lobbyPlayers);
    }

    public String getSelfName() {
        return selfName;
    }

    public void setSelfName(String selfName) {
        this.selfName = selfName;
    }

    public void challengePlayer(String playerToChallenge, String gameType) {
        client.sendChallengeMessage(playerToChallenge, gameType);
    }

    public void acceptChallenge(int challengeNr) {
        client.acceptChallenge(challengeNr);
    }

    @Override
    public void getMatchRequest(String opponent, String gametype, String challengeNR) {
        Match match = new Match(opponent, gametype, challengeNR);

        activeMatches.put(match.getChallengeNR(), match);
    }

    @Override
    public void startServerMatch(String opponentName, String playerToBegin) {
        initialize();

        serverPlayerOpponent = null;
        for(Player player : players) {
            if(player instanceof ServerPlayer) {
                serverPlayerOpponent = (ServerPlayer) player;
                break;
            }
        }

        if(serverPlayerOpponent == null) {
            // This should never happen!
            throw new IllegalStateException("Could not find a server player.");
        }

        serverPlayerOpponent.setName(opponentName);
        getOtherPlayer(serverPlayerOpponent).setName(this.selfName);

        client.getCommunicationHandler().setServerPlayerCommunicationListener(serverPlayerOpponent);

        start(getPlayer(playerToBegin));
    }

    @Override
    public void matchCancelled(String challengeNR) {
        activeMatches.remove(Integer.parseInt(challengeNR));
    }

    @Override
    public void updateLobbyPlayers(List<String> lobbyPlayers) {
        this.lobbyPlayers.clear();
        this.lobbyPlayers.addAll(lobbyPlayers);
    }

    @Override
    public void endMatch(String result) {
        board.finalizeRawMove();

        switch (result) {
            case "WIN": // Non-server / self player won!
                board.forceWin(getOtherPlayer(serverPlayerOpponent));
                break;
            case "LOSS": // Server / opponent player won! (We lost)
                board.forceWin(serverPlayerOpponent);
                break;
            case "DRAW": // Draw!
                board.forceWin(null);
                break;
            default:
                System.err.println("Received match end result '" + result + "' from the server, which is not a valid result!");
                break;
        }
    }

    @Override
    public void onPlayerMoved(Player who, BoardPiece where) {
        if (who != serverPlayerOpponent) {
            int move = where.getX() + board.getWidth() * where.getY();
            client.sendMoveMessage(move);
        }
    }

    @Override
    public void onPlayerMoveFinalized(Player previous, Player current) {
    }

    @Override
    public void onPlayerWon(Player who) {
    }

    @Override
    public void onGameStart(Player startingPlayer) {
    }

    @Override
    public void reset() {
        super.reset();
        subscribe(ConfigData.getInstance().getGameType().gameName);
    }
}
