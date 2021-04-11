package gui.view;

import framework.BoardState;
import framework.ConfigData;
import framework.GameType;
import framework.board.BoardPiece;
import framework.player.LocalPlayer;
import gui.controller.Controller;
import gui.model.GenericGameModel;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GameView extends View<GenericGameModel> {

    private Pane gameBoardPane;
    private List<URL> playerIconFileURLs;
    private Text waitingText;

    // Cell margin value between 0 (no margin) and 1 (no space for the piece at all)
    public static final double MARGIN = 0.2;

    public GameView(Parent parent, Controller controller, int windowWidth, int windowHeight) {
        super(parent, controller, windowWidth, windowHeight);
        gameBoardPane = (Pane) lookup("#Board");
        this.waitingText = new Text("Please wait for the game to start.");
    }

    public void setPlayerIconFileURLs(List<URL> playerIconFileURLs) {
        this.playerIconFileURLs = playerIconFileURLs;
    }

    @Override
    /**
     * Draws board with tiles
     */
    public void update(GenericGameModel model) {
        // Show waiting message when game hasn't started
        System.out.println("Boardstate: "+model.getBoard().getBoardState());
        if(model.getBoard().getBoardState() == BoardState.WAITING) {
            setBackgroundColorBoard(null);
            clearBoard();
            gameBoardPane.getChildren().add(waitingText);
            return;
        }

        setBackgroundColorBoard(model.getBackgroundColor());
        setPlayerIconFileURLs(model.getPlayerIconFileURLs());

        //show username on board
        showPlayerInformation(model.getPlayerInfo(model.getBoard().piecesCount()));
        showDialog(model.getDialogMessage(), "Info");
        showInfoText(model.getInfoMessage(), model.getTextNode());
        int gridSize = model.getBoard().getWidth();
        drawBoard(gridSize);

        for(int x=0;x<gridSize;x++) {
            for(int y=0;y<gridSize;y++) {
                drawPiece(model.getBoard().getBoardPiece(x, y), gridSize);
            }
        }

        if(model.getBoard().getCurrentPlayer() instanceof LocalPlayer && ConfigData.getInstance().getGameType().toString().toLowerCase().contains("othello")) {
            drawValidMoves(model.getBoard().getValidMoves(), gridSize);
        }
    }

    private void drawValidMoves(List<BoardPiece> validMoves, int gridSize) {
        double cellSize = gameBoardPane.getPrefWidth()/gridSize;

        for(BoardPiece piece : validMoves) {
            double boardX = piece.getX()*cellSize+cellSize/2;
            double boardY = piece.getY()*cellSize+cellSize/2;

            Circle boardHighlight = new Circle(boardX, boardY, cellSize/2*(1-MARGIN));

            boardHighlight.setFill(null);
            boardHighlight.setStroke(Color.rgb(200, 200, 200, 0.5));
            boardHighlight.setStrokeWidth(1.0);

            gameBoardPane.getChildren().add(boardHighlight);
        }
    }

    public void drawBoard(int gridSize) {
        // Clear board
        clearBoard();

        double boardSize = gameBoardPane.getPrefWidth();

        // 3 gridboxes => 2 lines 1|2|3
        for(int i=0;i<gridSize-1;i++) {
            double pos = (double) (i+1)/gridSize*boardSize;

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
        if(!piece.hasOwner()) {
            return;
        }

        int x = piece.getX();
        int y = piece.getY();
        double cellSize = gameBoardPane.getPrefWidth()/gridSize;

        // Hoop dat er geen outofbounds komt! lol
        URL pngURL = playerIconFileURLs.get(piece.getOwner().getID());

        // Create image
        Image pieceImage;
        try {
            pieceImage = new Image(pngURL.openStream(), cellSize*(1-MARGIN), cellSize*(1-MARGIN), true, true);
        } catch(IOException e) {
            System.err.println("Image does not exist?");
            e.printStackTrace();
            return; // stop, image not found
        }

        // Draw
        ImageView imageView = new ImageView(pieceImage);
        imageView.setX(cellSize*(x+MARGIN/2));
        imageView.setY(cellSize*(y+MARGIN/2));
        gameBoardPane.getChildren().add(imageView);
    }

    public void showPlayerInformation(ArrayList<String> playerInformationList){
        HBox playerInformationHBox = (HBox) lookup("#playerInformationHBox");

        playerInformationHBox.getChildren().clear();

        for (String playerInformation : playerInformationList){
            playerInformationHBox.getChildren().add(new Text(playerInformation));
        }
    }

    public void setBackgroundColorBoard(ArrayList<Integer> colors){
        if (colors == null){
            gameBoardPane.setBackground(new Background(new BackgroundFill(null, null, null)));
        }else {
            gameBoardPane.setBackground(new Background(new BackgroundFill(Color.rgb(colors.get(0), colors.get(1), colors.get(2)), null, null)));
        }
    }
}