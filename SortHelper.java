package helpers;

import java.util.LinkedList;
import java.util.List;
import models.Tuple;
import java.util.Comparator;
import net.sf.jsqlparser.statement.select.OrderByElement;

public class SortHelper {	
	public static int getAttributePosition(String selectedAttribute, List<String> tableSchema) {
		int index = tableSchema.indexOf(selectedAttribute);
		if (index != -1) return index;
		else {
			for(int i = 0; i < tableSchema.size(); i++) {
				if (tableSchema.get(i).split("\\.")[1].equals(selectedAttribute)) return i;
			}
			return -1;
		}
	}
}
