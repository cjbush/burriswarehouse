package code.model.player.MD5.interfaces.mesh;

import com.jme.math.Quaternion;
import com.jme.math.TransformMatrix;
import com.jme.math.Vector3f;
import com.jme.util.export.Savable;

/**
 * <code>IJoint</code> defines the interface of a joint in a skeleton
 * system. It maintains its own space position that is then translated
 * into vertex positions for all vertices that are affected by this
 * <code>IJoint</code>.
 *
 * @author Yi Wang (Neakor)
 * @version Creation date: 11-17-2008 19:59 EST
 * @version Modified date: 11-19-2008 15:34 EST
 */
public interface IJoint extends Savable {

	/**
	 * Update the translation and orientation of this joint.
	 * @param translation The new <code>Vector3f</code> translation value.
	 * @param orientation The new </code>Quaternion</code> orientation value.
	 */
	public void updateTransform(Vector3f translation, Quaternion orientation);

	/**
	 * Process the translation and orientation of this joint This process
	 * has to be started from the bottom of skeleton tree up to the root joint.
	 */
	public void processTransform();

	/**
	 * Process the relative transforms of this joint.
	 */
	public void processRelative();

	/**
	 * Set the parent joint of this one.
	 * @param parent The parent <code>IJoint</code> instance.
	 */
	public void setParent(IJoint parent);

	/**
	 * Set the super parent of this joint in the parent model node.
	 * @param superParent The super parent <code>IJoint</code> instance.
	 */
	public void setSuperParent(IJoint superParent);

	/**
	 * Retrieve the translation of this joint read from MD5 file.
	 * @return The <code>Vector3f</code> translation read directly from MD5 file.
	 */
	public Vector3f getTranslation();

	/**
	 * Retrieve the orientation of this joint read from MD5 file.
	 * @return The <code>Quaternion</code> orientation read directly from MD5 file.
	 */
	public Quaternion getOrientation();

	/**
	 * Retrieve the relative <code>TransformMatrix</code> of this <code>Joint</code>.
	 * @return The relative <code>TransformMatrix</code> of this <code>Joint</code>.
	 */
	public TransformMatrix getTransform();

	/**
	 * Retrieve the super parent of this joint.
	 * @return The super parent <code>IJoint</code> instance.
	 */
	public IJoint getSuperParent();
	
	/**
	 * Retrieve the parent joint.
	 * @return The parent <code>IJoint</code> instance.
	 */
	public IJoint getParent();

	/**
	 * Retrieve the name ID of this joint.
	 * @return The <code>String</code> name ID.
	 */
	public String getName();
	
	/**
	 * Retrieve the index value.
	 * @return The <code>Integer</code> index.
	 */
	public int getIndex();

	/**
	 * Clone this join.
	 * @return The cloned copy of this <code>IJoint</code>
	 */
	public IJoint clone();
}