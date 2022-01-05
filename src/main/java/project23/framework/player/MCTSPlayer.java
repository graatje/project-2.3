package project23.framework.player;

import project23.framework.ConfigData;
import project23.framework.ConnectedGameManager;
import project23.framework.board.Board;
import project23.framework.board.BoardObserver;
import project23.framework.board.BoardPiece;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import project23.util.Logger;
import static java.lang.System.currentTimeMillis;

public abstract class MCTSPlayer extends AIPlayer implements BoardObserver {
    /**
     * This algorithm works as following:
     * (a) get all possible moves for current player
     * (b) chose one of those at random
     * (c) alternately play the game with totally random (valid) moves until the game is over.
     * (d) value the game result (e) add this value to the totalscore of the move chosen in step (b)
     * (f) add 1 to the number of visits for the move chosen in step (b)
     * (g) jump to (b) if there is time left
     * (h) make the move with highest average score (totalscore/number of visits)
     * https://stackoverflow.com/questions/5443730/ideas-for-simple-and-useful-ai-for-othello-game-aka-reversi
     * @param board
     * @param id
     */
    public MCTSPlayer(Board board, int id) {
        super(board, id);
        board.registerObserver(this);
    }

    public MCTSPlayer(Board board, int id, String name) {
        super(board, id, name);
        board.registerObserver(this);
    }

    protected abstract float evaluateBoard(Board board);

    @Override
    public void onPlayerMoved(Player who, BoardPiece where) {

    }

    @Override
    public void onPlayerMoveFinalized(Player previous, Player current) {

    }

    @Override
    public void onPlayerWon(Player who) {

    }

    @Override
    public void onGameStart(Player startingPlayer) {

    }

    @Override
    public void requestMove() {
        BoardPiece move = monteCarlo(board, this);
        board.makeMove(this, move);
        if(move != null) {
            Logger.info("found best move " + move);
        }
        else{
            Logger.info("move was null.");
        }
    }

    static class ResultSet{
        BoardPiece boardPiece;
        float boardValue;
        public ResultSet(BoardPiece boardPiece, float boardValue){
            this.boardPiece = boardPiece;
            this.boardValue = boardValue;
        }
    }

    private BoardPiece monteCarlo(Board _board, Player player){
        long starttime = currentTimeMillis();
        List<BoardPiece> validMoves = _board.getValidMoves(player);
        HashMap<BoardPiece, ArrayList<Float>> vals = new HashMap<BoardPiece, ArrayList<Float>>();
        for(BoardPiece boardPiece: validMoves){
            vals.put(boardPiece, new ArrayList<>());
        }
        // ending early if only 1 or 0 moves available.
        if(validMoves.size() == 1){
            return validMoves.get(0);
        }
        else if(validMoves.size() == 0){
            return null;
        }
        Board board;
        while(currentTimeMillis() < starttime + ConfigData.getInstance().getMinimaxThinkingTime() - 100){
            try {
                board = _board.clone();
                board.setDisableRequestMove(true);
                ResultSet game = simulateGame(board);
                if(game != null) {
                    vals.get(game.boardPiece).add(game.boardValue);
                }
                else{
                    Logger.warning("resultset of the game was null");
                }
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                return null;
            }
        }
        int simulatedGames = 0;
        BoardPiece move = null;
        float bestavgval = Float.NEGATIVE_INFINITY;
        float tempcount;
        for(BoardPiece boardPiece: _board.getValidMoves(this)){
            simulatedGames += vals.get(boardPiece).size();
            tempcount = 0;
            for(float val: vals.get(boardPiece)){
                tempcount += val;
            }
            if(tempcount / vals.get(boardPiece).size() > bestavgval){
                bestavgval = tempcount / vals.get(boardPiece).size();
                move = boardPiece;
            }
        }
        if(move != null) {
            Logger.info("found move at x:" + move.getX() + ", y:" + move.getY() + "with a value of " + bestavgval +
                    " after simulating " + simulatedGames + " games.");
        }
        return move;
    }

    /**
     * simulate a game.
     * @param board the board to simulate with.
     * @return a resultset containing the boardpiece that was moved and the value of the board at the end of the game.
     */
    private ResultSet simulateGame(Board board){
        // picking the random move to simulate the rest of the game with.
        BoardPiece move = getRandomMove(board.getValidMoves(this));
        if(move == null){
            return null;
        }
        while(!board.calculateIsGameOver()){
            BoardPiece randomMove = getRandomMove(board.getValidMoves());
            board.makeMove(board.getCurrentPlayer(), randomMove);
        }
        return new ResultSet(move, evaluateBoard(board));
    }

    private BoardPiece getRandomMove(List<BoardPiece> validMoves){
        if(!validMoves.isEmpty()) {
            return validMoves.get((int) (Math.random() * validMoves.size()));
        }
        return null;
    }

    @Override
    public boolean isShowValidMoves() {
        return (board.getGameManager() instanceof ConnectedGameManager);
    }
}
