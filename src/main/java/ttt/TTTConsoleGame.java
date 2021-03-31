package ttt;

import Connection.Connection;
import framework.GameManager;
import framework.board.Board;
import framework.board.BoardObserver;
import framework.board.BoardPiece;
import framework.player.Player;
import framework.player.ConsoleLocalPlayer;
import ttt.player.TTTAIPlayer;

import java.io.IOException;

public class TTTConsoleGame implements BoardObserver {
    public static void main(String[] args) {
        new TTTConsoleGame();
    }

    private final Board board;

    public TTTConsoleGame() {

        Connection connection;
        try{
            connection = new Connection("localhost", 7789);
        }catch(IOException e) {
            System.out.println("Could not connect to server, continuing without a connection :(");
            System.out.println(e.getMessage());

            connection = null;
        }

        GameManager gameManager = new TTTGameManager(connection);
        board = gameManager.getBoard();

        board.registerObserver(this);

        gameManager.addPlayer(new TTTAIPlayer(board, "test-ai"));
        gameManager.addPlayer(new ConsoleLocalPlayer(board, "test-local"));

        gameManager.start(gameManager.getPlayer(1));
    }

    @Override
    public void boardUpdated() {
        System.out.println();
        System.out.println("Current board:");
        for(int y = 0; y < board.getHeight(); y++) {
            for(int x = 0; x < board.getWidth(); x++) {
                BoardPiece piece = board.getBoardPiece(x, y);
                System.out.print(getPlayerChar(piece.getOwner()));
            }
            System.out.println();
        }

        if(board.isGameOver()) {
            System.out.println();
            System.out.println("---");
            System.out.println("GAME OVER! Winner: " + getPlayerChar(board.getWinner()));
        }
    }

    private char getPlayerChar(Player player) {
        if(player != null) {
            if (player.getID() == 0) {
                return 'X';
            } else if (player.getID() == 1) {
                return 'O';
            }
        }

        return ' ';
    }
}
