package operators;

import java.util.HashSet;

import models.Tuple;

public class HashDuplicateEliminationOperator extends UnaryOperator {

	private HashSet<Tuple> set = new HashSet<Tuple>();
	public HashDuplicateEliminationOperator(Operator child) {
		super(child);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Tuple getNextTuple() {
		Tuple t = null;
		while ((t = child.getNextTuple()) != null) {
			if (set.contains(t)) continue;
			set.add(t);
			return t;
		}
		
		return null;
	}

}
