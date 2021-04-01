package Connection;

public interface ServerPlayerCommunicationListener {
    void turnReceive(String whoPlayer, String move);

    void endMatch(String result);

    void finalizeTurn();
}
