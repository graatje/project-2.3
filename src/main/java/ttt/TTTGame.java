package ttt;

import framework.ConfigData;
import framework.Game;
import framework.GameManager;
import framework.board.Board;
import framework.player.MinimaxAIPlayer;
import framework.player.Player;
import javafx.scene.paint.Color;
import ttt.board.TTTBoard;
import ttt.player.TTTMinimaxAIPlayer;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class TTTGame extends Game {
    private final String[] boardPieceNames = { "Noughts (O)", "Crosses (X)" };
    private final List<URL> boardPieceIcons = Arrays.asList(getClass().getResource("/boardPieces/ttt_o.png"), getClass().getResource("/boardPieces/ttt_x.png"));
    private final boolean showPiecesCount = false;
    private final Color colors = Color.rgb(245, 245, 245);
    private final String gameName = "Tic-tac-toe";

    @Override
    public Function<Board, Player> createAIPlayerFactory() {
        return (b) -> new TTTMinimaxAIPlayer(b, Game.AI_NAME, ConfigData.getInstance().getAIDifficulty());
    }

    @Override
    public Function<GameManager, Board> createBoardFactory() {
        return TTTBoard::new;
    }

    @Override
    public Color getBoardBackgroundColor() {
        return colors;
    }

    @Override
    public List<URL> getBoardPieceIcons() {
        return boardPieceIcons;
    }

    @Override
    public String[] getBoardPieceNames() {
        return boardPieceNames;
    }

    @Override
    public String getGameName() {
        return gameName;
    }

    @Override
    public boolean showPiecesCount() {
        return showPiecesCount;
    }
}
