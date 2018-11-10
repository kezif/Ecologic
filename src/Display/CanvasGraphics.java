package Display;

import Generation.Terrain;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class CanvasGraphics {

    /*private Canvas terCanvas;
    private PixelWriter pw;

    public CanvasGraphics(Canvas terCanvas) {
        this.terCanvas = terCanvas;
    }*/

    public static void drawHeightNwater(Canvas canvas, Terrain terrain) {
        double[][] Hmap = terrain.getHeightMap();
        double[][] Wmap = terrain.getWaterMap();
        clearCanvas(canvas);
        PixelWriter pw = canvas.getGraphicsContext2D().getPixelWriter();
        for (int i = 0; i < canvas.getHeight(); i++) {
            for (int j = 0; j < canvas.getWidth(); j++) {//ffe4b4  e1e67d
                Color col;// = Color.RED;
                if(Wmap[i][j] > 0){
                    col = Color.web("#0099bf").interpolate(Color.web("#83e9ff"), Wmap[i][j]);
                } else{
                    col = Color.web("#daea7e").interpolate(Color.web("#2b6200"),Hmap[i][j]);
                }
                pw.setColor(i,j,col);
                /*if(Pmap[i][j] > 0){
                    col = Color.web("#708090").interpolate(Color.web("#C0C0C0"), Pmap[i][j]);
                    col = new Color(col.getRed(),col.getGreen(),col.getBlue(), 0.4);
                    pw.setColor(i,j,col);
                }*/
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
                    Color col = transColor(getColorFromSafety(sMap[i][j]), pMap[i][j]);
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
                int maxBuilding = 1 +  (int)(pMap[(int)cI][(int)(cJ)] * 10)%10;
                int count = 0;
                for (int k = -1; k <= 1; k++) {
                    for (int l = -1; l <= 1; l++) {
                        if (pMap[(int)(cI + k * pasi)][(int)( cJ +  l * pasi)] > 0 && maxBuilding > count) {
                            drawhouse(gc,cI - size / 2 + k * pasi, cJ - size / 2 + l * pasi, size, sMap[(int)(cI + k * pasi)][(int)( cJ +  l * pasi)]);
                            count++;
                        }
                    }
                }

            }
        }
    }


    static private void drawhouse(GraphicsContext gc, double x, double y, double size, double value){
        if (value < houseV)
            gc.strokeRect(x,y,size,size);
        else if (value < shopV)
            gc.strokeOval(x,y,size,size);
        else
            gc.strokePolygon(new double[]{x, x + size, x},new double[]{y, y, y + size},3);
    }

    static private double houseV = 0.5;
    static private double shopV = 0.7;
    private static Color getColorFromSafety(double value) {
        if (value < houseV)
            return Color.web("#1B6623");
        else if (value < shopV)
            return Color.web("#00BFFF");
        else
            return Color.web("#ED4337");
    }


    private static Color transColor(Color c, double opasity) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), opasity);
    }


    public  static void drawDebugImage(Canvas canvas, Terrain t, int mode){
        double[][] map;
        switch (mode){
            case 0: map = t.getHeightMap();
            break;
            case 1: map = t.getWaterMap();
            break;
            case 2: map = t.getPopulationMap();
            break;
            case 3: map = t.getSafetyMap();
            break;
            default: map = new double[t.getHeight()][t.getWidth()];
            break;
        }
        PixelWriter pw = canvas.getGraphicsContext2D().getPixelWriter();
        for (int i = 0; i < canvas.getHeight(); i++) {
            for (int j = 0; j < canvas.getWidth(); j++) {
                Color col = Color.gray(map[i][j]);
                pw.setColor(i,j,col);
            }
        }
    }

    public static void clearCanvas(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0,0, canvas.getWidth(),canvas.getHeight());
    }

    static public void initializeGrid(Canvas c, int gridSize, int mins){
        PixelWriter gc = c.getGraphicsContext2D().getPixelWriter();
        for (int i = gridSize; i < c.getWidth(); i+=gridSize) {
            for (int j = gridSize; j < c.getHeight(); j+=gridSize) {
                gc.setColor(i,j,Color.BLACK);
            }
        }
        int min = gridSize * mins;
        GraphicsContext gcc = c.getGraphicsContext2D();
        gcc.strokeRect(min, min, c.getHeight() - min * 2, c.getWidth() - min * 2);
    }
}
