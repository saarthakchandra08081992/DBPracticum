package operators;

import models.Tuple;
import net.sf.jsqlparser.expression.Expression;

import java.util.List;

import expvisitors.ExpressionEvaluator;
import expvisitors.SelectVisitor;

public class SelectOperator extends UnaryOperator {	

	Expression selectCondition;
	Tuple currentTuple = null;
	List<String> tupleSchema = null;
	SelectVisitor selVisitor;
	
	public SelectOperator(ScanOperator child,Expression selectCondition) {
		super(child);
		this.selectCondition = selectCondition;
		this.tupleSchema = child.getSchema();
		// TODO Auto-generated constructor stub
	}

	@Override
	public Tuple getNextTuple() {
		// TODO Auto-generated method stub
		while((currentTuple = child.getNextTuple()) != null){
			selVisitor = new SelectVisitor(currentTuple, tupleSchema);
			if(ExpressionEvaluator.evaluateSelectExpression(selectCondition,selVisitor)){
				return currentTuple;
			}
		}
			return null;
	}
	
}
