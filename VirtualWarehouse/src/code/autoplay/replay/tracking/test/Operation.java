/**
 * 
 */
package code.autoplay.replay.tracking.test;

/**
 * See Expression.
 * 
 * @author Jordan
 *
 */
public class Operation extends Expression {

	//constants
	public enum OpType{ADD, SUB, MULT, DIV, EXP} 
	
	//variables
	private OpType type;
	
	public Operation(String name, OpType type) {
		super(name);
		this.type = type;
		System.out.println(name + ", " + type);
	}
	
	public double evaluate(double varValue) {
		double left = children[L_CHILD].evaluate(varValue);
		double right = children[R_CHILD].evaluate(varValue);

		double result = 0;
		
		switch(type) {
			case ADD:
				result = left + right;
				break;
			case DIV:
				result = left / right;
				break;
			case EXP:
				result = Math.pow(left, right);
				break;
			case MULT:
				result = left * right;
				break;
			case SUB:
				result = left - right;
				break;
		}
		
		return result;
	}
}
