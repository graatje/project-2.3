package Connection;

public interface ServerPlayerListener {
    void opponentTurn(String move);

    void endMatch(String result);

}
