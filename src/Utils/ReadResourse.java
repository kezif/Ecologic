package Utils;

import java.io.*;
import java.util.Properties;

public class ReadResourse {
    public static Properties getProperty(String fileName) {
        Properties prop = new Properties();
        try {
            InputStream is = new FileInputStream(fileName);
            prop.load(is);
        } catch (FileNotFoundException ex) {
            System.out.println(String.format("File %s not found", fileName ));
        } catch (IOException e) {
            System.out.println("Can't load properties");
        }
        return prop;
    }



}
