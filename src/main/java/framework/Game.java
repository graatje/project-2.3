package framework;

import framework.board.Board;
import framework.player.LocalPlayer;
import framework.player.Player;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.function.Function;

/**
 * Returns relevant Board-objects, Player-objects (and more?)
 */
public abstract class Game {

    private List<URL> boardPieceIcons;
    private boolean online = false;

    public static final String AI_NAME  = "Computer";

    public abstract Function<Board, Player> createAIPlayerFactory();
    public Function<Board, Player> createLocalPlayerFactory() {
        return (board -> new LocalPlayer(board, ConfigData.getInstance().getPlayerName()));
    }
    public abstract Function<GameManager, Board> createBoardFactory();
    private String helpText;

    /**
     * creates a GameManager.
     * @return the gameManger
     */
    public GameManager createGameManager() {
        if(online) {
            try {
                ConnectedGameManager cgm = new ConnectedGameManager(
                        createBoardFactory(),
                        ConfigData.getInstance().getServerIP(),
                        ConfigData.getInstance().getServerPort(),
                        createAIPlayerFactory()
                );

                cgm.setSelfName(ConfigData.getInstance().getPlayerName());
                cgm.login();

                return cgm;
            } catch (IOException e) {
                System.err.println("Couldn't connect to server!");
                e.printStackTrace();
            }

            return null;
        } else {
            return new GameManager(
                    createBoardFactory(),
                    createLocalPlayerFactory(),
                    createAIPlayerFactory()
            );
        }
    }

    public abstract Color getBoardBackgroundColor();
    public abstract List<URL> getBoardPieceIcons();
    public abstract String[] getBoardPieceNames();
    public abstract String getGameName();
    public abstract boolean showPiecesCount();

    // Aanroepen in GameMenu (lobby of lokale wedstrijd)
    public void setOnline(boolean isOnline) {
        this.online = isOnline;
    }
    public boolean isOnline() {
        return online;
    }


    public String getHelpText() {
        if(helpText==null) {
            loadHelpFile();
        }
        return helpText;
    }

    // Moet aangeroepen worden door child-klassen
    protected void loadHelpFile() {
        StringBuilder helpTextBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/helpfiles/"+getGameName() + "-help.txt")))) {
            String line;
            while((line = reader.readLine()) != null) {
                helpTextBuilder.append(line+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        helpText = helpTextBuilder.toString();
    }
}
