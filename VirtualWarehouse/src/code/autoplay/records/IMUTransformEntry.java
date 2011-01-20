/**
 * 
 */
package code.autoplay.records;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

import code.autoplay.records.base.RecordType;
import code.autoplay.records.base.SynchronousRecordEntry;

/**
 * Defines a RecordEntry that stores the information gathered from the IMU device/file.
 * 
 * @author Jordan Hinshaw
 *
 */
public class IMUTransformEntry extends SynchronousRecordEntry {

	/**
	 * Constructor.
	 * 
	 * @param aTime
	 * @param name
	 * @param aTrans
	 * @param aRot
	 */
	public IMUTransformEntry(float aTime, String name, Vector3f aTrans, Quaternion aRot) {
		super(aTime, RecordType.IMU_TRANSFORM, name, aTrans, aRot);
	}


	@Override
	public boolean equalsExceptTime(SynchronousRecordEntry rec) {
		boolean isSame = false;
		
		if(rec instanceof IMUTransformEntry) {
			IMUTransformEntry transRec = (IMUTransformEntry) rec; 
			
			boolean sameRot = (this.rotation.equals(transRec.rotation));
			boolean sameTrans = (this.translation.equals(transRec.translation));
			boolean sameTime = (this.time == transRec.time);
			boolean sameName = this.objName.equals(transRec.objName);
			
			isSame = sameRot && sameTrans && sameTime && sameName;
		}
		
		return isSame;
	}

}
