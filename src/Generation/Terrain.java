package Generation;

import Utils.ReadResourse;

import java.util.*;


public class Terrain  {
    //private Square[][] squares;
    private int gridSize;
    private int seed;
    private int Pseed;
    private int Sseed;
    private int width, height;
    private double[][] heightMap;
    private double[][] populationMap;
    private double[][] waterMap;
    private double[][] safetyMap;
    private Properties prop;


    public Terrain(int width, int height, int gridSize) {
        this.width = width;
        this.height = height;
        this.gridSize = gridSize;
        reroll();
    }



    public void reroll(){
        prop = ReadResourse.getProperty("res/generation.properties");
        seed = new Random().nextInt(9999);
        Pseed = new Random().nextInt(9999);
        Sseed = new Random().nextInt(9999);
        generateHeightNwaterMap();
        generatePopul();
        generateSafetyMap();
        //System.out.println(String.format("Number iter - %d\nScale - %f\nPers - %f\nSeed - %d\n---", tempNumberIr,tempScale, tempPers, seed ));

    }

    private void generateHeightNwaterMap(){
        double heightMap[][] = new double[height][width];
        double waterMap[][] = new double[height][width];
        double scale = Double.parseDouble(prop.getProperty("heightMap.scale"));
        double pers = Double.parseDouble(prop.getProperty("heightMap.pers"));
        int numberIter = Integer.parseInt(prop.getProperty("heightMap.numberIter"));
        int hStep = Integer.parseInt(prop.getProperty("heightMap.step"));
        int wStep = Integer.parseInt(prop.getProperty("waterMap.step"));
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double val = SimplexNoise.sumOctave(numberIter, i, j, seed, pers, scale);
                val = (val+1)/2; //map from -1 -  1 to 0 - 1
                heightMap[i][j] = stepValue(val, hStep);
                waterMap[i][j] = val > 0.8 ? stepValue((1 - val) * 5 ,  wStep) : 0; //map 0.8 - 1 values to 1 - 0
            }
        }
        this.heightMap = heightMap;
        this.waterMap = waterMap;
    }

    public double stepValue(double v, int step){
        return Math.floor(v*step)/step;
    }

    /*private int tempNumberIr = 5;
    public void setTempNumberIr(int tempNumberIr) {
        if (this.tempNumberIr != tempNumberIr){
            this.tempNumberIr = tempNumberIr;
            reroll();
        }
    }

    private double tempScale = 0.0031;
    public void setTempScale(double tempScale) {
        tempScale = tempScale / 100000;
        if (this.tempScale != tempScale){
            this.tempScale = tempScale;
            reroll();
        }
    }

    private double tempPers = 0.14;
    public void setTempPers(double tempPers) {
        tempPers = tempPers / 100;
        if (this.tempPers != tempPers){
            this.tempPers = tempPers;
            reroll();
        }
    }*/

    private void generatePopul(){
        double popMap[][] = new double[height][width];
        double scale = Float.parseFloat(prop.getProperty("popMap.scale"));
        double pers = Float.parseFloat(prop.getProperty("popMap.pers"));
        int numberIter = Integer.parseInt(prop.getProperty("popMap.numberIter"));
        double mapPeak = Double.parseDouble(prop.getProperty("popMap.peak"));
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double val = SimplexNoise.sumOctave(numberIter, i, j, Pseed, pers, scale);
                popMap[i][j] = (val+1)/2;
                popMap[i][j] = popMap[i][j] > mapPeak ? popMap[i][j] : 0;
            }
        }

        popMap = gridify(popMap, gridSize);

        if (waterMap != null) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if(waterMap[i][j] > 0){
                        popMap[i][j] = 0;
                    }
                }
            }
        }
        this.populationMap = popMap;
    }

    private double[][] gridify(double[][] map, int grid) {
        for (int i = 0; i < map.length; i += grid) {
            for (int j = 0; j < map[0].length; j += grid) {
                double averenge = 0;
                for (int k = 0; k < grid; k++) {
                    for (int l = 0; l < grid; l++) {
                        averenge += map[i + k][j + l];
                    }
                }
                averenge /= grid * grid;
                for (int k = 0; k < grid; k++) {
                    for (int l = 0; l < grid; l++) {
                        map[i + k][j + l] = averenge;
                    }
                }
            }
        }
        return map;
    }

    private void generateSafetyMap() {
        double saveMap[][] = new double[height][width];
        double scale = Double.parseDouble(prop.getProperty("safeMap.scale"));
        double pers = Double.parseDouble (prop.getProperty("safeMap.pers"));
        int numberIter = Integer.parseInt(prop.getProperty("safeMap.numberIter"));
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double val = SimplexNoise.sumOctave(numberIter, i, j, Sseed, pers, scale);
                saveMap[i][j] = (val+1)/2;
            }
        }
        saveMap = gridify(saveMap, gridSize);
        if (populationMap != null) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (populationMap[i][j] == 0){
                        saveMap[i][j] = 0;
                    }
                }
            }
        } else System.out.println("Generate population map first");
        this.safetyMap = saveMap;
    }


    public int getDensity(int i, int j){
        return 1 + (int)Math.round((int)(populationMap[i][(j)] * 10));
    }

    public double[][] getPopulationMap() {
        return populationMap;
    }

    public double[][] getHeightMap() {
        return heightMap;
    }

    public double[][] getWaterMap() {
        return waterMap;
    }

    public double[][] getSafetyMap() {
        return safetyMap;
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }

    public int getGridSize() {
        return gridSize;
    }


}
