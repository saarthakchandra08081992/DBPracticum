package helpers;

import java.util.List;

import expvisitors.JoinVisitor;
import models.Tuple;
import net.sf.jsqlparser.expression.Expression;

public class JoinHelper {
	public static boolean getJoinResult(Tuple t1, Tuple t2, Expression joinCondition, JoinVisitor joinVisitor) {
		joinVisitor.setTuple(t1, t2);
		joinCondition.accept(joinVisitor);
		return joinVisitor.getExpressionEvaluationResult();
	}
	
	public static Long getColumnValue(Tuple t, List<String> schema,String columnName) {
		int idx = getColumnPosition(t,schema,columnName);
		if (idx != -1) return (long) t.getValue(idx);
		return null;
	}
	
	public static int getColumnPosition(Tuple t,  List<String> schema,String columnAttribute) {
		int position = schema.indexOf(columnAttribute);
		if (position != -1) return position;
	
		for(int i = 0; i < schema.size(); i++) {
			if (schema.get(i).split("\\.")[1].equals(columnAttribute))
				return i;
		}
		
		return -1;
	}
}
