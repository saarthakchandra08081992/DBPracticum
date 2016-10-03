package helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.*;

/**
 * File with helpers needed by SelectExecutor, which builds the expression tree
 * 
 * @author Saarthak Chandra Shweta Shrivastava Vikas P Nelamangala
 *
 */
public class SelectExecHelper {

	/**
	 * Get table name from the input FromItem type
	 * 
	 * @param fromItm
	 *            - String that contains the table name, in the from part of an
	 *            expression
	 * @return first table name, that we get by splitting based on space
	 */
	public static String getSingleTableName(FromItem fromItm) {
		String returnTable = fromItm.toString().split(" ")[0]; 
		return returnTable;
	}

	/**
	 * 
	 * @param exp
	 *            - Expression to parse all and conditions
	 * @return List of smaller Expressions
	 */
	public static List<Expression> getListOfExpressionsAnds(Expression expression) {
		List<Expression> expressionList = new ArrayList<Expression>();
		while (expression instanceof AndExpression) {
			AndExpression andExpression = (AndExpression) expression;
			expressionList.add(andExpression.getRightExpression());
			expression = andExpression.getLeftExpression();
		}
		expressionList.add(expression);

		return expressionList;
	}

	/**
	 * In a binary expression, analyze table elements
	 * 
	 * @param expression
	 *            the binary expression
	 * @return the list of tables that are mentioned
	 * 
	 */

	public static List<String> getTabsInExpression(Expression expression) {
		List<String> expressionList = new ArrayList<String>();

		if ((expression instanceof BinaryExpression)) { // ONLY FOR BINARY
														// EXPRESSIONS

			BinaryExpression binaryExpression = (BinaryExpression) expression;
			Expression left = binaryExpression.getLeftExpression();
			Expression right = binaryExpression.getRightExpression();

			Column col;
			if (left instanceof Column) {
				col = (Column) left;
				expressionList.add(col.getTable().toString());
			}
			if (right instanceof Column) {
				col = (Column) right;
				// Add only if we have a different table
				if (!(expressionList.size() == 1 && expressionList.get(0).equals(col.getTable().toString())))
					expressionList.add(col.getTable().toString());
			}
		}
		return expressionList;
	}

	/**
	 * Join and expressions into one .
	 * 
	 * @param exps
	 *            the list of binary expressions
	 * @return the final AND expression
	 */
	public static Expression getConcatenatedAnds(List<Expression> exps) {
		if (exps.isEmpty())
			return null;
		//int i = 0;
		Expression joinedExpression = exps.get(0);
		for (int i = 1; i < exps.size(); i++)
			// joins lhs and rhs
			joinedExpression = new AndExpression(joinedExpression, exps.get(i));
		return joinedExpression;
	}

	/**
	 * Check if the ordered elements are not selected.
	 * 
	 * @param selectItems
	 *            - list of selected items
	 * @param orderByElement
	 *            - list of ordered elements
	 * @return true / false
	 */
	public static boolean projLossy(List<SelectItem> selectItems, List<OrderByElement> orderByElement) {

		HashSet<String> selectedColumns = new HashSet<String>();
		HashSet<String> orderedColumns = new HashSet<String>();

		if (selectItems.get(0) instanceof AllColumns)
			return false;

		// return if we get null/empty expression
		if (orderByElement == null || orderByElement.isEmpty())
			return false;

		for (OrderByElement orderByElements : orderByElement)
			orderedColumns.add(orderByElements.toString());

		for (SelectItem selectedItem : selectItems)
			selectedColumns.add(selectedItem.toString());

		orderedColumns.removeAll(selectedColumns);
		return !orderedColumns.isEmpty();
	}

}
