package code.autoplay.records.base;


import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

/**
 * Provides the basic functionality for all record entries that can occur at any time throughout
 * the game play.
 * 
 * @author Jordan Hinshaw
 *
 */
public abstract class AsynchronousRecordEntry extends RecordEntry 
{
	//constants
	private static final long serialVersionUID = 8760372859491802019L;
	
	//variables
	//protected String objName = null;
	
	public AsynchronousRecordEntry(float aTime, RecordType aType, String name,
			Vector3f aTrans, Quaternion aRot) {
		super(aTime, name, aType, aTrans, aRot);
	}


}
