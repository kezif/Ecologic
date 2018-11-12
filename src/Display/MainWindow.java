package Display;

import Generation.Terrain;
import Utils.Vector2d;
import javafx.fxml.FXML;
import javafx.scene.canvas.*;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;


public class MainWindow {

    @FXML
    public Canvas terCanvas;
    public Canvas gridCanvas;
    public Canvas pickCanvas;
    public Canvas sityCanvas;
    public Canvas calcCanvas;
    public Button calcButton;
    public Canvas pollutionCanvas;
    private int minGrid = 3;

    private Terrain terrain;

    @FXML
    private void initialize() {
        System.out.println("init");
        this.terrain = new Terrain((int) terCanvas.getWidth(), (int) terCanvas.getHeight(), (int) terCanvas.getWidth() / 20);
        RerollButtonPressed();
        CanvasGraphics.initializeGrid(gridCanvas, terrain.getGridSize());
        picked.setBoth(Integer.MAX_VALUE);
        System.out.println("done");
    }

    @FXML
    public void RerollButtonPressed() {
        long startTime = System.nanoTime(); //timer
        terrain.reroll();
        long endTime = System.nanoTime(); //timer
        long duration = (endTime - startTime); //timer
        System.out.println(String.format("generation in %d ms", duration / 1000000)); //timer
        startTime = System.nanoTime(); //timer
        CanvasGraphics.readProp();
        CanvasGraphics.drawHeightNwater(terCanvas, terrain);
        CanvasGraphics.drawSity(sityCanvas, terrain);
        endTime = System.nanoTime(); //timer
        duration = (endTime - startTime); //timer
        System.out.println(String.format("Draw in %d ms", duration / 1000000)); //timer
        System.out.println("---");
        resetPicked();
        /*highlightSquare(picked.x, picked.y);
        CanvasGraphics.clearCanvas(pollutionCanvas);*/
    }

    private void resetPicked(){
        picked.setBoth(Integer.MAX_VALUE);
        pick = false;
        calcButton.setDisable(false);
        CanvasGraphics.clearCanvas(calcCanvas);
        CanvasGraphics.clearCanvas(pickCanvas);
        CanvasGraphics.clearCanvas(pollutionCanvas);
    }


    @FXML
    public void gridModeButton() {
        gridCanvas.setVisible(!gridCanvas.isVisible());
    }

    @FXML
    public void MouseMoved(MouseEvent e) {
        highlightSquare(e.getX(), e.getY());
        CanvasGraphics.highlightZone(calcCanvas,terrain, new Vector2d(e.getX(), e.getY()));
    }

    private Vector2d picked = new Vector2d();

    private void highlightSquare(double X, double Y) {
        //TODO move to canvasGraphics
        int gridSize = terrain.getGridSize();
        int x = Terrain.getToGridSize(X, gridSize);
        int y = Terrain.getToGridSize(Y, gridSize);
        GraphicsContext gc = pickCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, pickCanvas.getWidth(), pickCanvas.getWidth());
        int min = gridSize * minGrid;
        if (picked.x == Integer.MAX_VALUE && picked.y == Integer.MAX_VALUE) {
            if (x >= min && x < pickCanvas.getHeight() - min && y >= min && y < pickCanvas.getHeight() - min) {
                gc.setFill(Color.color(1, 1, 1, 0.2));
                gc.fillRect(x, y, gridSize, gridSize);
            }
        } else {
            gc.setFill(Color.color(0, 0, 0, 0.3));
            gc.fillRect(picked.x, picked.y, gridSize, gridSize);
        }

    }

    @FXML
    public void mouseClicked(MouseEvent e) {
        pick = false;
        int gridSize = terrain.getGridSize();
        int min = gridSize * minGrid;
        int x = (int) (e.getX() / gridSize) * gridSize;
        int y = (int) (e.getY() / gridSize) * gridSize;
        if (picked.x == x && picked.y == y) {
            resetPicked();
            highlightSquare(Double.MAX_VALUE, Double.MAX_VALUE);
            return;
        }
        highlightSquare(e.getX(), e.getY());
        if (x >= min && x < pickCanvas.getHeight() - min && y >= min && y < pickCanvas.getHeight() - min) {
            CanvasGraphics.clearCanvas(pollutionCanvas);
            picked.x = x;
            picked.y = y;
            calcButton.setDisable(false);
            MouseMoved(e);
        }
    }

    @FXML
    public void KeyPressed(KeyEvent e) {
        //System.out.println(e.getCode());
        if (e.getCode() == KeyCode.ESCAPE) {
            resetPicked();
        }
    }

    public void mouseExited() {
        if (picked.x == Integer.MAX_VALUE) {
            GraphicsContext gc = pickCanvas.getGraphicsContext2D();
            gc.clearRect(0, 0, pickCanvas.getWidth(), pickCanvas.getWidth());
        }
    }

    private Boolean pick = false;
    public void calculationButton() {
        pick = !pick;
        if(pick){
            CanvasGraphics.clearCanvas(pollutionCanvas);
            ArrayList<Vector2d> surCoord = terrain.getNeighbors(picked.x, picked.y,minGrid);
            GraphicsContext gc = pollutionCanvas.getGraphicsContext2D();
            int gridSize = terrain.getGridSize();
            gc.setFill(Color.color(.3, 0.3, 1, 0.2));
            for (Vector2d c : surCoord) {
                if (c != null){
                    gc.fillRect(c.x,c.y,gridSize, gridSize);
                }
            }
            //coolCalculation(pX,pY);
        } else
            CanvasGraphics.clearCanvas(pollutionCanvas);


    }
}
