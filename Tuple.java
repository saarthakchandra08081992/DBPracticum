package models;

import java.io.IOException;
import java.io.PrintStream;

/**
 * Tuple class to handle the table tuples
 * 
 * @author shweta
 *
 */
public class Tuple {

	public int[] values;

	/**
	 * Constructor for tuple
	 * 
	 * @param tupleValues
	 *            integer array read from file which represents tuple
	 */
	public Tuple(int[] tupleValues) {
		this.values = tupleValues;
	}

	/**
	 * Get the size of the tuple
	 * 
	 * @return size of the tuple
	 */
	public int getSize() {
		return values.length;
	}

	/**
	 * Get the value at the specified index
	 * 
	 * @param i
	 *            index
	 * @return column value at i
	 */
	public int getValue(int i) {
		return values[i];
	}

	/**
	 * Print the tuple row entry line by line in the print stream.
	 * 
	 * @param printstream
	 *            the print stream
	 */
	public void dump(PrintStream printstream) {
		try {
			String str = toString() + '\n';
			printstream.write(str.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		if (values.length < 1) return "";
		StringBuilder sb = new StringBuilder(String.valueOf(values[0]));
		int i = 1;
		while (i < values.length) {
			sb.append(',');
			sb.append(String.valueOf(values[i++]));
		}
		return sb.toString();
	}

}
