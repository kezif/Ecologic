package Utils;

public class Utils {
    /*public static void saveToDics(String path, Terrain t){
        BufferedImage img = new BufferedImage(t.getHeight(), t.getWidth(), BufferedImage.TYPE_INT_RGB);
        double[][] p1 = t.getHeightMap();
        double[][] p2 = t.getPopulationMap();
        double[][] p3 = t.getWaterMap();
        double[][] p4 = t.getSafetyMap();
        for(int x = 0; x < t.getHeight(); x++){
            for(int y = 0; y<t.getHeight(); y++){
                Color col = new Color((float)p2[x][y], (float)p1[x][y],(float)p3[x][y]);
                img.setRGB(x, y, col.getRGB());
            }
        }
        File imageFile = new File(path+"/Maps.bmp");
        try {
            ImageIO.write(img, "bmp", imageFile);
            System.out.println(String.format("Saved!\nFile location: %s", imageFile.getAbsolutePath()));
        } catch (IOException r){

        }
    }*/
}
