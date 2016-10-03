package operators;

import java.util.ArrayList;
import java.util.List;
import models.Tuple;
import models.Table;

public class ScanOperator extends Operator {

	public Table currentTable;
	public List<String> currentSchema;
	
	public ScanOperator(Table table){
		this.currentTable = table;
		currentSchema = new ArrayList<String>();
		if (currentTable == null || currentTable.tableSchema == null){
			System.out.println("table / schema empty!");
		}
		generateSchema();		
	}
	
	@Override
	public Tuple getNextTuple() {
		// TODO Auto-generated method stub
		return currentTable.getNextTuple();
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		currentTable.reset();

	}
	
	public List<String> getSchema() {
		return currentSchema;
	}
	
	public void generateSchema() {
		for (String column : currentTable.tableSchema) {
			System.out.println("cols="+column);
			currentSchema.add(currentTable.tableName + '.' + column);
		}
	}

}
