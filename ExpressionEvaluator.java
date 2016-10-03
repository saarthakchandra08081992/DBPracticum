package expvisitors;

import expvisitors.SelectVisitor;
import net.sf.jsqlparser.expression.Expression;

public class ExpressionEvaluator {
	
	public static boolean evaluateSelectExpression(Expression selectCondition, SelectVisitor selVisitor){
		selectCondition.accept(selVisitor);
		return selVisitor.getExpressionEvaluationResult();
	}

}
