package ttt;

import Connection.Connection;
import framework.GameManager;
import framework.board.Board;
import framework.board.BoardObserver;
import framework.board.BoardPiece;
import framework.player.ConsoleLocalPlayer;
import framework.player.Player;
import ttt.player.TTTAIPlayer;

import java.io.IOException;

public class TTTConsoleGame implements BoardObserver {
    public static void main(String[] args) {
        new TTTConsoleGame();
    }

    private final Board board;

    public TTTConsoleGame() {
        Connection connection = null;
        try {
            connection = new Connection("localhost", 7789);
        } catch (IOException e) {
            System.out.println("Could not connect to server, continuing without a connection :(");
            System.out.println(e.getMessage());

            System.exit(-1);
        }

        GameManager gameManager = new TTTGameManager(connection);
        board = gameManager.getBoard();
        board.registerObserver(this);

        String name = "TTTConsoleGame-" + (int) (Math.random() * 100);
        connection.getClient().sendCommandToServer("login " + name + "\n");
        connection.getClient().sendCommandToServer("subscribe Tic-tac-toe\n");
    }

    @Override
    public void onPlayerMoved(Player who, BoardPiece where) {
    }

    @Override
    public void onPlayerMoveFinalized(Player previous, Player current) {
        System.out.println();
        System.out.println("Current board:");
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                BoardPiece piece = board.getBoardPiece(x, y);
                System.out.print(getPlayerChar(piece.getOwner()));
            }
            System.out.println();
        }
    }

    @Override
    public void onPlayerWon(Player who) {
        System.out.println();
        System.out.println("---");
        System.out.println("GAME OVER! Winner: " + getPlayerChar(board.getWinner()));
    }

    private char getPlayerChar(Player player) {
        if (player != null) {
            if (player.getID() == 0) {
                return 'X';
            } else if (player.getID() == 1) {
                return 'O';
            }
        }

        return ' ';
    }
}
