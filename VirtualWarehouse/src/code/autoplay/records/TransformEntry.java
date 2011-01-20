package code.autoplay.records;

import code.autoplay.records.base.RecordEntry;
import code.autoplay.records.base.RecordType;
import code.autoplay.records.base.SynchronousRecordEntry;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

/**
 * Defines a RecordEntry that stores the information for recorded game data.
 * 
 * @author Jordan Hinshaw
 *
 */
public class TransformEntry extends SynchronousRecordEntry 
{
	private static final long serialVersionUID = 8829718212222303681L;
	protected Vector3f scale = null;
	
	public TransformEntry(float aTime, String name,
			Vector3f aTrans, Quaternion aRot, Vector3f aScale) 
	{
		super(aTime, RecordType.TRANSFORMATION, name, aTrans, aRot);
		scale = aScale;
	}

	/**
	 * @return the scale
	 */
	public Vector3f getScale() {
		return scale;
	}


	/**
	 * @see code.autoplay.records.base.SynchronousRecordEntry#equalsExceptTime(java.lang.Object)
	 */
	@Override
	public boolean equalsExceptTime(SynchronousRecordEntry rec) 
	{
		boolean isSame = false;
		
		if(rec instanceof TransformEntry)
		{
			TransformEntry transRec = (TransformEntry) rec; 
			
			boolean sameScale = (this.scale.equals(transRec.scale));
			boolean sameRot = (this.rotation.equals(transRec.rotation));
			boolean sameTrans = (this.translation.equals(transRec.translation));
			boolean sameTime = (this.time == transRec.time);
			boolean sameName = this.objName.equals(transRec.objName);
			
			isSame = sameScale && sameRot && sameTrans && sameTime && sameName;
		}
		
		return isSame;
	}

}
