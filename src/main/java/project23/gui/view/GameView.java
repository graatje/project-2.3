package project23.gui.view;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import project23.framework.BoardState;
import project23.framework.ConfigData;
import project23.framework.board.Board;
import project23.framework.board.BoardPiece;
import project23.framework.player.Player;
import project23.gui.controller.Controller;
import project23.gui.model.GameModel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GameView extends View<GameModel> {

    private final Pane gameBoardPane;
    private List<URL> playerIconFileURLs;
    private final Text waitingText;

    // Cell margin value between 0 (no margin) and 1 (no space for the piece at all)
    public static final double MARGIN = 0.2;

    public GameView(Parent parent, Controller controller, int windowWidth, int windowHeight) {
        super(parent, controller, windowWidth, windowHeight);
        gameBoardPane = (Pane) lookup("#board");
        this.waitingText = new Text("Please wait for the game to start.");
    }

    public void setBoardPieceIcons(List<URL> playerIconFileURLs) {
        this.playerIconFileURLs = playerIconFileURLs;
    }

    @Override
    public void update(GameModel model) {
        // Clock
//        if(model.resetClock()) {
//            //do stuff
//        }

        showDialog(model.getDialogMessage(), model.getDialogTitle());
        showInfoText(model.getInfoMessage(), model.getLabelNode());

        // If online match is waiting, show "waiting for game"
        if (model.getBoard().getBoardState() == BoardState.WAITING && !ConfigData.getInstance().getCurrentGame().isOnline()) {
            setBackgroundColorBoard(null);
            clearBoard();
            gameBoardPane.getChildren().add(waitingText);
        } else {
            drawBoard(model);
        }
    }

    private void drawBoard(GameModel model) {
        Board board = model.getBoard();
        if (board == null) {
            clearBoard();
            return;
        }

        setBackgroundColorBoard(ConfigData.getInstance().getCurrentGame().getBoardBackgroundColor());
        setBoardPieceIcons(ConfigData.getInstance().getCurrentGame().getBoardPieceIcons());


        // Player stats
        showPlayerInformation(model.getPlayerInfo(board.piecesCount()));

        // Draw board, pieces and hints
        int gridSize = board.getWidth();
        drawLines(gridSize);

        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                drawPiece(board.getBoardPiece(x, y), gridSize);
            }
        }

        Player player = board.getCurrentPlayer();
        if (player != null && board.isShowValidMoves() && player.isShowValidMoves()) {
            drawValidMoves(board.getValidMoves(), gridSize);
        }
    }

    private void drawValidMoves(List<BoardPiece> validMoves, int gridSize) {
        double cellSize = gameBoardPane.getPrefWidth() / gridSize;

        for (BoardPiece piece : validMoves) {
            double boardX = piece.getX() * cellSize + cellSize / 2;
            double boardY = piece.getY() * cellSize + cellSize / 2;

            Circle boardHighlight = new Circle(boardX, boardY, cellSize / 2 * (1 - MARGIN));

            boardHighlight.setFill(null);
            boardHighlight.setStroke(Color.rgb(200, 200, 200, 0.5));
            boardHighlight.setStrokeWidth(1.0);

            gameBoardPane.getChildren().add(boardHighlight);
        }
    }

    public void drawLines(int gridSize) {
        // Clear board
        clearBoard();

        double boardSize = gameBoardPane.getPrefWidth();

        // 3 gridboxes => 2 lines 1|2|3
        for (int i = 0; i < gridSize - 1; i++) {
            double pos = (double) (i + 1) / gridSize * boardSize;

            Line horizontalDivider = new Line();
            horizontalDivider.setStartX(pos);
            horizontalDivider.setStartY(0.0f);
            horizontalDivider.setEndX(pos);
            horizontalDivider.setEndY(boardSize);

            Line verticalDivider = new Line();
            verticalDivider.setStartX(0.0f);
            verticalDivider.setStartY(pos);
            verticalDivider.setEndX(boardSize);
            verticalDivider.setEndY(pos);

            gameBoardPane.getChildren().add(horizontalDivider);
            gameBoardPane.getChildren().add(verticalDivider);
        }
    }

    public void clearBoard() {
        gameBoardPane.getChildren().clear();
    }

    public void drawPiece(BoardPiece piece, int gridSize) {
        if (!piece.hasOwner()) {
            return;
        }

        int x = piece.getX();
        int y = piece.getY();
        double cellSize = gameBoardPane.getPrefWidth() / gridSize;

        // Hoop dat er geen outofbounds komt! lol
        URL pngURL = playerIconFileURLs.get(piece.getOwner().getID());

        // Create image
        Image pieceImage;
        try {
            pieceImage = new Image(pngURL.openStream(), cellSize * (1 - MARGIN), cellSize * (1 - MARGIN), true, true);
        } catch (IOException e) {
            System.err.println("Image does not exist?");
            e.printStackTrace();
            return; // stop, image not found
        }

        // Draw
        ImageView imageView = new ImageView(pieceImage);
        imageView.setX(cellSize * (x + MARGIN / 2));
        imageView.setY(cellSize * (y + MARGIN / 2));
        gameBoardPane.getChildren().add(imageView);
    }

    public void showPlayerInformation(ArrayList<String> playerInformationList) {
        HBox playerInformationHBox = (HBox) lookup("#playerInformationHBox");

        playerInformationHBox.getChildren().clear();

        for (String playerInformation : playerInformationList) {
            Label playerInformationLabel = new Label(playerInformation);
            playerInformationLabel.setId("playerInformationLabel");
            playerInformationHBox.getChildren().add(playerInformationLabel);
        }
    }

    public void setBackgroundColorBoard(Color color) {
        gameBoardPane.setBackground(new Background(new BackgroundFill(color, null, null)));
    }
}