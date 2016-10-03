package operators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import helpers.ProjOperatorHelper;
import models.Tuple;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.*;

public class ProjectOperator extends UnaryOperator {

	/**
	 * Generate a projected tuple from the schema.
	 * 
	 * @return the projected tuple
	 */
	@Override
	public Tuple getNextTuple() {
		Tuple tp = child.getNextTuple();
		if (tp == null)
			return null;

		int[] cols = new int[schema.size()];
		int i = 0;
		for (String attr : schema) {
			Long val = ProjOperatorHelper.getAttrVal(tp, attr, child.getSchema());
			cols[i++] = val.intValue();
		}

		return new Tuple(cols);
	}

	/**
	 * Construct a project operator. Both AllColumns, AllTableColumns and
	 * SelectExpressionItem are considered.
	 * 
	 * @param child
	 *            the child
	 * @param sis
	 *            the list of selected columns
	 */
	public ProjectOperator(List<SelectItem> selectItemList, Operator child) {
		
		super(child);
		System.out.println("Came inside Project Operator");
		List<String> childSchema = child.getSchema();
		List<String> tmpScm = new ArrayList<String>();
		HashSet<String> allColumnsOfTable = new HashSet<String>();

		for (SelectItem selectItem : selectItemList) {
			if (selectItem instanceof AllColumns) {
				schema = childSchema;
				return;
			}

			if (selectItem instanceof AllTableColumns)
				allColumnsOfTable.add(selectItem.toString().split(".")[0]);
			else {
				Column column = (Column) ((SelectExpressionItem) selectItem).getExpression();
				if (column.getTable() != null && (column.getTable().getName() != null)) {
					String table = column.getTable().getName();
					if (allColumnsOfTable.contains(table))
						continue;
					tmpScm.add(table + '.' + column.getColumnName());
				} else {
					String colName = column.getColumnName();
					for (String tabCol : childSchema) {
						if (ProjOperatorHelper.getNameOfColumn(tabCol).equals(colName)) {
							tmpScm.add(tabCol);
							break;
						}
					}
				}
			}
		}

		if (allColumnsOfTable.isEmpty())
			schema = tmpScm;
		else {
			for (String tabCol : childSchema) {
				String table = tabCol.split(".")[0];
				if (allColumnsOfTable.contains(table) || tmpScm.contains(tabCol))
					schema.add(tabCol);
			}
		}
	}

}
