package Generation;

import Utils.ReadResourse;
import Utils.Vector2d;

import java.util.*;

public class Terrain {
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
    private double overallPopulationPers;
    static final private double acceptebleSityPers = 0.4d;
    private Region region;


    public Terrain(int width, int height, int gridSize) {
        this.width = width;
        this.height = height;
        this.gridSize = gridSize;
        reroll();
    }


    public void reroll() {
        prop = ReadResourse.getProperty("res/generation.properties");
        seed = new Random().nextInt(9999);
        Pseed = new Random().nextInt(9999);
        Sseed = new Random().nextInt(9999);
        region = generateRegion();
        generateHeightNwaterMap();
        generatePopul();
        generateSafetyMap();
        generateOverallPopulationPers();
        if (overallPopulationPers < acceptebleSityPers) {
            reroll();
        }
    }

    private Region generateRegion() {
        int pick = new Random().nextInt(Region.values().length);
        return Region.values()[pick];
    }

    private void generateHeightNwaterMap() {
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
                val = (val + 1) / 2; //map from -1 -  1 to 0 - 1
                heightMap[i][j] = stepValue(val, hStep);
                waterMap[i][j] = val > 0.8 ? stepValue((1 - val) * 5, wStep) : 0; //map 0.8 - 1 values to 1 - 0
            }
        }
        this.heightMap = heightMap;
        this.waterMap = waterMap;
    }

    private double stepValue(double v, int step) {
        return Math.floor(v * step) / step;
    }

    private int getToGridSize(double v) {
        return (int) (v / gridSize) * gridSize;
    }

    public static int getToGridSize(double v, int gridSize) {
        return (int) (v / gridSize) * gridSize;
    }


    private void generatePopul() {
        double popMap[][] = new double[height][width];
        double scale = Float.parseFloat(prop.getProperty("popMap.scale"));
        double pers = Float.parseFloat(prop.getProperty("popMap.pers"));
        int numberIter = Integer.parseInt(prop.getProperty("popMap.numberIter"));
        double mapPeak = Double.parseDouble(prop.getProperty("popMap.peak"));
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double val = SimplexNoise.sumOctave(numberIter, i, j, Pseed, pers, scale);
                popMap[i][j] = (val + 1) / 2;
                popMap[i][j] = popMap[i][j] > mapPeak ? popMap[i][j] : 0;
            }
        }

        //popMap =
        gridify(popMap, gridSize);

        if (waterMap != null) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (waterMap[i][j] > 0) {
                        popMap[i][j] = 0;
                    }
                }
            }
        }
        this.populationMap = popMap;
    }

    public Zone getSquareZone(int x, int y) {
        x = getToGridSize(x);
        y = getToGridSize(y);
        double value = getSquareAverage(x, y, "SAFETY");
        if (value == 0)
            return Zone.DIRT;
        else if (value < Double.parseDouble(prop.getProperty("houseV")))
            return Zone.HOUSE;
        else if (value < Double.parseDouble(prop.getProperty("shopV")))
            return Zone.SHOP;
        else
            return Zone.FACTORY;
    }

    private void generateOverallPopulationPers() {
        double value = 0;
        double hGridC = height / (double) gridSize;
        double wGridC = width / (double) gridSize;
        for (int i = 0; i < height; i += gridSize) {
            for (int j = 0; j < width; j += gridSize) {
                value += getSquareBinCount(i, j, "POPULATION");
            }
        }
        overallPopulationPers = value / (wGridC * hGridC);
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
        double pers = Double.parseDouble(prop.getProperty("safeMap.pers"));
        int numberIter = Integer.parseInt(prop.getProperty("safeMap.numberIter"));
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double val = SimplexNoise.sumOctave(numberIter, i, j, Sseed, pers, scale);
                saveMap[i][j] = (val + 1) / 2;
            }
        }
        //saveMap =
        gridify(saveMap, gridSize);
        if (populationMap != null) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (populationMap[i][j] == 0) {
                        saveMap[i][j] = 0;
                    }
                }
            }
        } else System.out.println("Generate population map first");
        this.safetyMap = saveMap;
    }


    public int getDensity(int i, int j) {
        return (int) Math.round((getSquareAverage(i, j, "POPULATION") * 10));
        //return 1 + (int) Math.round((int) (populationMap[i][(j)] * 10));
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

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getGridSize() {
        return gridSize;
    }

    public double getOverallPopulationPers() {
        return overallPopulationPers;
    }

    public Region getRegion() {
        return region;
    }

    public double[][] getMap(String name) {
        double[][] map;
        switch (name) {
            case "HEIGHT":
                map = heightMap;
                break;
            case "WATER":
                map = waterMap;
                break;
            case "POPULATION":
                map = populationMap;
                break;
            case "SAFETY":
                map = safetyMap;
                break;
            default:
                System.out.println("Incorrect parameter");
                return new double[0][];
        }
        return map;
    }

    public double getSquareBinCount(double X, double Y, String type) {
        int x = getToGridSize(X);
        int y = getToGridSize(Y);
        double[][] map = getMap(type);
        double value = 0;
        for (int i = (int) x; i < x + gridSize; i++) {
            for (int j = (int) y; j < y + gridSize; j++) {
                value += map[i][j] > 0 ? 1 : 0;
            }
        }
        value /= gridSize * gridSize;
        return value;
    }

    public double getSquareAverage(double X, double Y, String type) {
        int x = getToGridSize(X);
        int y = getToGridSize(Y);
        double[][] map = getMap(type);
        double value = 0;
        for (int i = x; i < x + gridSize; i++) {
            for (int j = y; j < y + gridSize; j++) {
                value += map[i][j];
            }
        }
        value /= gridSize * gridSize;
        return value;
    }

    public void getSqareInfo(double x, double y) {
        x = getToGridSize(x);
        y = getToGridSize(y);
        double heightPers = 0;
        double waterPers = 0;
        double popPerst = 0;
        double safePers = 0;
        for (int i = (int) x; i < x + gridSize; i++) {
            for (int j = (int) y; j < y + gridSize; j++) {
                heightPers += heightMap[i][j];
                waterPers += waterMap[i][j] > 0 ? 1 : 0;
                popPerst += populationMap[i][j];
                safePers += safetyMap[i][j] > 0 ? 1 - safetyMap[i][j] : 0;
            }
        }
        heightPers /= gridSize * gridSize;
        waterPers /= gridSize * gridSize;
        popPerst /= gridSize * gridSize;
        safePers /= gridSize * gridSize;
        safePers += waterPers * .4;
        if (popPerst > 0)
            System.out.println(String.format("Average height - %f\nAverage population - %f\nPermission - %f\nWater persentage - %f\n ", heightPers, popPerst, safePers, waterPers));
        else
            System.out.println(String.format("Average height - %f\nAverage population - not a sity\nPermission - %f\nWater persentage - %f\n ", heightPers, safePers, waterPers));
    }

    public ArrayList<Vector2d> getNeighbors(double X, double Y, int radius) {
        int x = getToGridSize(X, gridSize);
        int y = getToGridSize(Y, gridSize);
        ArrayList<Vector2d> neighbors = new ArrayList<>();//new Vector2d[radius*100];
        int count = 0;
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius + 1; j++) {
                if (i * i + j * j <= radius * radius + 1) {
                    int desiredX = i * gridSize + x;
                    int desiredY = j * gridSize + y;
                    //neighbors[count++] = new Vector2d(desiredX, desiredY);
                    neighbors.add(new Vector2d(desiredX, desiredY));
                    neighbors.removeIf(f -> f.x < 0 || f.x > height - gridSize || f.y < 0 || f.y > width - gridSize || (f.x == x && f.y == y));
                    //if((i == 0 && j == 0) || desiredX < 0 || desiredX > height-gridSize || desiredY < 0 || desiredY > width-gridSize)
                    //neighbors[count--] = null;
                }
            }
        }
        return neighbors;
    }

}
