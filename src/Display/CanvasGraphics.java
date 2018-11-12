package Display;

import Generation.Terrain;
import Utils.ReadResourse;
import Utils.Vector2d;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.Properties;

public class CanvasGraphics {


    static Properties prop;
    //TODO неплонтняя заселеность города чтобы можно было ставить внутрь города
    //TODO Подсветка области
    //TODO нормальное чтение конфига
    //TODO enum вместо стрингов

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
        double[][] sMap = t.getSafetyMap();
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
        int gridsize = t.getGridSize();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(.5);
        float center = gridsize / 2;
        float padding = gridsize / 6;
        float size = gridsize / 8;
        float pasi = padding + size;
        for (int i = 0; i < canvas.getHeight(); i += gridsize) {
            for (int j = 0; j < canvas.getWidth(); j += gridsize) {
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
            case "HOUSE":
                gc.strokeRect(x, y, size, size);
                break;
            case "SHOP":
                gc.strokeOval(x, y, size, size);
                break;
            default:
                gc.strokePolygon(new double[]{x, x + size, x}, new double[]{y, y, y + size}, 3);
                break;
        }
    }

    /*static private double houseV = 0.6;
    static private double shopV = 0.7;*/

    private static Color getColorFromSafety(String type) {
        switch (type) {
            case "HOUSE":
                return Color.web(prop.getProperty("colHouse"));
            case "SHOP":
                return Color.web(prop.getProperty("colShop"));
            default:
                return Color.web(prop.getProperty("colFactory"));
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

    static public void highlightZone(Canvas c, Terrain t, Vector2d v) {
        clearCanvas(c);
        GraphicsContext gc = c.getGraphicsContext2D();
        //double[][] safe = t.getSafetyMap();
        String zone = t.getSquareZone(v.x, v.y);
        if(zone.equals(""))
            return;
        int grid = t.getGridSize();
        gc.setFill(Color.web(prop.getProperty("colZoneHighlight")));
        System.out.println(gc.getFill().toString());
        for (int i = 0; i < t.getHeight(); i += grid) {
            for (int j = 0; j < t.getWidth(); j += grid) {
                if(zone.equals(t.getSquareZone(i,j))){
                    gc.fillRect(i,j,grid,grid);
                }
            }
        }
    }
}
