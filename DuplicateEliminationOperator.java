package operators;

import models.Tuple;

public class DuplicateEliminationOperator extends UnaryOperator {
	
	Tuple latestTuple,nonDuplicateTuple = null;
	
	public DuplicateEliminationOperator(Operator child) {
		super(child);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Tuple getNextTuple() {
		// TODO Auto-generated method stub
		if(latestTuple == null){
			latestTuple = child.getNextTuple();
			return latestTuple;
		}
		else {
			nonDuplicateTuple = null;
			while((nonDuplicateTuple=child.getNextTuple())!=null){
				if(! (nonDuplicateTuple.getValue(0) == (latestTuple.getValue(0)))) 
					{break;}
			}
			latestTuple = nonDuplicateTuple;
			return nonDuplicateTuple;
			} 
		}
	}
