package framework;

import connection.Client;
import connection.CommunicationHandler;
import connection.GameManagerCommunicationListener;
import framework.board.BoardObserver;
import framework.board.BoardPiece;
import framework.factory.BoardFactory;
import framework.factory.PlayerFactory;
import framework.player.Player;
import framework.player.ServerPlayer;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

public abstract class ConnectedGameManager extends GameManager implements GameManagerCommunicationListener, BoardObserver {
    private PlayerFactory selfPlayerFactory;

    private Client client;

    private final HashMap<Integer, Match> activeMatches = new HashMap<>();
    private final ArrayList<String> lobbyPlayers = new ArrayList<>();

    private ServerPlayer serverPlayerOpponent;
    private String selfName = "unknown-" + (int) (Math.random() * 100);

    /**
     * constructor, initializes connection, board and players.
     *
     * @param serverIP
     * @param serverPort
     * @param boardFactory
     * @param selfPlayerFactory
     */
    public ConnectedGameManager(String serverIP, int serverPort, BoardFactory boardFactory, PlayerFactory selfPlayerFactory) throws IOException {
        super(boardFactory);

        this.selfPlayerFactory = selfPlayerFactory;

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

    public void login() {
        client.sendLoginMessage(selfName);
    }

    public void subscribe(String gameName) {
        client.sendSubscribeMessage(gameName);
    }


    public Client getClient() {
        return client;
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

    public void setSelfPlayerFactory(PlayerFactory selfPlayerFactory) {
        this.selfPlayerFactory = selfPlayerFactory;
    }

    public PlayerFactory getSelfPlayerFactory() {
        return selfPlayerFactory;
    }

    @Override
    public void getMatchRequest(String opponent, String gametype, String challengeNR) {
        Match match = new Match(opponent, gametype, challengeNR);

        activeMatches.put(match.getChallengeNR(), match);
    }

    @Override
    public void startServerMatch(String opponentName, String playerToBegin) {
        serverPlayerOpponent = new ServerPlayer(getBoard(), opponentName);
        Player self = selfPlayerFactory.createPlayer(board, selfName);

        client.getCommunicationHandler().setServerPlayerCommunicationListener(serverPlayerOpponent);

        addPlayer(serverPlayerOpponent);
        addPlayer(self);

        start(serverPlayerOpponent, false);
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
}
