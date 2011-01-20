package code.model.player.MD5.interfaces.anim;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.util.export.Savable;

/**
 * <code>IFrame</code> defines the interface of a single frame of a
 * skeletal animation. It maintains the translation and orientation
 * of the entire skeleton at a particular frame in the animation.
 *
 * @author Yi Wang (Neakor)
 * @version Creation date: 11-17-2008 22:02 EST
 * @version Modified date: 11-17-2008 22:12 EST
 */
public interface IFrame extends Savable {

	/**
	 * Set the transform of this frame.
	 * @param jointIndex The <code>Integer</code> index of the joint.
	 * @param index The <code>Integer</code> transform index number.
	 * @param value The <code>Float</code> transform value to be set.
	 */
	public void setTransform(int jointIndex, int index, float value);
	
	/**
	 * Retrieve the translation of the <code>Joint</code> with given index.
	 * @param jointIndex The <code>Joint</code> index number.
	 * @return The <code>Vector3f</code> translation value.
	 */
	public Vector3f getTranslation(int jointIndex);

	/**
	 * Retrieve the orientation of the <code>Joint</code> with given index.
	 * @param jointIndex The <code>Joint</code> number.
	 * @return The <code>Quaternion</code> orientation value.
	 */
	public Quaternion getOrientation(int jointIndex);
	
	/**
	 * Retrieve the transform value with given indices.
	 * @param jointIndex The <code>Integer</code> joint index.
	 * @param transIndex The <code>Integer</code> transform index.
	 * @return The <code>Float</code> transform value.
	 */
	public float getTransformValue(int jointIndex, int transIndex);

	/**
	 * Clone this frame.
	 * @return The cloned <code>IFrame</code> instance.
	 */
	public IFrame clone();
}