package gui.model;

public class GenericGameModel extends Model {
    private int gridSize = 3; //TODO: opvragen van framework
    private double boardSize = 500; //TODO: opvragen van fxml?

    public void clickTile(double x, double y) {
        double cellSize = boardSize/gridSize;
        int xTile = (int) Math.floor(x/cellSize);
        int yTile = (int) Math.floor(y/cellSize);

        //TODO: meegeven met framework
        System.out.println("DEBUG: x: "+xTile + "; y: "+ yTile);
    }
}
