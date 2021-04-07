package test;

import framework.ConnectedGameManager;
import framework.GameManager;
import framework.board.Board;
import framework.board.BoardObserver;
import framework.board.BoardPiece;
import framework.player.MinimaxAIPlayer;
import framework.player.Player;
import othello.board.OthelloBoard;
import ttt.board.TTTBoard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Function;

public class TestConsoleGame implements BoardObserver {
    public static void main(String[] args) {
        new TestConsoleGame();
    }

    private Board board;

    public TestConsoleGame() {
        GameManager gameManager;

        // Meant for testing purposes!
        // Only change these final variables when testing
        final boolean CONNECT_TO_SERVER = false;
        final String SERVER_GAME_NAME = "Tic-tac-toe";
        final String SERVER_SELF_NAME = "TestConsoleGame-" + (int) (Math.random() * 100);

        final Function<GameManager, Board> boardProvider = OthelloBoard::new;
        final Function<Board, Player> localPlayerProvider = b -> new MinimaxAIPlayer(b, 4, MinimaxAIPlayer.AIDifficulty.HARD);
        final Function<Board, Player> secondPlayerProvider = b -> new MinimaxAIPlayer(b, 4, MinimaxAIPlayer.AIDifficulty.HARD);

        if(CONNECT_TO_SERVER) {
            // -- SERVER GAME -- //

            try {
                gameManager = new ConnectedGameManager(
                        boardProvider,
                        "main-vps.woutergritter.me",
                        7789,
                        localPlayerProvider
                );
            } catch (IOException e) {
                e.printStackTrace();

                System.exit(-1);
                return;
            }

            // Pass the console input to the server!
            new Thread(() -> {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                try {
                    while (true) {
                        String line = reader.readLine();
                        ((ConnectedGameManager) gameManager).getClient().sendCommandToServer(line + '\n');
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }else {
            // -- LOCAL GAME -- //
            gameManager = new GameManager(boardProvider);
            gameManager.addPlayer(localPlayerProvider.apply(gameManager.getBoard()));
            gameManager.addPlayer(secondPlayerProvider.apply(gameManager.getBoard()));
        }

        board = gameManager.getBoard();
        board.registerObserver(this);

        if(CONNECT_TO_SERVER) {
            // -- START SERVER GAME -- //

            ((ConnectedGameManager) gameManager).setSelfName(SERVER_SELF_NAME);
            ((ConnectedGameManager) gameManager).login();
            ((ConnectedGameManager) gameManager).subscribe(SERVER_GAME_NAME);
        }else{
            // -- START LOCAL GAME -- //
            gameManager.start(null);
        }
    }

    @Override
    public void onPlayerMoved(Player who, BoardPiece where) {
        System.out.println("MOVE BY " + getPlayerChar(who) + ": (" + where.getX() + ", " + where.getY() + ")");
    }

    @Override
    public void onPlayerMoveFinalized(Player previous, Player current) {
        System.out.println();
        printBoard();
    }

    @Override
    public void onPlayerWon(Player who) {
        System.out.println();
        System.out.println("---");
        System.out.println("GAME OVER! Winner: " + getPlayerChar(board.getWinner()));
    }

    @Override
    public void onGameStart(Player startingPlayer) {
        System.out.println("GAME STARTING!");
        printBoard();
    }

    private void printBoard() {
        System.out.println("Current board:");

        System.out.print("  ");
        for(int x = 0; x < board.getWidth(); x++) {
            System.out.print(x + " ");
        }
        System.out.println();

        for (int y = 0; y < board.getHeight(); y++) {
            System.out.print(y + " ");
            for (int x = 0; x < board.getWidth(); x++) {
                BoardPiece piece = board.getBoardPiece(x, y);
                System.out.print(getPlayerChar(piece.getOwner()) + " ");
            }
            System.out.println();
            System.out.println();
        }
    }

    private char getPlayerChar(Player player) {
        if (player != null) {
            if (player.getID() == 0) {
                return '#';
            } else if (player.getID() == 1) {
                return 'O';
            }
        }

        return ' ';
    }
}
