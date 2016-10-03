package operators;

import java.util.List;

import models.Tuple;

public abstract class UnaryOperator extends Operator {

	public Operator child;
	
	
	public UnaryOperator(Operator child){
		this.child = child;
	}

	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		child.reset();
	}
	
	public List<String> getSchema() {
		if (this.schema != null)
			return this.schema;
		return child.getSchema();
	}
}
