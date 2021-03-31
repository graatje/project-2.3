package ttt.player;

import framework.board.Board;
import framework.player.MoveRequestable;
import framework.player.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TTTConsoleLocalPlayer extends Player implements MoveRequestable {
    public TTTConsoleLocalPlayer(Board board) {
        super(board);
    }

    @Override
    public void requestMove() {
        System.out.println("Please enter the coordinates of the move you want to make (x,y):");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int x, y;
        try {
            String line = reader.readLine();
            String[] parts = line.split(",");

            x = Integer.parseInt(parts[0]);
            y = Integer.parseInt(parts[1]);
        } catch (Exception ignored) {
            return;
        }

        board.makeMove(this, x, y);
    }
}
