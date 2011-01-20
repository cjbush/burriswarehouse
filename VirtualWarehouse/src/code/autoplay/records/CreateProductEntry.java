package code.autoplay.records;

import code.autoplay.records.base.AsynchronousRecordEntry;
import code.autoplay.records.base.RecordEntry;
import code.autoplay.records.base.RecordType;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

/**
 * Defines a <code>RecordEntry</code> that stores informations about the creations of a product
 * when the user "grabs" product from the pallets. 
 * 
 * @author Jordan Hinshaw
 */
public class CreateProductEntry extends AsynchronousRecordEntry 
{
	private static final long serialVersionUID = -3438934391397340051L;
	private String parentName;
	
	public CreateProductEntry(float aTime, String name, String aParentName,
			Vector3f aTrans, Quaternion aRot) 
	{
		super(aTime, RecordType.CREATE_PRODUCT, name, aTrans, aRot);
		parentName = aParentName;
	}

	/**
	 * @return the parentName
	 */
	public String getParentName() {
		return parentName;
	}
}
