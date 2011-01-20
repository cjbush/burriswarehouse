package code.autoplay.replay.tracking.math;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

/**
 * <b>File:</b> DecimalMatrix_test.java<br/>
 * <b>Date:</b> November, 2010<br/><br/>
 * <b>Description:</b> Defines a relatively high accuracy 3x3 matrix that uses doubles 
 * to store internal values. Designed to be easily used in conjunction with the 
 * <code>com.jme.math.Matrix3f</code> format.
 * 
 * @author Jordan Hinshaw
 */
public class DecimalMatrix {

	//constants
	private static final int ROWS = 3;
	private static final int COLS = 3;
	
	//variables
	private double[][] m;
	
	
	/**
	 * Constructor. Sets all values to 0
	 */
	public DecimalMatrix() {	
		this.init();
		this.resetMatrix();
	}
	
	
	/**
	 * Constructor. Sets all values to the given Matrix3f
	 * 
	 * @param mat
	 */
	public DecimalMatrix(Matrix3f mat) {
		this.init();
		this.setMatrix(mat);
	}
	
	/**
	 * Constructor. Sets values of the matrix to the values of the
	 * array, in row major order.
	 * 
	 * @param vals - double array
	 */
	public DecimalMatrix(double[] vals) {
		this.init();
		this.setMatrix(vals);
	}
	
	/**
	 * Constructor. Sets values of the matrix to the values of the
	 * array, in row major order.
	 * 
	 * @param vals - float array
	 */
	public DecimalMatrix(float[] vals) {
		this.init();
		this.setMatrix(vals);
	}
	
	/**
	 * Constructor. Sets values of the matrix to the values of the
	 * array, in row major order.
	 * 
	 * @param vals - int array
	 */
	public DecimalMatrix(int[] vals) {
		this.init();
		this.setMatrix(vals);
	}
	
	
	/**
	 * Internal convenience method that should be called by every constructor.
	 * Performs operations common to all constructor methods.
	 */
	private void init() {
		m = new double[ROWS][COLS];
	}
	
	/**
	 * Resets all values contained in this matrix to 0.
	 */
	public void resetMatrix() {
		
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLS; j++) {
				m[i][j] = 0;
			}
		}
		
	}
	
	
	/**
	 * Sets the values of this matrix equal to the values of the given
	 * Matrix3f.
	 * 
	 * @param mat - matrix containing the values to copy.
	 */
	public void setMatrix(Matrix3f mat) {
		//copy values
		float x;
		
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLS; j++) {
				x = mat.get(i, j);
				m[i][j] = (double) x;
			}
		}
		
	}
	
	
	/**
	 * Sets the values of this matrix equal to the values in the given array
	 * in row major order. Only the first 9 values in the array are utilized.
	 * 
	 * @param vals - double array
	 */
	public void setMatrix(double[] vals) {
		int cnt = 0;
		
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLS; j++) {
				m[i][j] = vals[cnt] ;
				cnt++;
			}
		}
	}
	
	/**
	 * Sets the values of this matrix equal to the values in the given array
	 * in row major order. Only the first 9 values in the array are utilized.
	 * 
	 * @param vals - double array
	 */
	public void setMatrix(float[] vals) {
		int cnt = 0;
		
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLS; j++) {
				m[i][j] = (double) vals[cnt];
				cnt++;
			}
		}
	}
	
	/**
	 * Sets the values of this matrix equal to the values in the given array
	 * in row major order. Only the first 9 values in the array are utilized.
	 * 
	 * @param vals - int array
	 */
	public void setMatrix(int[] vals) {
		int cnt = 0;
		
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLS; j++) {
				m[i][j] = (double) vals[cnt];
				cnt++;
			}
		}
	}
	
	
	/**
	 * @return a Matrix3f approximation of this matrix.
	 */
	public Matrix3f toMatrix3f() {
		return new Matrix3f( (float) m[0][0], (float) m[0][1], (float) m[0][2], (float) m[1][0], 
				(float) m[1][1], (float) m[1][2], (float) m[2][0], (float) m[2][1], (float) m[2][2]);
	}
	
	public Quaternion toQuaternion() {
		return new Quaternion().fromRotationMatrix((float) m[0][0], (float) m[0][1], (float) m[0][2], 
				(float) m[1][0], (float) m[1][1], (float) m[1][2], (float) m[2][0], (float) m[2][1],
				(float) m[2][2]);
	}
	
	/**
	 * Sets m[row][col] to the given value. 
	 * 
	 * @param row - the row index. Value must be between 0 and 2
	 * @param col - the column index. Value must be between 0 and 2
	 * @param value
	 * @throws IndexOutOfBoundsException
	 */
	public void set(int row, int col, BigDecimal value) throws IndexOutOfBoundsException {
		this.set(row, col, value.doubleValue());
	}
	
	/**
	 * Sets m[row][col] to the given value. 
	 * 
	 * @param row - the row index. Value must be between 0 and 2
	 * @param col - the column index. Value must be between 0 and 2
	 * @param value
	 * @throws IndexOutOfBoundsException
	 */
	public void set(int row, int col, double value) throws IndexOutOfBoundsException {
		if(row < 0 || row > ROWS) {
			throw new IndexOutOfBoundsException("Row index provided: " + row);
		}
		else if(col < 0 || col > COLS) {
			throw new IndexOutOfBoundsException("Column index provided: " + col);
		}
		
		m[row][col] = value;
	}
	
	/**
	 * Sets m[row][col] to the given value. 
	 * 
	 * @param row - the row index. Value must be between 0 and 2
	 * @param col - the column index. Value must be between 0 and 2
	 * @param value
	 * @throws IndexOutOfBoundsException
	 */
	public void set(int row, int col, float value) throws IndexOutOfBoundsException {
		this.set(row, col, (double) value);
	}
	
	/**
	 * Sets m[row][col] to the given value. 
	 * 
	 * @param row - the row index. Value must be between 0 and 2
	 * @param col - the column index. Value must be between 0 and 2
	 * @param value
	 * @throws IndexOutOfBoundsException
	 */
	public void set(int row, int col, int value) throws IndexOutOfBoundsException {
		this.set(row, col, (double) value);
	}
	
	/**
	 * @param row - row index, must be between 0 and 2
	 * @param col - column index must be between 0 and 2
	 * @return the matrix value m[i][j]
	 */
	private double get(int row, int col) {
		return this.m[row][col];
	}
	
	/**
	 * Multiplies this DecimalMatrix by the given Matrix3f 
	 * 
	 * @param n - the multiplicand
	 */
	public void multBy(Matrix3f n) {
		double[] multRow;
		double reslt = 0;
		
		for(int i = 0; i < ROWS; i++) {
			//get the row of this matrix. Cloning is necessary to prevent
			//updates to this object from affecting the multRow
			multRow = this.getRow(i).clone();
			
			for(int j = 0; j < COLS; j++) {
				//gets the column of the multiplicand
				//multCol = n.getColumn(j);
				
				//multiplies the individual terms then adds them
				for(int q = 0; q < ROWS; q++) {
					reslt += multRow[q] * n.get(q, j);
				}
				
				//set the result
				m[i][j] = reslt;
				
				//clear results
				reslt = 0;
			}
		}
	}
	
	/**
	 * Multiplies this DecimalMatrix by the given DecimalMatrix 
	 * 
	 * @param n - the multiplicand
	 */
	public void multBy(DecimalMatrix n) {
		double[] multRow;
		double reslt = 0;
		double subRes;
		
		for(int i = 0; i < ROWS; i++) {
			//get the row of this matrix. Cloning is necessary to prevent
			//updates to this object from affecting the multRow
			multRow = this.getRow(i).clone();
			
			for(int j = 0; j < COLS; j++) {
				//gets the column of the multiplicand
				//multCol = n.getCol(j);
				
				//multiplies the individual terms then adds them
				for(int q = 0; q < ROWS; q++) {
					reslt += multRow[q] * n.m[q][j];
				}
				
				//set the result
				m[i][j] = reslt;
				
				//clear results
				reslt = 0;
			}
		}
	}
	
	/**
	 * @param i - the row to be returned. Value must be between 0 and 2
	 * @return the requested row
	 * @throws IndexOutOfBoundsException
	 */
	public double[] getRow(int i) throws IndexOutOfBoundsException {
		if(i < 0 || i >= ROWS) {
			throw new IndexOutOfBoundsException("Index provided: " + i);
		}
		
		return m[i];
	}
	
	/**
	 * @param j - the column to be returned. Value must be between 0 and 2
	 * @return the requested column
	 * @throws IndexOutOfBoundsException
	 */
	public double[] getCol(int j) throws IndexOutOfBoundsException {
		if(j < 0 || j >= COLS) {
			throw new IndexOutOfBoundsException("Index provided: " + j);
		}
		
		double[] col = new double[COLS];
		
		for(int i = 0; i < ROWS; i++) {
			col[i] = m[i][j];
		}
		
		return col;
	}
	
	/**
	 * Utilizes the <code>com.jme.math.Matrix3f.toString()</code>  method.
	 */
	@Override
	public String toString() {
		return this.toMatrix3f().toString();
	}
	
	/**
	 * Returns true if this matrix is an identity matrix. This method simply
	 * invokes the Matrix3f.isIdentity() method.
	 * 
	 * @return true if this matrix is an identity.
	 */
	public boolean isIdentity() {
		return this.toMatrix3f().isIdentity();
	}
	
	public static void main(String[] args) {
		int[] mval = {1,0,0,0,1,0,0,0,1};
		double[] nval = {9,8,7,6,5,4,3,2,1};
		
		DecimalMatrix m = new DecimalMatrix(mval);
		DecimalMatrix n = new DecimalMatrix(
				new Quaternion(0.7071067811865476f, 0f, 0f, -0.7071067811865476f).toRotationMatrix());
		
		System.out.println(m);
		System.out.println(n);
		
		//m.multBy(n);
		n.multBy(m);
		//m.set(0,2, 3.4);
		System.out.println(n.toString());
	}
}
