package ttt;

import framework.ConnectedGameManager;
import framework.GameManager;
import framework.board.Board;
import framework.board.BoardObserver;
import framework.board.BoardPiece;
import framework.player.ConsoleLocalPlayer;
import framework.player.Player;
import ttt.board.TTTBoard;
import ttt.player.TTTAIPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TTTConsoleGame implements BoardObserver {
    public static void main(String[] args) {
        new TTTConsoleGame();
    }

    private Board board;

    public TTTConsoleGame() {
        /* UNCOMMENT FOR CONNECTED GAMEMANAGER */
        ConnectedGameManager gameManager;
        try {
            gameManager = new ConnectedGameManager(
                    TTTBoard::new,
                    "main-vps.woutergritter.me",
                    7789,
                    b -> new TTTAIPlayer(b, 3)
            );
        } catch (IOException e) {
            e.printStackTrace();

            System.exit(-1);
            return;
        }

        /* UNCOMMENT FOR LOCAL GAMEMANAGER */
//        GameManager gameManager = new GameManager(TTTBoard::new);
//        gameManager.addPlayer(new TTTAIPlayer(gameManager.getBoard(), 2));
//        gameManager.addPlayer(new ConsoleLocalPlayer(gameManager.getBoard()));

        new Thread(() -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try{
                while(true) {
                    String line = reader.readLine();
                    System.out.println("SENDING COMMAND: " + line);
                    gameManager.getClient().sendCommandToServer(line + '\n');
                }
            }catch(IOException e) {
                e.printStackTrace();
            }
        }).start();

        board = gameManager.getBoard();
        board.registerObserver(this);

        /* UNCOMMENT FOR CONNECTED GAMEMANAGER */
        gameManager.setSelfName("Wouter");
        gameManager.login();
//        gameManager.subscribe("Tic-tac-toe");

        /* UNCOMMENT FOR LOCAL GAMEMANAGER */
//        gameManager.start(null);
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
