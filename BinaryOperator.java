package operators;

import java.util.ArrayList;
import java.util.List;

import models.Tuple;

public abstract class BinaryOperator extends Operator {

	public Operator leftchild;
	public Operator rightchild;
	
	public BinaryOperator(Operator leftchild, Operator rightchild){
		this.leftchild = leftchild;
		this.rightchild = rightchild;
		schema = new ArrayList<String>(leftchild.getSchema());
		schema.addAll(rightchild.getSchema());
	}


	@Override
	public void reset() {
		// TODO Auto-generated method stub
		leftchild.reset();
		rightchild.reset();
	}

	public List<String> getSchema() {
		return schema;
	}
}
