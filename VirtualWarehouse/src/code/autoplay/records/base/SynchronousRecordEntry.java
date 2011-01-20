package code.autoplay.records.base;


import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

/**
 * Provides the basic functionality for all record entries that occur on a regular basis (such as
 * the movement of a pallet jack or the movement of a player).
 * 
 * @author Jordan Hinshaw
 *
 */
public abstract class SynchronousRecordEntry extends RecordEntry 
{
	//constants
	private static final long serialVersionUID = -1912002107925175533L;

	//variables
	protected String objName = null;
	
	/**
	 * Contructor.
	 * 
	 * @param aTime - the time stamp for the entry
	 * @param aType - the <code>code.autoplay.RecordType</code> of the entry
	 * @param name - the name of the model/node being moved
	 * @param aTrans - the translation of that model
	 * @param aRot - the rotation of that model
	 */
	public SynchronousRecordEntry(float aTime, RecordType aType, String name,
			Vector3f aTrans, Quaternion aRot) {
		super(aTime, name, aType, aTrans, aRot);
	}
	
	
	
	/**
	 * @param rec - SynchronousRecordEntry to be compared with
	 * @return true if the all of the fields of the record are the same, excluding the time; returns false otherwise
	 */
	public abstract boolean equalsExceptTime(SynchronousRecordEntry rec);

}
