package operators;

import java.util.List;

import expvisitors.JoinVisitor;
import helpers.JoinHelper;
import models.Tuple;
import net.sf.jsqlparser.expression.Expression;

public class JoinOperator extends BinaryOperator {
	Expression joinCondition;
	Tuple leftTuple = null;
	Tuple rightTuple = null;
	//List<String> leftTupleSchema = null;
	//List<String> rightTupleSchema = null;
	JoinVisitor joinVisitor;
	
	
	public JoinOperator(Operator left, Expression joinCondition, Operator right) {
		super(left, right);
		this.joinCondition = joinCondition;
		leftTuple = left.getNextTuple();
		rightTuple = right.getNextTuple();
		joinVisitor = new JoinVisitor(left.getSchema(), right.getSchema());
	}

	public Tuple concatenateTuples(Tuple t1,Tuple t2) {
		System.out.println("Concatenate Tuples called......");
//		int size1 = t1.getSize();
//		int size2 = t2.getSize();
//		int j=0;
//		int[] fields = new int[size1+size2];
//		int i=0;
//		for(i=0;i<size1;i++){
//			fields[i] = t1.values[i];
//		}
//		i=size1;
//		for (int k=0;k<size2;k++){
//			fields[i] = t2.values[k];
//			i++;
//		}
		int[] fields =new int[t1.getSize()+t2.getSize()];
		int i=0;
		for(int val : t1.values){
			fields[i++]=val;
		}
		for(int val : t2.values){
			fields[i++]=val;
		}
		
		return new Tuple(fields);
	}

	@Override
	public Tuple getNextTuple() {
		Tuple ret = null;
		
		while (leftTuple != null && rightTuple != null) {
			if (joinCondition == null)
				ret = concatenateTuples(leftTuple, rightTuple);
			else
				{
					if (JoinHelper.getJoinResult(leftTuple, rightTuple, joinCondition, joinVisitor))
						ret = concatenateTuples(leftTuple, rightTuple);	
				}
			
			fixLeftIterateRight();
			if (ret != null) return ret;
		}
		
		return null;
	}
	
	
	public void fixLeftIterateRight(){
	if (leftTuple == null) return;
	// Keep checking the RHS table.
	if (rightTuple != null)
		rightTuple = rightchild.getNextTuple();
	
	if (rightTuple == null) {
		leftTuple = leftchild.getNextTuple();
		rightchild.reset();
		rightTuple = rightchild.getNextTuple();
	}
	
}
}
