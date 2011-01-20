/**
 * 
 */
package code.autoplay.replay.tracking.test;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * This class and its subclasses were created to help make the <code>DataGenerator</code> more flexible
 * by providing it with Queue of Expression with time limits that could be used to tailor the data
 * without having to hard-code it in. However, there is an error in it. And it does not work.
 *
 * <b>Note:</b> Idea for this Expression class taken from 
 * <a href="http://stackoverflow.com/questions/3742928/representing-math-equations-as-java-objects">
 * stackoverflow.com</a>.
 * 
 * @author Jordan Hinshaw
 *
 */
public abstract class Expression {

	//constants
	private static final String OP = "\\s*[" + Operation.ADD + Operation.DIV + Operation.EXPON 
								+ Operation.MULT + Operation.SUB + "]\\s*"; 
	
	private static final String NUM = "\\d+([.]\\d+)?";
	private static final String VAR = "[a-z]([a-z]|\\d)*";
	
	public static final char MULT = '*';
	public static final char ADD = '+';
	public static final char DIV = '/';
	public static final char EXPON = '^';
	public static final char SUB = '-';
	
	protected static final int NUM_CHILDREN = 2;
	protected static final int L_CHILD = 0;
	protected static final int R_CHILD = 1;
	
	
	//Variables
	//private Expression exp;
	protected String name;
	
	//private Hashtable<String, Expression> parenExps;
	protected Expression[] children;
	
	private double startTime;
	private double endTime;
	
	/**
	 * Used to ensure all expression get a distinct get a unique name
	 */
	private static int expressionCount = 0;
	
	private static int varCount = 0;
	
	/*public Expression(Expression exp, String name) {
		this.name = name;
	}*/
	
	protected Expression(String name) {
		this.name = name;
		//parenExps = new Hashtable<String, Expression>();
		children = new Expression[NUM_CHILDREN];
		this.startTime = -1;
		this.endTime = -1;
	}
	
	/*public Expression(String name, String exp) {
		this.name = name;
		parenExps = new Hashtable<String, Expression>();
		children = new Expression[NUM_CHILDREN];
		
		//this.exp = parse(exp, 0);
	}*/
	
	public static Expression generateExpression(String name, String expression, double startTime, double endTime){
		Expression e = parse(expression, 0, new Hashtable<String, Expression>());
		varCount = 0;
		e.setRuntimes(startTime, endTime);
		return e;
	}
	
	
	private static Expression parse(String exp, int parenCount, Hashtable<String, Expression> parenExps) {
		
		//temporary expression holders
		Expression temp;
		Expression left;
		Expression right;
		
		String editExp = exp;
		
		//finds the first open parenthesis if one exist
		int openParenIndx = exp.indexOf('(');
		
		//finds the first close parenthesis if one exists
		int closeParenIndx = exp.indexOf(')');
		
		if(openParenIndx >= 0 && openParenIndx < closeParenIndx ) {
			parse( exp.substring(openParenIndx + 1), parenCount + 1, parenExps);
		}
		else if(closeParenIndx >= 0 && parenCount > 0) {
			return parse(exp.substring(0, closeParenIndx), parenCount - 1, parenExps);
			/*temp = parse(exp.substring(0, closeParenIndx), parenCount - 1, parenExps);
		 	
		 	editExp = "$" + temp.name + exp.substring(closeParenIndx + 1); 
		 	parenExps.put(temp.name, temp);*/
		}
		else if(closeParenIndx >= 0 && parenCount <= 0) {
			throw new ParseException("Too many closing parentheses.");
		}
		
		/*if(openParenIndx >= 0) {
			return parse( exp.substring(openParenIndx + 1), openParenCount + 1);		
		}
		
		
		//finds the first close parenthesis if one exists
		int closeParenIndx = exp.indexOf(')');
		
		if(closeParenIndx >= 0) {
		 	temp = parse(exp.substring(0, closeParenIndx), openParenCount - 1);
		 	
		 	editExp = exp.substring(0, openParenIndx) + "$" + temp.name + exp.substring(openParenIndx + 1); 
		 	parenExps.put(temp.name, temp);
		}
		else if(parenCount <= 0) {
			throw new ParseException("Too many closing parentheses.");
		}*/
		
		
		//PARSE SUBTRACTION
		int indx = editExp.lastIndexOf(SUB);
		
		if( indx > 0) {
			left = parse(editExp.substring(0, indx).trim(), parenCount, parenExps);
			temp = new Operation("subE" + expressionCount++, Operation.OpType.SUB);
			
			String sub = editExp.substring( indx + 1, editExp.length() ).trim();
			right = parse(sub, parenCount, parenExps);
			
			temp.children[L_CHILD] = left;
			temp.children[R_CHILD] = right;
			
			return temp;
		}
		else if(indx == 0) {
			throw new ParseException("Expression cannot begin with: -");
		}
		
		
		//PARSE ADDITION
		indx = editExp.lastIndexOf(ADD);
		
		if( indx > 0) {
			left = parse(editExp.substring(0, indx).trim(), parenCount, parenExps);
			temp = new Operation("subE" + expressionCount++, Operation.OpType.ADD);
			
			String sub = editExp.substring( indx + 1, editExp.length() ).trim();
			right = parse(sub, parenCount, parenExps);
			
			temp.children[L_CHILD] = left;
			temp.children[R_CHILD] = right;
			
			return temp;
		}
		else if(indx == 0) {
			throw new ParseException("Expression cannot begin with: +");
		}
		
		
		//PARSE DIVIDE
		indx = editExp.lastIndexOf(DIV);
		
		if( indx > 0) {
			left = parse(editExp.substring(0, indx).trim(), parenCount, parenExps);
			temp = new Operation("subE" + expressionCount++, Operation.OpType.DIV);
			
			String sub = editExp.substring( indx + 1, editExp.length() ).trim();
			right = parse(sub, parenCount, parenExps);
			
			temp.children[L_CHILD] = left;
			temp.children[R_CHILD] = right;
			
			return temp;
		}
		else if(indx == 0) {
			throw new ParseException("Expression cannot begin with: /");
		}
		
		
		//PARSE MULTIPLICATION
		indx = editExp.lastIndexOf(MULT);
		
		if( indx > 0) {
			left = parse(editExp.substring(0, indx).trim(), parenCount, parenExps);
			temp = new Operation("subE" + expressionCount++, Operation.OpType.MULT);
			
			String sub = editExp.substring( indx + 1, editExp.length() ).trim();
			right = parse(sub, parenCount, parenExps);
			
			temp.children[L_CHILD] = left;
			temp.children[R_CHILD] = right;
			
			return temp;
		}
		else if(indx == 0) {
			throw new ParseException("Expression cannot begin with: *");
		}
		
		//PARSE EXPONENTS
		indx = editExp.lastIndexOf(EXPON);
		
		if( indx > 0) {
			left = parse(editExp.substring(0, indx).trim(), parenCount, parenExps);
			temp = new Operation("subE" + expressionCount++, Operation.OpType.EXP);
			
			String sub = editExp.substring( indx + 1, editExp.length() ).trim();
			right = parse(sub, parenCount, parenExps);
			
			temp.children[L_CHILD] = left;
			temp.children[R_CHILD] = right;
			
			return temp;
		}
		else if(indx == 0) {
			throw new ParseException("Expression cannot begin with: ^");
		}
		
		if( editExp.startsWith("$") ){
			return parenExps.get( editExp.substring(1) );
		}
		else if( editExp.matches(NUM) ) {
			return new Constant("subE" + expressionCount++, Double.parseDouble( editExp ));
		}
		else if( editExp.matches(VAR) && varCount < 1 ) {
			return new Variable(editExp);
		}
		else if( editExp.matches(VAR) && varCount >= 1 ) {
			throw new ParseException("Only one variable allowed in expression.");
		}
		else {
			throw new ParseException("Incorrect token: " + editExp);
		}
				
	}
	
	public double evaluate(double varValue) {
		return this.evaluate(varValue);
	}
	
	private void setRuntimes(double startTime, double endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public static void main(String[] args) {
		String str = "1*v  - 2+3 - (4*(5-6)) ^  2 * 2";
		
		Expression exp = Expression.generateExpression("q", str, 0, 20);
		double res = exp.evaluate(4.5);
		
		System.out.println(res);
	}

}
