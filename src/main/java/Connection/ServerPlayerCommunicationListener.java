package Connection;

public interface ServerPlayerCommunicationListener {
    void opponentTurn(String move);

    void endMatch(String result);

    void finalizeTurn();
}
