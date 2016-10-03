package expvisitors;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;

import java.util.List;

import helpers.AttributeMapper;
import helpers.JoinHelper;
import models.Tuple;

public class JoinVisitor extends ExpressionVisitorWrapper {
 
	private Tuple t1 = null, t2 = null;
	private List<String> schema1 = null, schema2 = null;
	public JoinVisitor(List<String> schema1, List<String> schema2) {
		// TODO Auto-generated constructor stub
			
			this.schema1 = schema1; 
			this.schema2 = schema2;
		
	}
	public void setTuple(Tuple t1, Tuple t2) {
		this.t1 = t1;
		this.t2 = t2;
	}
	
	@Override
	public void visit(Column arg0) {
		Long columnValue = JoinHelper.getColumnValue(t1, schema1,arg0.toString());
		if (columnValue == null)
			columnValue = JoinHelper.getColumnValue(t2,schema2, arg0.toString());
		expressionValue = columnValue;
	}

}
