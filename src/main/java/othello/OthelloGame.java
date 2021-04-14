package othello;

import framework.ConfigData;
import framework.Game;
import framework.GameManager;
import framework.GameType;
import framework.board.Board;
import framework.player.Player;
import javafx.scene.paint.Color;
import othello.board.OthelloBoard;
import othello.player.OthelloMinimaxAIPlayer;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class OthelloGame extends Game {
    private final String[] boardPieceNames = { "Black", "White" };
    private final List<URL> boardPieceIcons = Arrays.asList(getClass().getResource("/images/boardPieces/othello_black.png"), getClass().getResource("/images/boardPieces/othello_white.png"));
    private final boolean showPiecesCount = true;
    private final Color colors = Color.rgb(0, 153, 0);

    @Override
    public BiFunction<Board, Integer, Player> createAIPlayerFactory() {
        return (board, id) -> new OthelloMinimaxAIPlayer(board, id, Game.AI_NAME, ConfigData.getInstance().getAIDifficulty());
    }

    @Override
    public Function<GameManager, Board> createBoardFactory() {
        return OthelloBoard::new;
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
    public GameType getGameType() {
        return GameType.OTHELLO;
    }

    @Override
    public boolean showPiecesCount() {
        return showPiecesCount;
    }
}
