package helpers;

import java.util.List;

import models.Tuple;

public class ProjOperatorHelper {

	/**
	 * Obtain the vale of the column from the tuple
	 * 
	 * @param tp
	 *            the tuple
	 * @param attr
	 *            the attribute
	 * @param schema
	 *            the tuple's schema
	 * @return the long value of the attribute
	 */
	public static Long getAttrVal(Tuple tp, String attr, List<String> schema) {
		int indexValue = getAttributeIndex(tp,schema, attr);
		if (indexValue != -1)
			return (long) tp.getValue(indexValue);
		return null;
	}

	/**
	 * Get the index of a tp's attribute.
	 * 
	 * @param attribute
	 *            the attribute
	 * @param schema
	 *            the Database schema as a list of Strings
	 * @return the position of the attribute
	 */
	public static int getAttributeIndex(Tuple t,List<String> dbSchema, String attribute) {
		int idx = dbSchema.indexOf(attribute);
		if (idx != -1) return idx;
		// We return -1 if we cannot find the attribute in the DB Schema
		int returnValue = -1;

		for (int i = 0; i < dbSchema.size(); i++) {
			String columnName = getNameOfColumn(dbSchema.get(i));
			if (columnName.equals(attribute)) {
				returnValue = i;
			}
		}

		return returnValue;
	}

	/**
	 * Column name comes as - "Table.Column" string, we return the Column name
	 * 
	 * @param tableColumn
	 *            the string
	 * @return the name of the column
	 */
	public static String getNameOfColumn(String tableColumn) {
		return (String) tableColumn.split("\\.")[1]; // Table.column splits to
														// column by this
														// expressioon
	}

}
