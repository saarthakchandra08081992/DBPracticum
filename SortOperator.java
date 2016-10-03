package operators;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import helpers.CustomTupleComparator;
import helpers.SortHelper;
import models.Tuple;
import net.sf.jsqlparser.statement.select.OrderByElement;


public class SortOperator extends UnaryOperator {
	List<Tuple> tuplesList = new ArrayList<Tuple>();
	List<Integer> ordersList = new ArrayList<Integer>();
	int index = 0;
	Tuple t;
	
	public SortOperator(ArrayList<OrderByElement> orders,Operator child) {
		super(child);
		// TODO Auto-generated constructor stub
		
		while(true){
			if((t = child.getNextTuple()) != null){
					tuplesList.add(t);
				}
			else {
				break;
			}
		
		for (OrderByElement element : orders){
			this.ordersList.add(SortHelper.getAttributePosition(element.toString(),child.getSchema()));
			}	
		}
		Collections.sort(tuplesList, new CustomTupleComparator(this.ordersList));
			
	}

	@Override
	public Tuple getNextTuple() {
		// TODO Auto-generated method stub
		if (index >= tuplesList.size()) {
			return null;
			}
		else {
			return tuplesList.get(index++);
			}
	}
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		index = 0;
	}

}
