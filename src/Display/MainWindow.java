package Display;

import Generation.Terrain;
import Utils.Vector2d;
import Utils.Utils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.*;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.Random;


public class MainWindow {
    //TODO refractor class/functions names (they are misslead)

    @FXML
    public Canvas terCanvas;
    public Canvas gridCanvas;
    public Canvas pickCanvas;
    public Slider slider1;
    public Slider slider2;
    public Slider slider3;
    //public CheckBox checkBudug;
    //public ComboBox comboB;
    public Canvas sityCanvas;
    public Canvas debugCanvas;
    public Button calcButton;
    public Canvas polutionCanvas;
    private int minGrid = 3;

    private Terrain terrain;

    @FXML
    private void initialize() {
        System.out.println("init");
        this.terrain = new Terrain((int) terCanvas.getWidth(), (int) terCanvas.getHeight(), (int) terCanvas.getWidth() / 20);
        evalButton();
        CanvasGraphics.initializeGrid(gridCanvas, terrain.getGridSize());
        System.out.println("done");
    }

    @FXML
    public void evalButton() {
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
        pX = pY = Integer.MAX_VALUE;
        highlightSquare(pX, pY);
        CanvasGraphics.clearCanvas(polutionCanvas);
    }


    @FXML
    public void gridModeButton() {
        gridCanvas.setVisible(!gridCanvas.isVisible());
    }

    @FXML
    public void MouseMoved(MouseEvent e) {
        highlightSquare(e.getX(), e.getY());
        double curSaf = terrain.getSquareAverenge(e.getX(), e.getY(), "SAFETY");
        Vector2d[] nei = Terrain.getNeighbors(e.getX(), e.getY(), terrain.getGridSize(), 1);
        for(Vector2d n : nei){
            //if(curSaf == )
        }
    }

    private int pX = Integer.MAX_VALUE;
    private int pY = Integer.MAX_VALUE;

    private void highlightSquare(double X, double Y) {
        int gridSize = terrain.getGridSize();
        int x = (int) (X / gridSize) * gridSize;
        int y = (int) (Y / gridSize) * gridSize;
        GraphicsContext gc = pickCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, pickCanvas.getWidth(), pickCanvas.getWidth());
        int min = gridSize * minGrid;
        if (pX == Integer.MAX_VALUE && pY == Integer.MAX_VALUE) {
            if (x >= min && x < pickCanvas.getHeight() - min && y >= min && y < pickCanvas.getHeight() - min) {
                gc.setFill(Color.color(1, 1, 1, 0.2));
                gc.fillRect(x, y, gridSize, gridSize);
            }
        } else {
            gc.setFill(Color.color(0, 0, 0, 0.3));
            gc.fillRect(pX, pY, gridSize, gridSize);
        }

    }

    @FXML
    public void mouseCliked(MouseEvent e) {
        picked = false;
        int gridSize = terrain.getGridSize();
        int min = gridSize * minGrid;
        int x = (int) (e.getX() / gridSize) * gridSize;
        int y = (int) (e.getY() / gridSize) * gridSize;
        if (pX == x && pY == y) {
            CanvasGraphics.clearCanvas(polutionCanvas);
            pX = pY = Integer.MAX_VALUE;
            calcButton.setDisable(true);
            highlightSquare(Double.MAX_VALUE, Double.MAX_VALUE);
            return;
        }
        highlightSquare(e.getX(), e.getY());
        if (x >= min && x < pickCanvas.getHeight() - min && y >= min && y < pickCanvas.getHeight() - min) {
            CanvasGraphics.clearCanvas(polutionCanvas);
            pX = x;
            pY = y;
            calcButton.setDisable(false);
            MouseMoved(e);
        }
    }

    @FXML
    public void KeyPressed(KeyEvent e) {
        //System.out.println(e.getCode());
        if (e.getCode() == KeyCode.ESCAPE) {
            pX = pY = Integer.MAX_VALUE;
            calcButton.setDisable(true);
        }
    }

    public void mouseExited() {
        if (pX == Integer.MAX_VALUE) {
            GraphicsContext gc = pickCanvas.getGraphicsContext2D();
            gc.clearRect(0, 0, pickCanvas.getWidth(), pickCanvas.getWidth());
        }
    }

    private Boolean picked = false;
    public void calcButton() {
        picked = !picked;
        if(picked){
            CanvasGraphics.clearCanvas(polutionCanvas);
            Vector2d[] surCoord = Terrain.getNeighbors(pX, pY, terrain.getGridSize(),minGrid);
            GraphicsContext gc = polutionCanvas.getGraphicsContext2D();
            int gridSize = terrain.getGridSize();
            gc.setFill(Color.color(.3, 0.3, 1, 0.2));
            for (Vector2d c : surCoord) {
                if (c != null){
                    gc.fillRect(c.x,c.y,gridSize, gridSize);
                }
            }
            //coolCalculation(pX,pY);
        } else
            CanvasGraphics.clearCanvas(polutionCanvas);


    }
}
