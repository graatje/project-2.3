package project23.framework;

import project23.connection.Client;
import project23.connection.CommunicationHandler;
import project23.connection.GameManagerCommunicationListener;
import project23.framework.board.Board;
import project23.framework.board.BoardObserver;
import project23.framework.board.BoardPiece;
import project23.framework.player.Player;
import project23.framework.player.ServerPlayer;

import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ConnectedGameManager extends GameManager implements GameManagerCommunicationListener, BoardObserver {
    private Client client;

    private final List<ChallengeRequest> activeChallengeRequests = new ArrayList<>();
    private final List<String> lobbyPlayers = new ArrayList<>();

    private boolean loggedIn = false;

    private ServerPlayer serverPlayerOpponent;
    private String selfName = "unknown" + (int) (Math.random() * 100);

    private final Set<ConnectedGameManagerObserver> observers = new HashSet<>();

    /**
     * constructor, initializes project23.connection, board and players.
     *
     * @param boardSupplier
     * @param serverIP
     * @param serverPort
     * @param selfPlayerSupplier
     * @throws IOException
     */
    public ConnectedGameManager(Function<GameManager, Board> boardSupplier, String serverIP, int serverPort, BiFunction<Board, Integer, Player> selfPlayerSupplier) throws IOException {
        super(boardSupplier);

        updateSelfPlayerSupplier(selfPlayerSupplier);

        createClient(serverIP, serverPort);
        client.sendGetPlayerlistMessage();

        client.getCommunicationHandler().setGameManagerCommunicationListener(this);
        board.registerObserver(this);
    }

    public void updateSelfPlayerSupplier(BiFunction<Board, Integer, Player> selfPlayerSupplier) {
        playerSuppliers.clear();
        playerSuppliers.add(ServerPlayer::new);
        playerSuppliers.add(selfPlayerSupplier);
    }

    /**
     * Creates a Client to start handling communication
     */
    private void createClient(String serverIP, int serverPort) throws IOException {
        Socket clientSocket = new Socket(serverIP, serverPort);
        client = new Client(clientSocket, new CommunicationHandler());
        client.setDaemon(true);
        client.start();
    }

    public void login() {
        // We only have to log-in once!
        if (loggedIn) {
            return;
        }

        client.sendLoginMessage(selfName);
        loggedIn = true;
    }

    public void subscribe() {
        client.sendSubscribeMessage(getGameType().serverName);
    }

    public Client getClient() {
        return client;
    }

    public void closeClient() {
        client.close();
    }

    public List<ChallengeRequest> getActiveChallengeRequests() {
        return activeChallengeRequests;
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

    public void challengePlayer(String playerToChallenge) {
        client.sendChallengeMessage(playerToChallenge, getGameType().serverName);
    }

    public void acceptChallengeRequest(ChallengeRequest challengeRequest) {
        client.acceptChallenge(challengeRequest.getChallengeNr());
    }

    /**
     * Request start subscribes!
     */
    @Override
    public void requestStart() {
        setSelfName(ConfigData.getInstance().getPlayerName());
        login();
        subscribe();
    }

    @Override
    public void forfeit() {
        getClient().sendForfeitMessage();

        super.forfeit();
    }

    @Override
    public void destroy() {
        closeClient();

        super.destroy();
    }

    @Override
    public void onChallengeRequestReceive(String opponent, String gameTypeServerName, int challengeNr) {
        ChallengeRequest challengeRequest = new ChallengeRequest(opponent, GameType.getByServerName(gameTypeServerName), challengeNr);
        activeChallengeRequests.add(challengeRequest);

        observers.forEach(o -> o.onChallengeRequestReceive(challengeRequest));
    }

    @Override
    public void startServerMatch(String opponentName, String playerToBegin) {
        if (isInitialized()) {
            reset();
        }

        observers.forEach(ConnectedGameManagerObserver::onPreGameStart);

        initialize();

        serverPlayerOpponent = null;
        for (Player player : players) {
            if (player instanceof ServerPlayer) {
                serverPlayerOpponent = (ServerPlayer) player;
                break;
            }
        }

        if (serverPlayerOpponent == null) {
            // This should never happen!
            throw new IllegalStateException("Could not find a server player.");
        }

        serverPlayerOpponent.setName(opponentName);
        getOtherPlayer(serverPlayerOpponent).setName(this.selfName);

        client.getCommunicationHandler().setServerPlayerCommunicationListener(serverPlayerOpponent);

        _start(getPlayer(playerToBegin));

        observers.forEach(ConnectedGameManagerObserver::onPostGameStart);
    }

    @Override
    public void challengeRequestCancelled(int challengeNr) {
        activeChallengeRequests.removeIf(challengeRequest -> challengeRequest.getChallengeNr() == challengeNr);
    }

    @Override
    public void updateLobbyPlayers(List<String> lobbyPlayers) {
        this.lobbyPlayers.clear();
        this.lobbyPlayers.addAll(lobbyPlayers);
        this.lobbyPlayers.remove(selfName);

        observers.forEach(ConnectedGameManagerObserver::onPlayerListReceive);
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
    public void onServerError(String errorMessage) {
        observers.forEach(o -> o.onServerError(errorMessage));
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

    public void registerObserver(ConnectedGameManagerObserver observer) {
        observers.add(observer);
    }

    public void unregisterObserver(ConnectedGameManagerObserver observer) {
        observers.remove(observer);
    }
}
