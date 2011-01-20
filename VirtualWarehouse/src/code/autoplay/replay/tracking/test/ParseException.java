/**
 * 
 */
package code.autoplay.replay.tracking.test;

/**
 * Defines an exception thrown when an incorrect equation was given to the Expression. See Expression.
 * 
 * @author Jordan Hinshaw
 *
 */
public class ParseException extends RuntimeException {

	/**
	 * Constructor
	 */
	public ParseException() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param String
	 */
	public ParseException(String arg0) {
		super(arg0);
	}

}
