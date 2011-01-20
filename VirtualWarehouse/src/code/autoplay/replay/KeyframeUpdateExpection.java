/**
 * 
 */
package code.autoplay.replay;

/**
 * Defines an exception that is thrown by <code>UpdatableController.addKeyframe(float, 
 * Vector3f, Quaternion, Vector3f)</code> if the user attempts to add keyframe information
 * whose time stamp predates previous time keyframe times. 
 * 
 * @author Jordan Hinshaw
 *
 */
public class KeyframeUpdateExpection extends RuntimeException {

	/**
	 * 
	 */
	public KeyframeUpdateExpection() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public KeyframeUpdateExpection(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public KeyframeUpdateExpection(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public KeyframeUpdateExpection(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

}
