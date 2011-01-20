package code.autoplay.records;

import code.autoplay.records.base.AsynchronousRecordEntry;
import code.autoplay.records.base.RecordEntry;
import code.autoplay.records.base.RecordType;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

/**
 * Defines a RecordEntry storing the information about changes made in the JME scene
 * graph.
 * 
 * @author Jordan Hinshaw
 *
 */
public class ParentChangeEntry extends AsynchronousRecordEntry 
{
	private static final long serialVersionUID = -5029720612114294671L;
	private String parent;

	public ParentChangeEntry(float aTime, String name, 
			String parentName, Vector3f aTrans, Quaternion aRot) 
	{
		super(aTime, RecordType.PARENT_CHANGE, name, aTrans, aRot);
		parent = parentName;
	}

	/**
	 * @return the parent
	 */
	public String getParentName() {
		return parent;
	}
	
}
