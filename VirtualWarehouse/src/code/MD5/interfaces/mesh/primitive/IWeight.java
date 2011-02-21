package code.MD5.interfaces.mesh.primitive;

import com.jme.math.Vector3f;
import com.jme.util.export.Savable;
import code.MD5.interfaces.mesh.IJoint;

/**
 * <code>IWeight</code> defines the interface of a weight that affects
 * a single joint in the mesh based a weight value and position.
 *
 * @author Yi Wang (Neakor)
 * @version Creation date: 11-17-2008 21:39 EST
 * @version Modified date: 11-19-2008 17:11 EST
 */
public interface IWeight extends Savable {
	
	/**
	 * Set the joint this weight affects.
	 * @param joint The <code>IJoint</code> instance.
	 */
	public void setJoint(IJoint joint);

	/**
	 * Retrieve the fixed weight value of this <code>Weight</code>.
	 * @return The float weight value.
	 */
	public float getWeightValue();

	/**
	 * Retrieve the fixed position of this <code>Weight</code>.
	 * @return The <code>Vector3f</code> position of this <code>Weight</code>.
	 */
	public Vector3f getPosition();

	/**
	 * Retrieve the index of the <code>Joint</code> this <code>Weight</code> affects.
	 * @return The index number of the <code>Joint</code>.
	 */
	public IJoint getJoint();
	
	/**
	 * Retrieve the index value of this weight.
	 * @return The <code>Integer</code> index value.
	 */
	public int getIndex();

	/**
	 * Clone this weight.
	 * @param clonedJoints The array of cloned <code>IJoint</code>.
	 * @return The cloned <code>IWeight</code> instance.
	 */
	public IWeight clone(IJoint[] clonedJoints);
}