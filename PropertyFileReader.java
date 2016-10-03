package utils;

import java.io.FileInputStream;
import java.util.Properties;
import error.SQLCustomErrorHandler;

public class PropertyFileReader extends Properties {
	private static String FILE_NAME = "conf.properties";
	private static String ROOT_PATH = "./src/";
	
	private static PropertyFileReader instance = null;

    private PropertyFileReader() {
    }

    public static PropertyFileReader getInstance() {
        if (instance == null) {
            try {
                instance = new PropertyFileReader();
                FileInputStream in = new FileInputStream(ROOT_PATH + FILE_NAME);
                instance.load(in);
                in.close();
            } catch (Exception ex) {
    			SQLCustomErrorHandler handler = new SQLCustomErrorHandler(ex.getMessage());
                return null;
            }
        }
        return instance;
    }
}