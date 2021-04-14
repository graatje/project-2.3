package framework.player;

import framework.ConfigData;
import framework.ConnectedGameManager;
import framework.board.Board;
import framework.board.BoardObserver;
import framework.board.BoardPiece;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class MinimaxAIPlayer extends AIPlayer implements BoardObserver {
    private AIDifficulty difficulty;

    private final Object minimaxSessionLock = new Object();
    private UUID minimaxSession;

    private final Object bestMoveLock = new Object();
    private BoardPiece bestMove;
    private float bestMoveValue;

    private final AtomicBoolean anyEndedInNonGameOver = new AtomicBoolean();
    private final AtomicInteger highestDepth = new AtomicInteger();
    private final Set<UUID> runningThreads = new HashSet<>();

    public MinimaxAIPlayer(Board board, int id, String name, AIDifficulty difficulty) {
        super(board, id, name);

        this.difficulty = difficulty;
        board.registerObserver(this);
    }

    public MinimaxAIPlayer(Board board, int id, AIDifficulty difficulty) {
        super(board, id);

        this.difficulty = difficulty;
        board.registerObserver(this);
    }

    protected abstract float evaluateBoard(Board board, int treeDepth);

    public abstract int getStartDepth();

    @Override
    public boolean isShowValidMoves() {
        return (board.getGameManager() instanceof ConnectedGameManager);
    }

    @Override
    public void requestMove() {
        List<BoardPiece> validMoves = board.getValidMoves(this);
        if(validMoves.size() == 0) {
            board.makeMove(this, null);
            return;
        }else if(validMoves.size() == 1) {
            board.makeMove(this, validMoves.get(0));
            return;
        }

        switch (difficulty) {
            case EASY:
                executeRandomMove();
                break;
            case MEDIUM:
                if (Math.random() > 0.5) {
                    executeRandomMove();
                } else {
                    executeMinimaxMove();
                }
                break;
            case HARD:
                executeMinimaxMove();
                break;
            default:
                throw new IllegalStateException("Invalid AI difficulty '" + difficulty + "'!");
        }
    }

    public void executeRandomMove() {
        List<BoardPiece> validMoves = board.getValidMoves(this);

        BoardPiece randomMove = null;
        if (!validMoves.isEmpty()) {
            randomMove = validMoves.get((int) (Math.random() * validMoves.size()));
        }

        board.makeMove(this, randomMove);
    }

    /**
     * check all valid moves. return the best move of those.
     *
     * @return the boardpiece with the best move.
     */
    public void executeMinimaxMove() {
        UUID session = UUID.randomUUID();
        synchronized (minimaxSessionLock) {
            minimaxSession = session;
        }

        synchronized (bestMoveLock) {
            bestMove = null;
            bestMoveValue = Float.NEGATIVE_INFINITY;
        }

        synchronized (highestDepth) {
            highestDepth.set(0);
        }

        performAsyncMinimax(session, getStartDepth());

        Thread watchdogThread = new Thread(() -> {
            try {
                Thread.sleep(ConfigData.getInstance().getMinimaxThinkingTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            onMinimaxDone(session);
        });
        watchdogThread.setDaemon(true);
        watchdogThread.start();
    }

    private void onMinimaxDone(UUID session) {
        synchronized (minimaxSessionLock) {
            if(minimaxSession != session) {
                return;
            }

            minimaxSession = null;
        }

        BoardPiece bestMove;
        float bestMoveValue;
        synchronized (bestMoveLock) {
            bestMove = this.bestMove;
            bestMoveValue = this.bestMoveValue;
        }

        List<BoardPiece> validMoves = board.getValidMoves(this);
        if(!validMoves.isEmpty()) {
            if(bestMove == null) {
                System.err.println("Minimax couldn't come up with a best move, but there are more than 0 valid moves! Sending a random move..");
                bestMove = validMoves.get((int) (Math.random() * validMoves.size()));
            }

            if(!validMoves.contains(bestMove)) {
                System.err.println("Minimax came up with a best move, but it isn't a valid move! Sending a random move..");
                bestMove = validMoves.get((int) (Math.random() * validMoves.size()));
            }
        }

        int highestDepthValue;
        synchronized (highestDepth) {
            highestDepthValue = highestDepth.get();
        }

        boolean anyEndedInNonGameOverValue;
        synchronized (anyEndedInNonGameOver) {
            anyEndedInNonGameOverValue = anyEndedInNonGameOver.get();
        }

        System.out.println("Found best move " + bestMove + " with a value of " + bestMoveValue + " at a depth of " + highestDepthValue + ".");

        System.out.print("We are ");
        if(anyEndedInNonGameOverValue) {
            if(Math.abs(bestMoveValue) > 1.0f) {
                System.out.print("most likely ");
            }else{
                System.out.print("probably ");
            }
        }else{
            System.out.print("definitely ");
        }

        System.out.print("going to ");

        if(bestMoveValue > 0) {
            System.out.print("win ");
        }else if(bestMoveValue == 0) {
            System.out.print("tie ");
        }else{
            System.out.print("lose ");
        }

        System.out.println("if the opponent plays perfectly.");

        board.makeMove(this, bestMove);
    }

    private void performAsyncMinimax(UUID session, int depth) {
        synchronized (minimaxSessionLock) {
            if(minimaxSession != session) {
                return;
            }
        }

        synchronized (runningThreads) {
            // Just to be sure!
            runningThreads.clear();
        }

        synchronized (anyEndedInNonGameOver) {
            anyEndedInNonGameOver.set(false);
        }

        List<BoardPiece> validMoves = board.getValidMoves(this);

        for (BoardPiece boardPiece : validMoves) {
            int x = boardPiece.getX();
            int y = boardPiece.getY();

            final UUID threadUuid = UUID.randomUUID();
            synchronized (runningThreads) {
                runningThreads.add(threadUuid);
            }

            Thread thread = new Thread(() -> {
                synchronized (minimaxSessionLock) {
                    if (minimaxSession != session) {
                        return;
                    }
                }

                float moveValue = miniMax(session, board, depth, this, x, y);

                synchronized (bestMoveLock) {
                    if (moveValue > bestMoveValue) {
                        bestMove = boardPiece;
                        bestMoveValue = moveValue;
                    }
                }

                boolean isEmpty;
                synchronized (runningThreads) {
                    runningThreads.remove(threadUuid);
                    isEmpty = runningThreads.isEmpty();
                }

                if (isEmpty) {
                    // We're DONE!
                    synchronized (minimaxSessionLock) {
                        if(minimaxSession != session) {
                            return;
                        }
                    }

                    synchronized (highestDepth) {
                        if(depth > highestDepth.get()) {
                            highestDepth.set(depth);
                        }
                    }

                    boolean anyEndedInNonGameOverValue;
                    synchronized (anyEndedInNonGameOver) {
                        anyEndedInNonGameOverValue = anyEndedInNonGameOver.get();
                    }

                    if(anyEndedInNonGameOverValue) {
                        // We can still go higher!
                        System.out.println("Done with minimax at a depth of " + depth + ", but we still have time. Going deeper!");
                        performAsyncMinimax(session, depth + 1);
                    }else{
                        System.out.println("All minimax ends ended in a game-over. Aborting early at a depth of " + depth + "!");
                        onMinimaxDone(session);
                    }
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
    }

    /**
     * returns the highest value move when the end is reached because either a lack of valid moves,
     * the end of a node or the maximum search depth is reached.
     *
     * @param _board a playing board.
     * @param depth depth of the nodes to look into.
     * @return int value of the board.
     */
    private float miniMax(UUID session, Board _board, int depth, Player player, int moveX, int moveY) {
        synchronized (minimaxSessionLock) {
            if (minimaxSession != session) {
                return 0;
            }
        }

        // Clone the board
        Board board;
        try {
            board = _board.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return 0;
        }
        board.setDisableRequestMove(true);

        // Execute the move
        if(moveX == -1 && moveY == -1) {
            board.makeMove(player, null);
        }else{
            board.makeMove(player, moveX, moveY);
        }

        boolean gameOver = board.calculateIsGameOver();
        if (depth == 0 || gameOver) {
            // end reached.
            if(!gameOver) {
                synchronized (anyEndedInNonGameOver) {
                    anyEndedInNonGameOver.set(true);
                }
            }

            return evaluateBoard(board, depth);
        }

        Player playerToMove = board.getCurrentPlayer();
        boolean lookForMax = playerToMove == this;
        float extremeVal = lookForMax ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;

        List<BoardPiece> validMoves = board.getValidMoves(playerToMove);
        if(validMoves.isEmpty()) { /* && board.canPass(playerToMove) */
            validMoves.add(null);
        }

        for (BoardPiece _boardPiece : validMoves) {
            int x, y;
            if(_boardPiece != null) {
                x = _boardPiece.getX();
                y = _boardPiece.getY();
            }else{
                x = y = -1;
            }

            float val = miniMax(session, board, depth - 1, playerToMove, x, y);

            if (lookForMax) {
                if (val > extremeVal) extremeVal = val;
            } else {
                if (val < extremeVal) extremeVal = val;
            }
        }

        return extremeVal;
    }

    public AIDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(AIDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public void onPlayerMoved(Player who, BoardPiece where) {}

    @Override
    public void onPlayerMoveFinalized(Player previous, Player current) {}

    @Override
    public void onPlayerWon(Player who) {
        synchronized (minimaxSessionLock) {
            minimaxSession = null;
        }
    }

    @Override
    public void onGameStart(Player startingPlayer) {}


    public enum AIDifficulty {
        EASY,
        MEDIUM,
        HARD;

        public String displayName() {
            return String.valueOf(name().charAt(0)).toUpperCase() + name().substring(1).toLowerCase();
        }

        public static AIDifficulty fromDisplayName(String name) {
            try{
                return valueOf(name.toUpperCase());
            }catch(IllegalArgumentException ignored) {}

            return null;
        }
    }
}
