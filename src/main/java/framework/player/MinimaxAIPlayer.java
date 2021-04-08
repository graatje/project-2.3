package framework.player;

import framework.board.Board;
import framework.board.BoardPiece;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MinimaxAIPlayer extends AIPlayer {
    private static final int START_DEPTH = 1;

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

        performAsyncMinimax(session, START_DEPTH);

        new Thread(() -> {
            try {
                Thread.sleep(2000);
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

    /**
     * Evaluate the given board from the perspective of the current player, return 10 if a
     * winning board configuration is found, -10 for a losing one and 0 for a draw,
     * weight the value of a win/loss/draw according to how many moves it would take
     * to realise it using the depth of the game tree the board configuration is at.
     *
     * @param treeDepth depth of the game tree the board configuration is at
     * @return value of the board
     */
    private float evaluateBoard(Board board, int treeDepth) {
        // TODO: Maybe add a game-specific AI implementation for MinimaxAIPlayer#evaluateBoard?
        // Eg. for Othello give borders more points

        int[][] board_value = {
            {20, -3, 11, 8,  8, 11, -3, 20},
            {-3, -7, -4, 1,  1, -4, -7, -3},
            {11, -4, 2,  2,  2,  2, -4, 11},
            { 8,  1, 2, -3, -3,  2,  1,  8},
            { 8,  1, 2, -3, -3,  2,  1,  8},
            {11, -4, 2,  2,  2,  2, -4, 11},
            {-3, -7, -4, 1,  1, -4, -7, -3},
            {20, -3, 11, 8,  8, 11, -3, 20}
        };

        int b = 0, w = 0;
        for(int x = 0; x < 8; x++) {
            for(int y = 0; y < 8; y++) {
                Player owner = board.getBoardPiece(x, y).getOwner();
                if(owner == this) {
                    b += board_value[x][y];
                }else if(owner != null) {
                    w += board_value[x][y];
                }
            }
        }

        float value = 0;
        if(b + w != 0) {
            value = (float) (b - w) / (b + w);
        }

        b += board.getValidMoves(this).size();
        w += board.getValidMoves(board.getGameManager().getOtherPlayer(this)).size();

        if(b + w != 0) {
            value += (float) (b - w) / (b + w);
        }

        return value;


//        if(board.calculateIsGameOver()) {
//            Player winner = board.calculateWinner();
//            if(winner == this) {
//                return 100;
//            }else if(winner != null) {
//                return -100;
//            }else{
//                return 0;
//            }
//        }
//
//        int nSelf = 0;
//        int nOther = 0;
//        for(int y = 0; y < board.getHeight(); y++) {
//            for(int x = 0; x < board.getWidth(); x++) {
//                BoardPiece piece = board.getBoardPiece(x, y);
//                if(!piece.hasOwner()) {
//                    continue;
//                }
//
//                boolean xBorder = (x == 0 || x == board.getWidth() - 1);
//                boolean yBorder = (y == 0 || y == board.getHeight() - 1);
//
//                int piecePoints;
//                if(xBorder && yBorder) {
//                    piecePoints = 20;
//                }else if(xBorder || yBorder) {
//                    piecePoints = 8;
//                }else{
//                    piecePoints = 1;
//                }
//
//                if(piece.getOwner() == this) {
//                    nSelf += piecePoints;
//                }else {
//                    nOther += piecePoints;
//                }
//            }
//        }
//
//        return nSelf - nOther;

//        Player winner = board.calculateWinner();
//        if (winner == this) {
//            // Win for self
//            return 10 + treeDepth;
//        } else if (winner != null) {
//            // Win for other
//            return -10 - treeDepth;
//        } else {
//            // Draw or no win
//            return 0;
//        }
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
