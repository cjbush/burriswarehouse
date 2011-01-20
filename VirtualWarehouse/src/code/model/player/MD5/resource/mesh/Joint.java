package code.model.player.MD5.resource.mesh;

import java.io.IOException;
import java.io.Serializable;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.TransformMatrix;
import com.jme.math.Vector3f;
import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import code.model.player.MD5.interfaces.mesh.IJoint;

/**
 * <code>Joint</code> defines the concrete implementation of a joint
 * in the skeleton system of a model.
 * <p>
 * <code>Joint</code> cannot be cloned directly. The cloning process
 * of a <code>Joint</code> can only be initiated by the cloning
 * process of the parent <code>IModelNode</code>.
 * <p>
 * This class is used internally by <code>MD5Importer</code> only.
 * 
 * @author Yi Wang (Neakor)
 * @version Modified date: 11-19-2008 15:34 EST
 */
public class Joint implements Serializable, IJoint {
	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = -926371530130383637L;
	/**
	 * The <code>Integer</code> index.
	 */
	private int index;
	/**
	 * The name ID of the <code>IJoint</code>.
	 */
	private String name;
	/**
	 * The parent <code>IJoint</code> instance.
	 */
	private IJoint parent;
	/**
	 * The super parent <code>IJoint</code> instance.
	 */
	private IJoint superParent;
	/**
	 * The <code>Vector3f</code> translation value.
	 */
	private Vector3f translation;
	/**
	 * The <code>Quaternion</code> orientation value.
	 */
	private Quaternion orientation;
	/**
	 * The relative <code>TransformMatrix</code> of this joint to its parent.
	 */
	private TransformMatrix transform;
	/**
	 * The base <code>TransformMatrix</code> instance.
	 */
	private final TransformMatrix baseTransform;
	/**
	 * The temporary <code>TransformMatric</code> for updating relative transform.
	 */
	private final TransformMatrix tempTransform;
	/**
	 * The temporary <code>Vector3f</code> for updating relative transform.
	 */
	private final Vector3f tempVector;

	/**
	 * Constructor of <code>Joint</code>.
	 */
	public Joint() {
		super();
		this.baseTransform = new TransformMatrix();
		this.tempTransform = new TransformMatrix();
		this.tempVector = new Vector3f();
	}

	/**
	 * Constructor of <code>Joint</code>.
	 * @param index The <code>Integer</code> index.
	 * @param name The <code>String</code> name ID.
	 * @param translation The <code>Vector3f</code> translation value.
	 * @param orientation The un-processed <code>Vector3f</code> orientation value.
	 */
	public Joint(int index, String name, Vector3f translation, Vector3f orientation) {
		this.index = index;
		this.name = name;
		this.translation = translation;
		this.orientation = new Quaternion();
		this.orientation.x = orientation.x;
		this.orientation.y = orientation.y;
		this.orientation.z = orientation.z;
		float t = 1.0f-(this.orientation.x*this.orientation.x)-(this.orientation.y*this.orientation.y)-(this.orientation.z*this.orientation.z);
		if (t < 0.0f) this.orientation.w = 0.0f;
		else this.orientation.w = -(FastMath.sqrt(t));
		this.transform = new TransformMatrix();
		this.baseTransform = new TransformMatrix();
		this.tempTransform = new TransformMatrix();
		this.tempVector = new Vector3f();
	}
	
	/**
	 * Constructor of <code>Joint</code>.
	 * @param index The <code>Integer</code> index.
	 * @param name The <code>String</code> name ID.
	 * @param translation The <code>Vector3f</code> translation value.
	 * @param orientation The <code>Quaternion</code> orientation value.
	 */
	private Joint(int index, String name, Vector3f translation, Quaternion orientation, TransformMatrix transform) {
		this.index = index;
		this.name = name;
		this.translation = translation;
		this.orientation = orientation;
		this.transform = transform;
		this.baseTransform = new TransformMatrix();
		this.tempTransform = new TransformMatrix();
		this.tempVector = new Vector3f();
	}

	@Override
	public void updateTransform(Vector3f translation, Quaternion orientation) {
		this.translation.set(translation);
		this.orientation.set(orientation);
	}
	
	@Override
	public void processTransform() {
		Vector3f parentTrans = null;
		Quaternion parentOrien = null;
		if(this.parent == null) {
			parentTrans = new Vector3f();
			parentOrien = new Quaternion();
		} else {
			parentTrans = this.parent.getTranslation();
			parentOrien = this.parent.getOrientation();
		}
		this.orientation.set(parentOrien.inverse().multLocal(this.orientation));
		this.translation.subtractLocal(parentTrans);
		parentOrien.inverse().multLocal(this.translation);
	}

	@Override
	public void processRelative() {
		this.transform.loadIdentity();
		if(this.parent != null) this.transform.set(this.parent.getTransform());
		else this.transform.set(this.getBaseTransform());
		this.tempTransform.set(this.orientation, this.translation);
		this.transform.multLocal(this.tempTransform, this.tempVector);
	}

	/**
	 * Get the base transform of the parent <code>Joint</code> in either the parent
	 * <code>ModelNode</code> or the local <code>ModelNode</code>.
	 * @return The base <code>TransformMatrix</code>.
	 */
	private TransformMatrix getBaseTransform() {
		if(this.superParent == null) this.baseTransform.loadIdentity();
		else {
			this.baseTransform.loadIdentity();
			this.baseTransform.combineWithParent(this.superParent.getTransform());
		}
		return this.baseTransform;
	}

	@Override
	public void setParent(IJoint parent) {
		this.parent = parent;
	}

	@Override
	public void setSuperParent(IJoint superParent) {
		this.superParent = superParent;
	}

	@Override
	public Vector3f getTranslation() {
		return this.translation;
	}

	@Override
	public Quaternion getOrientation() {
		return this.orientation;
	}

	@Override
	public TransformMatrix getTransform() {
		return this.transform;
	}
	
	@Override
	public IJoint getSuperParent() {
		return this.superParent;
	}

	@Override
	public IJoint getParent() {
		return this.parent;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getIndex() {
		return this.index;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class getClassTag() {
		return Joint.class;
	}

	@Override
	public void write(JMEExporter ex) throws IOException {
		OutputCapsule oc = ex.getCapsule(this);
		oc.write(this.index, "Index", -1);
		oc.write(this.name, "Name", null);
		oc.write(this.parent, "Parent", null);
		oc.write(this.superParent, "SuperParent", null);
		oc.write(this.translation, "Translation", null);
		oc.write(this.orientation, "Orientation", null);
		oc.write(this.transform, "Transform", null);
	}

	@Override
	public void read(JMEImporter im) throws IOException {
		InputCapsule ic = im.getCapsule(this);
		this.index = ic.readInt("Index", -1);
		this.name = ic.readString("Name", null);
		this.parent = (IJoint)ic.readSavable("Parent", null);
		this.superParent = (IJoint)ic.readSavable("SuperParent", null);
		this.translation = (Vector3f)ic.readSavable("Translation", null);
		this.orientation = (Quaternion)ic.readSavable("Orientation", null);
		this.transform = (TransformMatrix)ic.readSavable("Transform", null);
	}

	@Override
	public IJoint clone() {
		return new Joint(this.index, new String(this.name), this.translation.clone(), this.orientation.clone(), this.transform.clone());
	}
}
