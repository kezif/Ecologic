package Display;

import Generation.Terrain;
import Generation.Zone;
import Utils.ReadResourse;
import Utils.Vector2d;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.Properties;

public class CanvasGraphics {


    static private Properties prop;
    //TODO неплонтняя заселеность города чтобы можно было ставить внутрь города
    //TODO нормальное чтение конфига

    public static void readProp() {
        prop = ReadResourse.getProperty("res/display.properties");
    }

    public static void drawHeightNwater(Canvas canvas, Terrain terrain) {
        double[][] Hmap = terrain.getHeightMap();
        double[][] Wmap = terrain.getWaterMap();
        clearCanvas(canvas);
        PixelWriter pw = canvas.getGraphicsContext2D().getPixelWriter();
        String colWaterFrom = prop.getProperty("water.fromCol");
        String colWaterTo = prop.getProperty("water.toCol");
        String colTerFrom = prop.getProperty("ter.fromCol");
        String colTerTo = prop.getProperty("ter.toCol");
        for (int i = 0; i < canvas.getHeight(); i++) {
            for (int j = 0; j < canvas.getWidth(); j++) {//ffe4b4  e1e67d
                Color col;// = Color.RED;
                if (Wmap[i][j] > 0) {
                    col = Color.web(colWaterFrom).interpolate(Color.web(colWaterTo), Wmap[i][j]);
                } else {
                    col = Color.web(colTerFrom).interpolate(Color.web(colTerTo), Hmap[i][j]);
                }
                pw.setColor(i, j, col);
            }
        }
    }

    public static void drawSity(Canvas canvas, Terrain t) {
        double[][] pMap = t.getPopulationMap();
        clearCanvas(canvas);
        PixelWriter pw = canvas.getGraphicsContext2D().getPixelWriter();
        for (int i = 0; i < canvas.getHeight(); i++) {
            for (int j = 0; j < canvas.getWidth(); j++) {
                if (pMap[i][j] > 0) {
                    Color col = transColor(getColorFromSafety(t.getSquareZone(i,j)), pMap[i][j]);
                    pw.setColor(i, j, col);
                }
            }
        }
        drawHouse(canvas,t);
    }

    static private void drawHouse(Canvas c, Terrain t){
        int gridsize = t.getGridSize();
        double[][] pMap = t.getPopulationMap();
        GraphicsContext gc = c.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(.5);
        float center = gridsize / 2f;
        float padding = gridsize / 6f;
        float size = gridsize / 8f;
        float pasi = padding + size;
        for (int i = 0; i < c.getHeight(); i += gridsize) {
            for (int j = 0; j < c.getWidth(); j += gridsize) {
                float cI = i + center;
                float cJ = j + center;
                int maxBuilding = t.getDensity(i, j);
                int count = 0;
                for (int k = -1; k <= 1; k++) {
                    for (int l = -1; l <= 1; l++) {
                        if (pMap[(int) (cI + k * pasi)][(int) (cJ + l * pasi)] > 0 && maxBuilding > count) {
                            drawHouseType(gc, cI - size / 2 + k * pasi, cJ - size / 2 + l * pasi, size, t);
                            count++;
                        }
                    }
                }

            }
        }
    }


    static private void drawHouseType(GraphicsContext gc, double x, double y, double size, Terrain t) {
        switch (t.getSquareZone((int) x, (int) y)) {
            case HOUSE:
                gc.strokeRect(x, y, size, size);
                break;
            case SHOP:
                gc.strokeOval(x, y, size, size);
                break;
            case FACTORY:
                gc.strokePolygon(new double[]{x, x + size, x}, new double[]{y, y, y + size}, 3);
                break;
            default:
                break;
        }
    }

    /*static private double houseV = 0.6;
    static private double shopV = 0.7;*/

    private static Color getColorFromSafety(Zone type) {
        switch (type) {
            case HOUSE:
                return Color.web(prop.getProperty("colHouse"));
            case SHOP:
                return Color.web(prop.getProperty("colShop"));
            case FACTORY:
                return Color.web(prop.getProperty("colFactory"));
            default:
                return Color.RED;
        }
    }


    private static Color transColor(Color c, double opasity) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), opasity);
    }


    public static void clearCanvas(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    static public void initializeGrid(Canvas c, int gridSize) {
        PixelWriter gc = c.getGraphicsContext2D().getPixelWriter();
        for (int i = gridSize; i < c.getWidth(); i += gridSize) {
            for (int j = gridSize; j < c.getHeight(); j += gridSize) {
                gc.setColor(i, j, Color.BLACK);
            }
        }
        int mins = Integer.parseInt(prop.getProperty("padding"));
        int min = gridSize * mins;
        GraphicsContext gcc = c.getGraphicsContext2D();
        gcc.strokeRect(min, min, c.getHeight() - min * 2, c.getWidth() - min * 2);
    }

    static public void highlightZone(Canvas c, Terrain t, Vector2d v, Label label) {
        clearCanvas(c);
        label.setVisible(false);
        GraphicsContext gc = c.getGraphicsContext2D();
        //double[][] safe = t.getSafetyMap();
        Zone zone = t.getSquareZone(v.x, v.y);
        if(zone == Zone.DIRT)
            return;
        label.setVisible(true);
        label.setText(zone+"");
        int grid = t.getGridSize();
        gc.setFill(Color.web(prop.getProperty("colZoneHighlight")));
        for (int i = 0; i < t.getHeight(); i += grid) {
            for (int j = 0; j < t.getWidth(); j += grid) {
                if(zone == t.getSquareZone(i,j)){
                    gc.fillRect(i,j,grid,grid);
                }
            }
        }
    }
}


