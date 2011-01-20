package code.autoplay.replay.tracking.math;

import java.math.BigDecimal;

import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;


/**
 * <b>File:</b> DecimalVector.java<br/>
 * <b>Date:</b> November, 2010<br/><br/>
 * <b>Description:</b> Defines a high accuracy 3 element vector that uses BigDecimals 
 * to store internal values. Designed to be easily used in conjunction with the 
 * <code>com.jme.math.Vector3f</code> format.
 * 
 * @author Jordan Hinshaw
 */
public class DecimalVector {
	//constants
	private static final int SIZE = 3;
	
	//variables
	private BigDecimal[] v;
	private Vector3f v3f;
	
	/**
	 * Constructor. Sets all values in the vector to 0.
	 */
	public DecimalVector() {
		this.init();
		this.resetVector();
	}
	
	/**
	 * Constructor. Sets all values in the DecimalVector equal to the
	 * values in the provided Vector3f.
	 * 
	 * @param vec
	 */
	public DecimalVector(Vector3f vec) {
		this.init();
		this.setVector(vec);
	}
	
	/**
	 * Constructor. Sets all values in the DecimalVector equal to the
	 * values in the provided float array. Only the first 3 values are
	 * used.
	 * 
	 * @param vec
	 */
	public DecimalVector(float[] vec) {
		this.init();
		this.setVector(vec);
	}
	
	/**
	 * Constructor. Sets all values in the DecimalVector equal to the
	 * values in the provided double array. Only the first 3 values are
	 * used.
	 * 
	 * @param vec
	 */
	public DecimalVector(double[] vec) {
		this.init();
		this.setVector(vec);
	}
	
	/**
	 * Constructor. Sets all values in the DecimalVector equal to the
	 * values in the provided int array. Only the first 3 values are
	 * used.
	 * 
	 * @param vec
	 */
	public DecimalVector(int[] vec) {
		this.init();
		this.setVector(vec);
	}
	
	/**
	 * Constructor. Sets all values in the DecimalVector equal to the
	 * values in the provided BigDecimal array. Only the first 3 values are
	 * used.
	 * 
	 * @param vec
	 */
	public DecimalVector(BigDecimal[] vec) {
		this.init();
		this.setVector(vec);
	}
	
	/**
	 * Internal convenience method that should be called by every constructor.
	 * Performs operations common to all constructor methods.
	 */
	private void init() {
		v = new BigDecimal[SIZE];
		v3f = new Vector3f();
	}
	
	/**
	 * Resets all values contained in this vector to 0.
	 */
	public void resetVector() {
		for(int i = 0; i < SIZE; i++) {
			v[i] = BigDecimal.ZERO;
			v3f.set(i, 0f);
		}
	}
	
	
	/**
	 * Sets the values of this vector equal to the values in the given Vector3f.
	 * 
	 * @param mat - matrix containing the values to copy.
	 */
	public void setVector(Vector3f vec) {
		for(int i = 0; i < SIZE; i++) {	
			v[i] = new BigDecimal( vec.get(i) );
		}
		
		v3f = new Vector3f(vec);
	}
	
	
	/**
	 * Sets the values of this vector equal to the values in the given array.
	 * Only the first 3 values are used.
	 * 
	 * @param vals - int array
	 */
	public void setVector(BigDecimal[] vals) {
		for(int i = 0; i < SIZE; i++) {
			v[i] = vals[i];
			v3f.set( i, vals[i].floatValue() );
		}
	}
	
	
	/**
	 * Sets the values of this vector equal to the values in the given array.
	 * Only the first 3 values are used.
	 * 
	 * @param vals - double array
	 */
	public void setVector(double[] vals) {
		for(int i = 0; i < SIZE; i++) {
			v[i] = new BigDecimal( vals[i] );
			v3f.set(i, (float) vals[i]);
		}
	}
	
	/**
	 * Sets the values of this vector equal to the values in the given array.
	 * Only the first 3 values are used.
	 * 
	 * @param vals - double array
	 */
	public void setVector(float[] vals) {
		for(int i = 0; i < SIZE; i++) {
			v[i] = new BigDecimal( vals[i] );
			v3f.set(i, vals[i]);
		}
	}
	
	/**
	 * Sets the values of this vector equal to the values in the given array.
	 * Only the first 3 values are used.
	 * 
	 * @param vals - int array
	 */
	public void setVector(int[] vals) {
		for(int i = 0; i < SIZE; i++) {
			v[i] = new BigDecimal( vals[i] );
			v3f.set(i, (float) vals[i]);
		}
	}
	
	
	/**
	 * @return a Vector3f approximation of this vector.
	 */
	public Vector3f toVector3f() {
		return v3f;
	}
	
	/**
	 * Sets v[indx] to the given value. 
	 * 
	 * @param indx - the index. Value must be between 0 and 2
	 * @param value
	 * @throws IndexOutOfBoundsException
	 */
	public void set(int indx, BigDecimal value) throws IndexOutOfBoundsException {
		if(indx < 0 || indx > SIZE) {
			throw new IndexOutOfBoundsException("Index provided: " + indx);
		}
		
		v[indx] = value;
		v3f.set(indx, value.floatValue() );
	}
	
	/**
	 * Sets v[indx] to the given value. 
	 * 
	 * @param indx - the index. Value must be between 0 and 2
	 * @param value
	 * @throws IndexOutOfBoundsException
	 */
	public void set(int indx, double value) throws IndexOutOfBoundsException {
		this.set(indx, new BigDecimal(value));
	}
	
	/**
	 * Sets v[indx] to the given value. 
	 * 
	 * @param indx - the index. Value must be between 0 and 2
	 * @param value
	 * @throws IndexOutOfBoundsException
	 */
	public void set(int indx, float value) throws IndexOutOfBoundsException {
		this.set(indx, new BigDecimal(value));
	}
	
	/**
	 * Sets v[indx] to the given value. 
	 * 
	 * @param indx - the index. Value must be between 0 and 2
	 * @param value
	 * @throws IndexOutOfBoundsException
	 */
	public void set(int indx, int value) throws IndexOutOfBoundsException {
		this.set(indx, new BigDecimal(value));
	}
	
	/**
	 * @param indx - must be between 0 and 2
	 * @return the BigDecimal stored at the given email address
	 * @throws IndexOutOfBoundsException
	 */
	public BigDecimal get(int indx) throws IndexOutOfBoundsException {
		if(indx < 0 || indx > SIZE) {
			throw new IndexOutOfBoundsException("Index provided: " + indx);
		}
		
		return this.v[indx];
	}
	
	/**
	 * Multiplies this DecimalVector by the given scalar. Does not modify
	 * this DecimalVector
	 * 
	 * @param x - BigDecimal multiplicand
	 */
	public DecimalVector mult(BigDecimal x) {
		BigDecimal[] res = new BigDecimal[SIZE];
		
		for(int i = 0; i < SIZE; i++) {
			res[i] = v[i].multiply(x);
		}
		
		return new DecimalVector(res);
	}
	
	/**
	 * Multiplies this DecimalVector by the given scalar. Does not modify
	 * this DecimalVector
	 * 
	 * @param x - float multiplicand
	 */
	public DecimalVector mult(float x) {
		return this.mult( new BigDecimal(x) );
	}
	
	/**
	 * Multiplies this DecimalVector by the given scalar. Does not modify
	 * this DecimalVector
	 * 
	 * @param x - double multiplicand
	 */
	public DecimalVector mult(double x) {
		return this.mult( new BigDecimal(x) );
	}
	
	/**
	 * Multiplies this DecimalVector by the given scalar. Does not modify
	 * this DecimalVector
	 * 
	 * @param x - int multiplicand
	 */
	public DecimalVector mult(int x) {
		return this.mult( new BigDecimal(x) );
	}
	
	/**
	 * Multiplies this DecimalVector by the given scalar. Changes the value
	 * of this DecimalVector.
	 * 
	 * @param x - BigDecimal multiplicand
	 */
	public void multLocal(BigDecimal x) {
		for(int i = 0; i < SIZE; i++) {
			v[i] = v[i].multiply(x);
			v3f.set( i, v[i].floatValue() );
		}
	}
	
	/**
	 * Multiplies this DecimalVector by the given scalar. Changes the value
	 * of this DecimalVector.
	 * 
	 * @param x - float multiplicand
	 */
	public void multLocal(float x) {
		this.multLocal( new BigDecimal(x) );
	}
	
	/**
	 * Multiplies this DecimalVector by the given scalar. Changes the value
	 * of this DecimalVector.
	 *  
	 * @param x - double multiplicand
	 */
	public void multLocal(double x) {
		this.multLocal( new BigDecimal(x) );
	}
	
	/**
	 * Multiplies this DecimalVector by the given scalar. Changes the value
	 * of this DecimalVector.
	 * 
	 * @param x - int multiplicand
	 */
	public void multLocal(int x) {
		this.multLocal( new BigDecimal(x) );
	}
	
	/**
	 * Adds the provided vector to this vector. Does not modify this
	 * DecimalVector.
	 * 
	 * @param vec
	 */
	public DecimalVector add(DecimalVector vec) {
		BigDecimal[] res = new BigDecimal[SIZE];
		BigDecimal aug;
		
		for(int i = 0; i < SIZE; i++) {
			aug = vec.get(i);
			res[i] = v[i].add(aug);
		}
		
		return new DecimalVector(res);
	}
	
	/**
	 * Adds the provided vector to this vector. Does not modify this
	 * DecimalVector.
	 * 
	 * @param vec
	 */
	public DecimalVector add(Vector3f vec) {
		BigDecimal[] res = new BigDecimal[SIZE];
		BigDecimal aug;
		
		for(int i = 0; i < SIZE; i++) {
			aug = new BigDecimal( vec.get(i) );
			res[i] = v[i].add(aug);
		}
		
		return new DecimalVector(res);
	}
	
	
	/**
	 * Adds the provided vector to this vector. Changes the value
	 * of this DecimalVector.
	 * 
	 * @param vec
	 */
	public void addLocal(DecimalVector vec) {
		BigDecimal aug;
		
		for(int i = 0; i < SIZE; i++) {
			aug = vec.get(i);
			v[i] = v[i].add(aug);
			v3f.set(i, v[i].floatValue() );
		}
	}
	
	/**
	 * Adds the provided vector to this vector. Changes the value
	 * of this DecimalVector.
	 * 
	 * @param vec
	 */
	public void addLocal(Vector3f vec) {
		BigDecimal aug;
		
		for(int i = 0; i < SIZE; i++) {
			aug = new BigDecimal( vec.get(i) );
			v[i] = v[i].add(aug);
			v3f.set(i, v[i].floatValue() );
		}
	}
	
	/**
	 * Utilizes the <code>com.jme.math.Vector3f.toString()</code>  method.
	 */
	@Override
	public String toString() {
		return v3f.toString();
	}
	
	
	/*public static void main(String[] args) {
		DecimalVector v = new DecimalVector(new int[]{3,4,5});
		Vector3f q = new Vector3f(2, 3, 1.1f);
		
		System.out.println(v + "  " + q);
		
		v.addLocal(q);
		
		System.out.println(v);
		
		v.multLocal(3);
		
		System.out.println(v);
	}*/
}
