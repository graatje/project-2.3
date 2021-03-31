package framework.board;

@FunctionalInterface
public interface BoardObserver {
    /**
     * A handler for when the board state gets updated.
     */
    void boardUpdated();
}
