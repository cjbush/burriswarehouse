package code.autoplay;

/**
 * Defines an exception that is thrown when the replay attempts to access a Spatial with a name
 * that is not unique. In order to find the correct spatial during replay, it <b>must</b> have a
 * unique name.<br/><br/>
 * 
 * <b>Used in:</b></br/> 
 * 1. <code>WarehouseWriter.assessParentChange(String, String)</code> the user attempts to record
 * information about a spatial with a non-unique name.<br/><br/>
 * 2. <code>ReplayController.run()</code> when the user tries to replay information for a spatial
 * with a non-unique name.
 * 
 * @author Jordan Hinshaw
 *
 */
public class ObjectNameNotUniqueException extends RuntimeException {

	public ObjectNameNotUniqueException() {
		super();
	}

	public ObjectNameNotUniqueException(String arg0) {
		super(arg0);
	}

	public ObjectNameNotUniqueException(Throwable arg0) {
		super(arg0);
	}

	public ObjectNameNotUniqueException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
