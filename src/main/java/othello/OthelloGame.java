package othello;

import framework.ConfigData;
import framework.Game;
import framework.GameManager;
import framework.board.Board;
import framework.player.Player;
import javafx.scene.paint.Color;
import othello.board.OthelloBoard;
import othello.player.OthelloMinimaxAIPlayer;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class OthelloGame extends Game {

    private String[] boardPieceNames = { "Black", "White" };
    private List<URL> boardPieceIcons = Arrays.asList(getClass().getResource("/boardPieces/othello_black.png"), getClass().getResource("/boardPieces/othello_white.png"));
    private boolean showPiecesCount = true;
    private Color colors = Color.rgb(0, 153, 0);
    private String gameName = "Reversi";
    private boolean showValidMoves = true;

    @Override
    public Function<Board, Player> createAIPlayerFactory() {
        return (b) -> new OthelloMinimaxAIPlayer(b, Game.AI_NAME, ConfigData.getInstance().getAIDifficulty());
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
    public String getGameName() {
        return gameName;
    }

    @Override
    public boolean showPiecesCount() {
        return showPiecesCount;
    }

    @Override
    public boolean showValidMoves() {
        return showValidMoves;
    }
}
