package code.MD5.resource.mesh.primitive;

import java.io.IOException;
import java.io.Serializable;

import com.jme.math.Vector3f;
import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import code.MD5.interfaces.mesh.IJoint;
import code.MD5.interfaces.mesh.primitive.IWeight;

/**
 * <code>Weight</code> defines the concrete implementation of a weight.
 * <p>
 * <code>Weight</code> should not be cloned directly. The cloning
 * process of a <code>Weight</code> should be initiated by the cloning
 * process of the parent <code>IMesh</code>.
 * <p>
 * This class is used internally by <code>MD5Importer</code> only.
 * 
 * @author Yi Wang (Neakor)
 * @version Modified date: 11-19-2008 17:13 EST
 */
public class Weight implements Serializable, IWeight {
	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = 4719214599414606855L;
	/**
	 * The <code>Integer</code> index value.
	 */
	private int index;
	/**
	 * The <code>IJoint</code> this weight affects.
	 */
	private IJoint joint;
	/**
	 * The fixed <code>Float</code> weight value.
	 */
	private float value;
	/**
	 * The fixed <code>Vector3f</code> position vector.
	 */
	private Vector3f position;

	/**
	 * Constructor of <code>Weight</code>.
	 */
	public Weight() {
		this(-1, -1, null);
	}
	
	/**
	 * Constructor of <code>Weight</code>.
	 * @param index The <code>Integer</code> index value.
	 * @param value The fixed <code>Float</code> weight value.
	 * @param position The fixed <code>Vector3f</code> position vector.
	 */
	public Weight(int index, float value, Vector3f position) {
		this.index = index;
		this.value = value;
		this.position = position;
	}

	@Override
	public void setJoint(IJoint joint) {
		this.joint = joint;
	}

	@Override
	public float getWeightValue() {
		return this.value;
	}

	@Override
	public Vector3f getPosition() {
		return this.position;
	}

	@Override
	public IJoint getJoint() {
		return this.joint;
	}

	@Override
	public int getIndex() {
		return this.index;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class getClassTag() {
		return Weight.class;
	}

	@Override
	public void write(JMEExporter ex) throws IOException {
		OutputCapsule oc = ex.getCapsule(this);
		oc.write(this.index, "Index", -1);
		oc.write(this.joint, "Joint", null);
		oc.write(this.value, "Value", 0);
		oc.write(this.position, "Position", null);
	}

	@Override
	public void read(JMEImporter im) throws IOException {
		InputCapsule ic = im.getCapsule(this);
		this.index = ic.readInt("Index", -1);
		this.joint = (IJoint)ic.readSavable("Joint", null);
		this.value = ic.readFloat("Value", 0);
		this.position = (Vector3f)ic.readSavable("Position", null);
	}

	@Override
	public IWeight clone(IJoint[] clonedJoints) {
		IWeight cloned = new Weight(this.index, this.value, this.position.clone());
		cloned.setJoint(clonedJoints[this.joint.getIndex()]);
		return cloned;
	}
}
