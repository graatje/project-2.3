package framework.board;

@FunctionalInterface
public interface BoardObserver {
    void boardUpdated();
}
