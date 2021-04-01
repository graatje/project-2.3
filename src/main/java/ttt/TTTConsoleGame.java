package ttt;

import framework.ConnectedGameManager;
import framework.board.Board;
import framework.board.BoardObserver;
import framework.board.BoardPiece;
import framework.player.Player;
import ttt.factory.TTTAIPlayerFactory;

import java.io.IOException;

public class TTTConsoleGame implements BoardObserver {
    public static void main(String[] args) {
        new TTTConsoleGame();
    }

    private Board board;

    public TTTConsoleGame() {
        ConnectedGameManager gameManager;
        try {
            gameManager = new TTTConnectedGameManager("localhost", 7789, new TTTAIPlayerFactory(3));
        } catch (IOException e) {
            e.printStackTrace();

            System.exit(-1);
            return;
        }

        board = gameManager.getBoard();
        board.registerObserver(this);

//        gameManager.setSelfName("Mike");
        gameManager.login();
        gameManager.subscribe("Tic-tac-toe");
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
