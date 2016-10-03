package models;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;

import helpers.DBCatalog;


/**
 * Table object to handle all table level operations
 * 
 * @author shweta
 *
 */
public class Table {

	//Every table will have a name and schema associated with it
	//We also create a buffer through which we connect to the data file of the table and read the tuples
	public String tableName;
	public List<String> tableSchema=null;
	private BufferedReader tableBuffer=null;
	
	/**
	 * Constructor class for table object
	 * @param tableName name of the table
	 * @param tableSchema schema of the table
	 * @param tableBuffer buffer to the table data file
	 */
	public Table(String tableName, List<String> tableSchema, BufferedReader tableBuffer){
		this.tableName = tableName;
		this.tableSchema = tableSchema;
		this.tableBuffer = tableBuffer;
	}
	
	/**
	 * Get next tuple from the buffer
	 * @return next tuple
	 */
	public Tuple getNextTuple(){
		String row = null;
		try {
			row = tableBuffer.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(row == null)
			return null;
		String[] rowvalues = row.split(",");
		int[] tupleValues = new int[rowvalues.length];
		for(int i=0;i<rowvalues.length;i++){
			tupleValues[i] = Integer.parseInt(rowvalues[i]);
		}
		Tuple tup = new Tuple(tupleValues);
		return tup;		
	}
	
	/**
	 * In order to reset, close the existing buffer and reset it again
	 */
	public void reset(){

		try {
			tableBuffer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tableBuffer = DBCatalog.createTableBuffer(tableName);
		
		
	}
	
}
