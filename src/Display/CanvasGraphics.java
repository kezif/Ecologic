package Display;

import Generation.Terrain;
import Utils.ReadResourse;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Properties;

public class CanvasGraphics {

    /*private Canvas terCanvas;
    private PixelWriter pw;

    public CanvasGraphics(Canvas terCanvas) {
        this.terCanvas = terCanvas;
    }*/
    static Properties prop;

    public static void readProp(){
        prop = ReadResourse.getProperty("res/display.properties");
    }

    public static void drawImage(Canvas canvas, Terrain terrain) {
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
                if(Wmap[i][j] > 0){
                    col = Color.web(colWaterFrom).interpolate(Color.web(colWaterTo), Wmap[i][j]);
                } else{
                    col = Color.web(colTerFrom).interpolate(Color.web(colTerTo),Hmap[i][j]);
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

    public static void drawSity(Canvas canvas, Terrain t){

    }


    /*public  static void drawDebugImage(Canvas canvas, Terrain t, int mode){
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
    }*/

    public static void clearCanvas(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0,0, canvas.getWidth(),canvas.getHeight());
    }

    static public void initializeGrid(Canvas c, int gridSize){
        PixelWriter gc = c.getGraphicsContext2D().getPixelWriter();
        for (int i = gridSize; i < c.getWidth(); i+=gridSize) {
            for (int j = gridSize; j < c.getHeight(); j+=gridSize) {
                gc.setColor(i,j,Color.BLACK);
            }
        }
        int mins = Integer.parseInt(prop.getProperty("padding"));
        int min = gridSize * mins;
        GraphicsContext gcc = c.getGraphicsContext2D();
        gcc.strokeRect(min, min, c.getHeight() - min * 2, c.getWidth() - min * 2);
    }
}
