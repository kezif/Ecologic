package Utils;

import java.io.*;
import java.util.Date;
import java.util.Properties;

public class ReadResourse {
    public static Properties getProperty(String fileName) {
        Properties prop = new Properties();
        InputStream is = null;
        try {
            is = new FileInputStream(fileName);
        } catch (FileNotFoundException ex) {
            System.out.println(String.format("File %s not found", fileName ));
        }
        try {
            prop.load(is);
        } catch (IOException ex) {
            System.out.println("Can't load properties");
        }
        return prop;
    }



}
