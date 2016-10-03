package operators;

import java.io.PrintStream;
import java.util.List;

import models.Tuple;

public abstract class Operator {
	public abstract Tuple getNextTuple();	
	public abstract List<String> getSchema();
	public abstract void reset();
	protected List<String> schema = null;
	
	/**
	 * Print Every table row
	 * @param ps the print stream
	 */
	public void dump(PrintStream ps) {
		Tuple tuple = null;
		while ((tuple = getNextTuple()) != null) {
			tuple.dump(ps);
		}
	}
	
}
