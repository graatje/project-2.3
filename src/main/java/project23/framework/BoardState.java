package project23.framework;

public enum BoardState {
    /**
     * The game is currently not playing. The Board will be in this state
     * when it has just been created, or when it has been reset.
     */
    WAITING,

    /**
     * The game is currently being played.
     */
    PLAYING,

    /**
     * The game was being played, but has ended and thus the Board has a winner.
     */
    GAME_OVER,
}
