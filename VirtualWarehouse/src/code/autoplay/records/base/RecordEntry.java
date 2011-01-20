package code.autoplay.records.base;

import java.io.Serializable;


import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

/**
 * The base class for all record entries.
 * 
 * @author Jordan Hinshaw
 *
 */
public abstract class RecordEntry implements Serializable
{
	private static final long serialVersionUID = -1925072508831803309L;
//	public static final RecordEntry END_READ 
//			= new RecordEntry(-1, RecordType.END_RECORD, null, null, null, null, null);
	
	protected float time = -1;
	protected RecordType type = null;
	protected Vector3f translation = null;
	protected Quaternion rotation = null;
	protected String objName;
	//protected String newParent = null;
	
	public RecordEntry(float aTime, String aName, RecordType aType, Vector3f aTrans, 
			Quaternion aRot) 
	{
		time = aTime;
		type = aType;
		translation = aTrans;
		rotation = aRot;
		objName = aName;
	}

	/**
	 * @return the time
	 */
	public float getTime() {
		return time;
	}

	/**
	 * @return the type
	 */
	public RecordType getType() {
		return type;
	}

	/**
	 * @return the translation
	 */
	public Vector3f getTranslation() {
		return translation;
	}

	/**
	 * @return the rotation
	 */
	public Quaternion getRotation() {
		return rotation;
	}
	
	/**
	 * @return the objName
	 */
	public String getObjName() {
		return objName;
	}
	
	@Override
	public String toString() {
		String str = "" + time;
		str+= (type != null) ? ", " + type : "" ;
		str+= (translation != null) ? ", " + translation.toString() : "";
		str+= (rotation != null) ? ", " + rotation.toRotationMatrix().toString() : "";
		str+= (objName != null) ?  ", " + objName : "";
		
		return str;
	}
}
