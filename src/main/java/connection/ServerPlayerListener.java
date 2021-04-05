package connection;

public interface ServerPlayerListener
{
    void opponentTurn(String move);
    void endMatch(String result);

}
