package framework.player;

import framework.board.Board;
import framework.board.BoardPiece;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MinimaxAIPlayer extends AIPlayer {
    private int startDepth;
    private AIDifficulty difficulty;

    private UUID minimaxSession;
    private BoardPiece bestMove;
    private int bestValue;
    private final Set<UUID> runningThreads = new HashSet<>();

    public MinimaxAIPlayer(Board board, String name, int startDepth, AIDifficulty difficulty) {
        super(board, name);

        this.startDepth = startDepth;
        this.difficulty = difficulty;
    }

    public MinimaxAIPlayer(Board board, int startDepth, AIDifficulty difficulty) {
        super(board);

        this.startDepth = startDepth;
        this.difficulty = difficulty;
    }

    @Override
    public void requestMove() {
        List<BoardPiece> validMoves = board.getValidMoves(this);
        if(validMoves.size() == 0) {
            board.makeMove(this, null);
        }else if(validMoves.size() == 1) {
            board.makeMove(this, validMoves.get(0));
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
            bestValue = Integer.MIN_VALUE;
            minimaxSession = session;
        }

        performAsyncMinimax(session, this.startDepth);

        new Thread(() -> {
            try {
                Thread.sleep(9000);
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

        board.makeMove(this, bestMove);
    }

    private void performAsyncMinimax(UUID session, int depth) {
        synchronized (this) {
            if (minimaxSession != session) {
                return;
            }
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
                Board clonedBoard;
                try {
                    clonedBoard = board.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                    return;
                }

                clonedBoard._executeMove(this, clonedBoard.getBoardPiece(x, y));
                int moveValue = miniMax(session, clonedBoard, depth, board.getGameManager().getOtherPlayer(this));

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
                        performAsyncMinimax(session, depth + 1);
                    }
                }
            }).start();
        }
    }

    /**
     * returns the highest value move when the end is reached because either a lack of valid moves,
     * the end of a node or the maximum search depth is reached.
     *
     * @param board a playing board.
     * @param depth depth of the nodes to look into.
     * @param playerToMove the player to move. If playerToMove == this is true, the algorithm will maximise, otherwise minimise.
     * @return int value of the board.
     */
    private int miniMax(UUID session, Board board, int depth, Player playerToMove) {
        synchronized (this) {
            if (minimaxSession != session) {
                return 0;
            }
        }

        int boardVal = evaluateBoard(board, depth);

        Player other = board.getGameManager().getOtherPlayer(playerToMove);
        boolean lookForMax = playerToMove == this;

        if (Math.abs(boardVal) > 0 || depth == 0 || board.getValidMoves(playerToMove).isEmpty()) {
            // end reached.
            return boardVal;
        }

        int extremeVal = lookForMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (BoardPiece boardPiece : board.getValidMoves(playerToMove)) {
            int x = boardPiece.getX();
            int y = boardPiece.getY();

            Board clonedBoard;
            try {
                clonedBoard = board.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                return 0;
            }

            clonedBoard._executeMove(playerToMove, clonedBoard.getBoardPiece(x, y));

            Player nextToMove;
            if(!board.getValidMoves(other).isEmpty()) {
                nextToMove = other;
            }else{
                nextToMove = playerToMove;
            }

            int val = miniMax(session, clonedBoard, depth - 1, nextToMove);

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
    private int evaluateBoard(Board board, int treeDepth) {
        Player winner = board.calculateWinner();

        if (winner == this) {
            // Win for self
            return 10 + treeDepth;
        } else if (winner != null) {
            // Win for other
            return -10 - treeDepth;
        } else {
            // Draw or no win
            return 0;
        }
    }

    public int getStartDepth() {
        return startDepth;
    }

    public void setStartDepth(int startDepth) {
        this.startDepth = startDepth;
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
