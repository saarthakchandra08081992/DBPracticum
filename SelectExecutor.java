package utils;

import java.util.*;

import helpers.DBCatalog;
import helpers.SelectExecHelper;
import models.Table;
import net.sf.jsqlparser.statement.select.*;
import operators.*;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.Statement;

/**
 * 
 * @authors Saarthak Chandra Shweta Shrivastava Vikas P Nelamangala
 *
 */
public class SelectExecutor {
	// Action here....
	public Select sel;
	public Distinct distinctElements;
	public PlainSelect plainSelect;
	public List<SelectItem> selectElements;
	public FromItem from;
	public List<Join> joins;
	public Expression where;
	public ArrayList<OrderByElement> orderElements;
	
	public List<String> tableList = new ArrayList<String>();
	public List<Expression> ands = null;
	public HashMap<String, List<Expression>> selectCondition = null, joinCondition = null;
	public HashMap<String, Expression> fnSelCond, fnJoinCond;

	public Operator root = null;

	/**
	 * Constructor. It extracts all the binary expressions and analyze the
	 * relevant ones at each joining stage.
	 * 
	 * @param st
	 *            the SQL statement
	 */
	public SelectExecutor(Statement st) {
		sel = (Select) st;
		plainSelect = (PlainSelect) sel.getSelectBody();

		populateFromsWithJoinConditions();
		populateFroms();
		plainSelectInit();
		
		
		//mapFromsWithAliases();
		
		selectCondition = new HashMap<String, List<Expression>>();
		joinCondition = new HashMap<String, List<Expression>>();
		for (String tab : tableList) {
			selectCondition.put(tab, new ArrayList<Expression>());
			joinCondition.put(tab, new ArrayList<Expression>());
		}
		// After where we have list of Ands, so we loop through that
		ands = SelectExecHelper.getListOfExpressionsAnds(where);

		for (Expression exp : ands) {
			List<String> tabs = SelectExecHelper.getTabsInExpression(exp);
			int ctr = lastIdx(tabs);
			if (tabs == null)
				joinCondition.get(tableList.get(tableList.size() - 1)).add(exp);
			else if (tabs.size() <= 1)
				selectCondition.get(tableList.get(ctr)).add(exp);
			else
				joinCondition.get(tableList.get(ctr)).add(exp);
		}

		fnSelCond= new HashMap<String, Expression>();
		fnJoinCond=new HashMap<String, Expression>();

		for (String tab : tableList) {
			fnSelCond.put(tab, SelectExecHelper.getConcatenatedAnds(selectCondition.get(tab)));
			fnJoinCond.put(tab, SelectExecHelper.getConcatenatedAnds(joinCondition.get(tab)));
		}

		generateTree();

		selectCondition.clear();
		joinCondition.clear();
		fnSelCond.clear();
		fnJoinCond.clear();
	}

	/**
	 * Return a table according to its index in the FROM clause.
	 * 
	 * @param idx
	 *            the index
	 * @return the table
	 */
	private Table getTableObject(int idx) {
		Table table = DBCatalog.getTableObject(tableList.get(idx));
		//System.out.println("Table Name="+table.tableName);
		return table;
	}

	/**
	 * The max index of a list of tables in FROM.
	 * 
	 * @param tabs
	 *            the list of tables
	 * @return the last index
	 */
	private int lastIdx(List<String> tables) {
		if (tables == null)
			return tableList.size() - 1;
		int pos = 0;
		for (String tab : tables) {
			pos = Math.max(pos, tableList.indexOf(tab));
		}
		return pos;
	}

	/**
	 * Get the select condition of the idx'th table.
	 * 
	 * @param idx
	 *            the index
	 * @return the final select condition
	 */
	private Expression getSelectCondition(int pos) {
		return fnSelCond.get(tableList.get(pos));
	}

	/**
	 * Get the join condition of the idx'th table with its precedents in FROM.
	 * 
	 * @param idx
	 *            the index
	 * @return the join condition
	 */
	private Expression getJoinCond(int pos) {
		return fnJoinCond.get(tableList.get(pos));
	}

	/**
	 * Build the operator tree according to conditions in fnSelCond and
	 * fnJoinCond. The tree is built bottom-up
	 */
	private void generateTree() {
		System.out.println("Inside Genreate Tree....");
		// base of our tree is the scan operator
		//System.out.println("Table name before calling scan"+getTableObject(0).tableName);
		Operator rootTemp = new ScanOperator(getTableObject(0));

		if (getSelectCondition(0) != null)
			rootTemp = new SelectOperator((ScanOperator) rootTemp, getSelectCondition(0));

		int i = 1;
		while (i < tableList.size()) {	
			Operator scanOp = new ScanOperator(getTableObject(i));
			if (getSelectCondition(i) != null)
				{scanOp = new SelectOperator((ScanOperator) scanOp, getSelectCondition(i));}
			rootTemp = new JoinOperator(rootTemp,getJoinCond(i),scanOp);
			i++;
		}

		// After adding join/select nodes, look at operations like order and so
		// on.

		boolean isLossy = SelectExecHelper.projLossy(selectElements, orderElements);

		if (orderElements != null && isLossy)
			rootTemp = new SortOperator(orderElements,rootTemp);

		if (selectElements != null)
			rootTemp = new ProjectOperator(selectElements , rootTemp);

		if (orderElements != null && !isLossy)
			rootTemp = new SortOperator(orderElements , rootTemp);

		if (distinctElements != null) {
			if (orderElements == null)
				rootTemp = new SortOperator(new ArrayList<OrderByElement>(), rootTemp);

			if (isLossy)
				rootTemp = new HashDuplicateEliminationOperator(rootTemp);
			else
				rootTemp = new DuplicateEliminationOperator(rootTemp);
		}
		root = rootTemp;
		System.out.println("Came out of Generate Tree....");
	}

	private void populateFroms() {
		DBCatalog.aliases.clear(); // reset previously set aliases
		if (from.getAlias() != null) {
			DBCatalog.aliases.put(from.getAlias(), SelectExecHelper.getSingleTableName(from));
			tableList.add(from.getAlias());
		} else
			tableList.add(from.toString());
	}

	/**
	 * Function to add all the join conditions from the select statement
	 */
	private void plainSelectInit() {
		if (joins != null) {
			for (Join join : joins) { // loop through all the joins
				FromItem item = join.getRightItem();
				if (item.getAlias() != null) { // check if we have an alias
					System.out.println(item.toString());
					
					DBCatalog.aliases.put(item.getAlias(), SelectExecHelper.getSingleTableName(item));
					tableList.add(item.getAlias());
				} else
					tableList.add(item.toString());
			}
		}
		selectCondition = new HashMap<String, List<Expression>>();
		joinCondition = new HashMap<String, List<Expression>>();
	}

	private void populateFromsWithJoinConditions() {
		distinctElements = plainSelect.getDistinct();
		selectElements = plainSelect.getSelectItems();
		from = plainSelect.getFromItem();
		joins = plainSelect.getJoins();
		where = plainSelect.getWhere();
		orderElements = (ArrayList<OrderByElement>) plainSelect.getOrderByElements();
	}
	
	private void mapFromsWithAliases(){
		DBCatalog.aliases.clear();
		if (from.getAlias() != null) {
			DBCatalog.aliases.put(from.getAlias(), from.toString().split(" ")[0]);
			tableList.add(from.getAlias());
		}
		else
			tableList.add(from.toString());

	}

}
