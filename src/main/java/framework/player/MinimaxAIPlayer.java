package framework.player;

import framework.board.Board;
import framework.board.BoardObserver;
import framework.board.BoardPiece;

import java.util.*;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class MinimaxAIPlayer extends AIPlayer implements BoardObserver {
    private static final int THINK_TIMEOUT = 2500;
    private static final int NUM_THREADS = 8;

    private AIDifficulty difficulty;

    // TODO: Make this a tree structure maybe?
    // Note that Sets have an O(1) insertion and remove time, which is REALLY needed in this algorithm (lists of 4gb+ in memory)
    private final Set<MoveOutcome> moveOutcomesSet = new HashSet<>();

    private final SynchronousQueue<MoveOutcome> workerQueue = new SynchronousQueue<>();
    private final List<Thread> workerThreads = new ArrayList<>();
    private final AtomicInteger workerThreadsWorking = new AtomicInteger(0);
    private boolean workerThreadsRunning = false;

    private final Object currentSessionLock = new Object();
    private UUID currentSession = null;

    private final Object evaluateLock = new Object();
    private float bestMoveValue;
    private Move bestMove;
    private int highestDepth;

    public MinimaxAIPlayer(Board board, String name, AIDifficulty difficulty) {
        super(board, name);

        this.difficulty = difficulty;

        // Register self as observer
        board.registerObserver(this);
    }

    public MinimaxAIPlayer(Board board, AIDifficulty difficulty) {
        super(board);

        this.difficulty = difficulty;

        // Register self as observer
        board.registerObserver(this);
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

    private void executeMinimaxMove() {
        UUID session = UUID.randomUUID();
        synchronized (currentSessionLock) {
            currentSession = session;
        }

        synchronized (evaluateLock) {
            bestMoveValue = Float.MIN_VALUE;
            bestMove = null;
            highestDepth = 0;
        }

        synchronized (moveOutcomesSet) {
            moveOutcomesSet.clear();
        }

        // Prime the moveOutcomes and doneMoveOutcomes maps.
        for(BoardPiece validMove : board.getValidMoves()) {
            Move move = new Move(validMove.getX(), validMove.getY());

            MoveOutcome outcome = calculateFirstOutcome(board, move);
            evaluateOutcome(outcome);
        }

        new Thread(() -> {
            while(true) {
                boolean didAnything = calculateNextDepth(session);
                if(!didAnything) {
                    synchronized (workerThreadsWorking) {
                        if(workerThreadsWorking.get() == 0) {
                            break;
                        }
                    }
                }
            }

            // Clear outcomes immediately to save RAM!
            synchronized (moveOutcomesSet) {
                moveOutcomesSet.clear();
            }

            float bestMoveValue;
            Move bestMove;
            int highestDepth;
            synchronized (evaluateLock) {
                bestMoveValue = this.bestMoveValue;
                bestMove = this.bestMove;
                highestDepth = this.highestDepth;
            }

            System.out.println("No more outcomes to check (we've either checked all outcomes, or the session expired), and all worker threads are idle!");
            System.out.println("Done thinking of the next move :)");
            System.out.println("bestMoveValue = " + bestMoveValue);
            System.out.println("bestMove = " + bestMove);
            System.out.println("highestDepth = " + highestDepth);

            if(bestMove == null) {
                board.makeMove(this, null);
            }else{
                board.makeMove(this, bestMove.x, bestMove.y);
            }

            // Make sure the session ended
            synchronized (currentSessionLock) {
                if(currentSession == session) {
                    currentSession = null;
                }
            }
        }, "AI-WorkerMaster").start();

        // TODO: This check can be done at every place we're checking the currentSession!
        new Thread(() -> {
            try{
                Thread.sleep(THINK_TIMEOUT);
            }catch(InterruptedException ignored) {}

            synchronized (currentSessionLock) {
                if(currentSession == session) {
                    System.out.println("After AI timeout, we're still working on the best move. Aborting early / making the session expire!");
                    currentSession = null;
                }
            }
        }, "AI-Supervisor").start();
    }

    private boolean calculateNextDepth(UUID session) {
        synchronized (currentSessionLock) {
            if(session != currentSession) {
                return false;
            }
        }

        Set<MoveOutcome> moveOutcomesSet_copy;
        synchronized (moveOutcomesSet) {
            moveOutcomesSet_copy = new HashSet<>(moveOutcomesSet);
        }

        for(MoveOutcome moveOutcome : moveOutcomesSet_copy) {
            synchronized (currentSessionLock) {
                if(session != currentSession) {
                    return false;
                }
            }

            synchronized (moveOutcomesSet) {
                moveOutcomesSet.remove(moveOutcome);
            }

            try {
                workerQueue.put(moveOutcome);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return !moveOutcomesSet_copy.isEmpty();
    }

    private void workerThread() {
        System.out.println("AI worker thread started.");

        while(workerThreadsRunning) {
            MoveOutcome moveOutcome;
            try{
                moveOutcome = workerQueue.take();
            }catch(InterruptedException e) {
                // Continue, to check the while-loop condition again!
                continue;
            }

            synchronized (workerThreadsWorking) {
                workerThreadsWorking.incrementAndGet();
            }

            for(BoardPiece validMove : moveOutcome.board.getValidMoves()) {
                Move move = new Move(validMove.getX(), validMove.getY());

                MoveOutcome nextOutcome = calculateNextOutcome(moveOutcome, move);
                evaluateOutcome(nextOutcome);
            }

            synchronized (workerThreadsWorking) {
                workerThreadsWorking.decrementAndGet();
            }
        }

        System.out.println("AI worker thread stopped.");
    }

    private void startWorkerThreads() {
        if(!workerThreads.isEmpty()) {
            throw new IllegalStateException("Worker threads already started!");
        }

        workerThreadsRunning = true;

        for (int i = 0; i < NUM_THREADS; i++) {
            Thread thread = new Thread(this::workerThread, "AI-Worker-" + i);
            thread.setDaemon(true);

            workerThreads.add(thread);
            thread.start();
        }
    }

    private void stopWorkerThreads() {
        workerThreadsRunning = false;

        for(Thread workerThread : workerThreads) {
            workerThread.interrupt();
        }

        workerThreads.clear();
    }

    private void evaluateOutcome(MoveOutcome outcome) {
        synchronized (evaluateLock) {
            if(outcome.boardValue > bestMoveValue) {
                bestMoveValue = outcome.boardValue;
                bestMove = outcome.firstMove;
            }

            if(outcome.depth > highestDepth) {
                highestDepth = outcome.depth;
            }
        }

        if(!outcome.done) {
            synchronized (moveOutcomesSet) {
                moveOutcomesSet.add(outcome);
            }
        }
    }

    private MoveOutcome calculateNextOutcome(MoveOutcome previous, Move move) {
        Board board;
        try{
            board = previous.board.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
            System.exit(-1);
            return null;
        }
        board.setDisableRequestMove(true);

        board.makeMove(board.getCurrentPlayer(), move.x, move.y);

        float value = evaluateBoard(board, previous.depth + 1);
        boolean done = board.calculateIsGameOver();

        return new MoveOutcome(previous.firstMove, board, value, done, previous.depth + 1);
    }

    private MoveOutcome calculateFirstOutcome(Board _board, Move move) {
        Board board;
        try{
            board = _board.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
            System.exit(-1);
            return null;
        }
        board.setDisableRequestMove(true);

        board.makeMove(board.getCurrentPlayer(), move.x, move.y);

        float value = evaluateBoard(board, 1);
        boolean done = board.calculateIsGameOver();

        return new MoveOutcome(move, board, value, done, 1);
    }

    protected abstract float evaluateBoard(Board board, int treeDepth);

    @Override
    public void onPlayerMoved(Player who, BoardPiece where) {}

    @Override
    public void onPlayerMoveFinalized(Player previous, Player current) {}

    @Override
    public void onGameStart(Player startingPlayer) {
        if(!workerThreads.isEmpty()) {
            System.err.println("Received onGameStart observation, but we already have worker threads running. Ignoring.");
            return;
        }

        startWorkerThreads();
    }

    @Override
    public void onPlayerWon(Player who) {
        stopWorkerThreads();
    }

    public AIDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(AIDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    private static class Move {
        public final int x, y;

        public Move(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Move{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    private static class MoveOutcome {
        public final Move firstMove;
        public final Board board;
        public final float boardValue;
        public final boolean done;
        public final int depth;

        public MoveOutcome(Move firstMove, Board board, float boardValue, boolean done, int depth) {
            this.firstMove = firstMove;
            this.board = board;
            this.boardValue = boardValue;
            this.done = done;
            this.depth = depth;
        }
    }

    public enum AIDifficulty {
        EASY,
        MEDIUM,
        HARD,
    }
}
