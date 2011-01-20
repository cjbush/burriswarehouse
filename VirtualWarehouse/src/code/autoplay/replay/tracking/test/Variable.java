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
public class Variable extends Expression {
	
	
	public Variable(String name) {
		super(name);
		System.out.println(name);
	}
	
	public double evaluate(double varValue) {
		return varValue;
	}
}
