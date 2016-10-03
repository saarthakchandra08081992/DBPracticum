package helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Table;
import utils.PropertyFileReader;

public class DBCatalog {
	private static DBCatalog catalogInstance;
	// public static String inputDirectory="";
	// public static String outputDirectory="";
	public static String inputDirectory = "samples" + File.separator + "input" + File.separator;
	public static String outputDirectory = "samples" + File.separator + "output" + File.separator;
	public static String dbDirectory = "";
	public static String dataDirectory = "";
	public static String queryPath = "";
	public static String schemaPath = "";

	PropertyFileReader reader = PropertyFileReader.getInstance();
	public static Map<String, ArrayList<String>> schemas = new HashMap<String, ArrayList<String>>();
	public static Map<String, String> aliases = new HashMap<String, String>();
	
	public DBCatalog() {
		createDirectories(inputDirectory, outputDirectory);
	}


	 	public static void createDirectories(String newInputDirectory, String newOutputDirectory){
	 		// samples/input
	 		// samples/output
	 		inputDirectory = newInputDirectory;
	 		outputDirectory = newOutputDirectory;
	 		dbDirectory = newInputDirectory + File.separator+"db" + File.separator;
	 		dataDirectory = dbDirectory + "data" + File.separator;
	 		schemaPath = dbDirectory + "schema.txt";
	 		queryPath = inputDirectory +File.separator+ "queries.sql";
	 		createSchema();
	 	}

	public static DBCatalog getCatalogInstance() {
		//System.out.println("get catalog called");
		if (catalogInstance==null) {
			catalogInstance = new DBCatalog();
		}
		return catalogInstance;
	}

	public static void createSchema() {
		System.out.println("Create Schema Called....");
		BufferedReader br;
		 schemas.clear();
		try {
			br = new BufferedReader(new FileReader(schemaPath));
			String row = null;
			while ((row = br.readLine()) != null) {
				String[] tokens = row.split(" ");
					String key = tokens[0];
					ArrayList<String> columnNames = new ArrayList<String>();
					for (int i = 1; i < tokens.length; i++) {
						columnNames.add(tokens[i]);
					}				
					schemas.put(key, columnNames);
					//System.out.println("key=" + key + " ....and.... value=" + columnNames);
				
			}

			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Creates a table object with given table name and schema details.
	public static Table getTableObject(String tableName) {
		BufferedReader br = createTableBuffer(tableName);
		if (br == null) return null;
		return new Table(tableName, getSchema(tableName),br);
	}

	public static BufferedReader createTableBuffer(String fileName) {
		fileName = actualName(fileName);
		try {
			String actualTableName = actualName(fileName);
			return new BufferedReader(new FileReader(tableFilePath(actualTableName)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String tableFilePath(String tabName) {
		return dataDirectory + tabName + ".csv";
	}
	private static String actualName(String tabName) {
		if (aliases.containsKey(tabName))
			return aliases.get(tabName);
		return tabName.trim();
	}

//	public static void main(String[] args) {
//		DBCatalog dbCatalog = new DBCatalog();
//		createSchema();
//
//	}
	public static List<String> getSchema(String tabName) {
		return schemas.get(actualName(tabName));
	}

}
