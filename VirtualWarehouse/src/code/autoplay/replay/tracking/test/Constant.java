package code.autoplay.replay.tracking.test;

/**
 * See Expression.
 * 
 * @author Jordan Hinshaw
 *
 */
public class Constant extends Expression {

	private double value;
	
	public Constant(String name, double value) {
		super(name);
		this.value = value;
		System.out.println(name + ", " + value);
	}
	
	@Override
	public double evaluate(double varValue) {
		return value;
	}
}
