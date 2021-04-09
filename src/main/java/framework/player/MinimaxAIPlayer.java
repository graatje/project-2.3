package framework.player;

import framework.board.Board;
import framework.board.BoardPiece;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public abstract class MinimaxAIPlayer extends AIPlayer {
    private AIDifficulty difficulty;

    private UUID minimaxSession;
    private BoardPiece bestMove;
    private float bestValue;
    private boolean anyEndedInNonGameOver;
    private int highestDepth;
    private final Set<UUID> runningThreads = new HashSet<>();

    public MinimaxAIPlayer(Board board, String name, AIDifficulty difficulty) {
        super(board, name);

        this.difficulty = difficulty;
    }

    public MinimaxAIPlayer(Board board, AIDifficulty difficulty) {
        super(board);

        this.difficulty = difficulty;
    }

    protected abstract float evaluateBoard(Board board, int treeDepth);

    public abstract int getStartDepth();

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
        synchronized (this) {
            bestMove = null;
            bestValue = Float.MIN_VALUE;
            minimaxSession = session;
            highestDepth = 0;
        }

        performAsyncMinimax(session, getStartDepth());

        new Thread(() -> {
            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            onMinimaxDone(session);
        }).start();
    }

    private void onMinimaxDone(UUID session) {
        BoardPiece bestMove;
        synchronized (this) {
            if(minimaxSession != session) {
                return;
            }

            bestMove = this.bestMove;

            this.minimaxSession = null;
        }

        if (bestMove == null) {
            List<BoardPiece> validMoves = board.getValidMoves(this);
            if (!validMoves.isEmpty()) {
                System.err.println("Minimax couldn't come up with a best move, but there are more than 0 valid moves! Sending a random move..");
                bestMove = validMoves.get((int) (Math.random() * validMoves.size()));
            }
        }

        System.out.println("Figured out the best move at a depth of " + highestDepth);

        board.makeMove(this, bestMove);
    }

    private void performAsyncMinimax(UUID session, int depth) {
        synchronized (this) {
            if (minimaxSession != session) {
                return;
            }

            if(depth > highestDepth) {
                highestDepth = depth;
            }

            anyEndedInNonGameOver = false;
        }

        List<BoardPiece> validMoves = board.getValidMoves(this);

        for (BoardPiece boardPiece : validMoves) {
            int x = boardPiece.getX();
            int y = boardPiece.getY();

            final UUID threadUuid = UUID.randomUUID();
            synchronized (runningThreads) {
                runningThreads.add(threadUuid);
            }

            new Thread(() -> {
                synchronized (this) {
                    if (minimaxSession != session) {
                        return;
                    }
                }

                float moveValue = miniMax(session, board, depth, this, x, y);

                synchronized (this) {
                    if (moveValue > bestValue) {
                        bestMove = boardPiece;
                        bestValue = moveValue;
                    }
                }

                boolean isEmpty;
                synchronized (runningThreads) {
                    runningThreads.remove(threadUuid);
                    isEmpty = runningThreads.isEmpty();
                }

                if (isEmpty) {
                    // We're DONE!
                    if (minimaxSession == session) {
                        if(anyEndedInNonGameOver) {
                            // We can still go higher!
                            performAsyncMinimax(session, depth + 1);
                        }else{
                            System.out.println("All minimax ends ended in a game-over. Aborting early at a depth of " + depth + "!");
                            onMinimaxDone(session);
                        }
                    }
                }
            }).start();
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
        synchronized (this) {
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
                synchronized (this) {
                    anyEndedInNonGameOver = true;
                }
            }

            return evaluateBoard(board, depth);
        }

        Player playerToMove = board.getCurrentPlayer();
        boolean lookForMax = playerToMove == this;
        float extremeVal = lookForMax ? Float.MIN_VALUE : Float.MAX_VALUE;

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

    public enum AIDifficulty {
        EASY,
        MEDIUM,
        HARD,
    }
}
