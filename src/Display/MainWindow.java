package Display;

import Calcul.Calculations.Company;
import Generation.Terrain;
import Utils.DataFromExcel.ExcelParser;
import Utils.Vector2d;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


public class MainWindow {

    @FXML
    public Canvas terCanvas;
    public Canvas gridCanvas;
    public Canvas pickCanvas;
    public Canvas sityCanvas;
    public Canvas zoneCanvas;
    public Button calcButton;
    public Canvas pollutionCanvas;
    public Label zoneNameLabel;
    public ListView companiesListView;
    public Button companyInfoButton;
    public Label regionLabel;

    private int minGrid;
    private Terrain terrain;
    private Company company;
    private Boolean companyPicked = false;
    private Boolean picked = false;
    private Vector2d pick = new Vector2d();
    private ExcelParser parser;



    @FXML
    private void initialize() {
        System.out.println("Initialization...");
        this.terrain = new Terrain((int) terCanvas.getWidth(), (int) terCanvas.getHeight(), (int) terCanvas.getWidth() / 20);
        RerollButton();
        CanvasGraphics.initializeGrid(gridCanvas, terrain.getGridSize());
        pick.setBoth(Integer.MAX_VALUE);
        minGrid = CanvasGraphics.getMinWidth();
        System.out.println("Done generating\nReading company names...\n---");
        readCompaniesNames();
        System.out.println("done!");
        CompaniesListViewValueChanged(); //listening to listview values
    }

    private void readCompaniesNames(){
        String[] companies = null;
        parser = null;
        try {
            parser = new ExcelParser("res/parametry_vybrosov_ZV.xls");
            try {
                companies = parser.getSheetList();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
        ObservableList<String> companiesList = FXCollections.observableArrayList(companies);
        companiesListView.setItems(companiesList);
    }

    @FXML
    public void RerollButton() {
        System.out.println("---");
        long startTime = System.nanoTime(); //timer
        terrain.reroll();
        long endTime = System.nanoTime(); //timer
        long duration = (endTime - startTime); //timer
        regionLabel.setText("Текущая область:\n" + terrain.getRegion().toString());
        System.out.println(String.format("Generation in %d ms", duration / 1000000)); //timer
        startTime = System.nanoTime(); //timer
        CanvasGraphics.readProp();
        CanvasGraphics.drawHeightNwater(terCanvas, terrain);
        CanvasGraphics.drawSity(sityCanvas, terrain);
        endTime = System.nanoTime(); //timer
        duration = (endTime - startTime); //timer
        System.out.println(String.format("Draw in %d ms", duration / 1000000)); //timer
        System.out.println("---");
        resetPicked();
        /*highlightSquare(CompaniesListViewValueChanged.x, CompaniesListViewValueChanged.y);
        CanvasGraphics.clearCanvas(pollutionCanvas);*/
    }

    private void resetPicked() {
        pick.setBoth(Integer.MAX_VALUE);
        picked = false;
        calcButton.setDisable(true);
        zoneNameLabel.setVisible(false);
        CanvasGraphics.clearCanvas(zoneCanvas);
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
        CanvasGraphics.highlightZone(zoneCanvas, terrain, new Vector2d(e.getX(), e.getY()), zoneNameLabel);
    }


    private void highlightSquare(double X, double Y) {
        //TODO move to canvasGraphics
        int gridSize = terrain.getGridSize();
        int x = Terrain.getToGridSize(X, gridSize);
        int y = Terrain.getToGridSize(Y, gridSize);
        GraphicsContext gc = pickCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, pickCanvas.getWidth(), pickCanvas.getWidth());
        int min = gridSize * minGrid;
        if (pick.x == Integer.MAX_VALUE && pick.y == Integer.MAX_VALUE) {
            if (x >= min && x < pickCanvas.getHeight() - min && y >= min && y < pickCanvas.getHeight() - min) {
                gc.setFill(Color.color(1, 1, 1, 0.2));
                gc.fillRect(x, y, gridSize, gridSize);
            }
        } else {
            gc.setFill(Color.color(0, 0, 0, 0.3));
            gc.fillRect(pick.x, pick.y, gridSize, gridSize);
        }

    }

    @FXML
    public void mouseClicked(MouseEvent e) {
        //Еба код который или подсвечивает ячейку на которую наведена мышь или же, если ячейка выбрана, подсвечивает ее
        picked = false;
        int gridSize = terrain.getGridSize();
        int min = gridSize * minGrid;
        int x = (int) (e.getX() / gridSize) * gridSize;
        int y = (int) (e.getY() / gridSize) * gridSize;
        if (pick.x == x && pick.y == y) {
            resetPicked();
            highlightSquare(Double.MAX_VALUE, Double.MAX_VALUE);
            return;
        }
        highlightSquare(e.getX(), e.getY());
        if (x >= min && x < pickCanvas.getHeight() - min && y >= min && y < pickCanvas.getHeight() - min) {
            CanvasGraphics.clearCanvas(pollutionCanvas);
            pick.x = x;
            pick.y = y;
            picked = true;
            checkEnableCalcButton();
            MouseMoved(e);
        }
    }

    private void checkEnableCalcButton() {
        calcButton.setDisable(!(picked && companyPicked));
    }

    @FXML
    public void KeyPressed(KeyEvent e) {
        //System.out.println(e.getCode());
        if (e.getCode() == KeyCode.ESCAPE) {
            resetPicked();
        }
    }

    @FXML
    public void mouseExited() {
        //Если ячейка не выбрана, то не показывает выделение
        if (pick.x == Integer.MAX_VALUE) {
            CanvasGraphics.clearCanvas(pickCanvas);
        }
        CanvasGraphics.clearCanvas(zoneCanvas);
        zoneNameLabel.setVisible(false);
    }


    @FXML
    public void calculationButton() {
        //picked = !picked;
        if (picked) {
            CanvasGraphics.clearCanvas(pollutionCanvas);
            ArrayList<Vector2d> surCoord = terrain.getNeighbors(pick.x, pick.y, minGrid);
            GraphicsContext gc = pollutionCanvas.getGraphicsContext2D();
            int gridSize = terrain.getGridSize();
            gc.setFill(Color.color(.3, 0.3, 1, 0.2));
            for (Vector2d c : surCoord) {
                if (c != null) {
                    gc.fillRect(c.x, c.y, gridSize, gridSize);
                }
            }
            //coolCalculation(pX,pY);
        } else
            CanvasGraphics.clearCanvas(pollutionCanvas);

    }

    @FXML
    private void CompaniesListViewValueChanged(){
        companiesListView.getSelectionModel().selectedItemProperty()
                .addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
                    companyPicked = true;
                    companyInfoButton.setDisable(!(companyPicked));
                    checkEnableCalcButton();
                    try {
                        company = parser.getData(newValue);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    System.out.println(company);
                });
    }

    @FXML
    public void InfoCompanyButton() {
        CompanyInfoFrom.display(company);
    }

    public void regionInfoButton() {
        RegionInfoFrom.display(terrain.getRegion());
    }
}
