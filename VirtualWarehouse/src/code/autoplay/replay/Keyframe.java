/**
 * 
 */
package code.autoplay.replay;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

/**
 * <b>Note:</b> This class is somewhat based on the nested class:
 * <code>com.jme.animation.SpatialTransformer.PointInTime</code>. Used to store the
 * position, rotation, and scale for a given point in time, known as a "keyframe."
 * 
 * @author Jordan Hinshaw
 *
 */
public class Keyframe {

	protected Quaternion rot;
	protected Vector3f pos;
	protected Vector3f scale;
	protected float time;
	
	public Keyframe(float time, Vector3f position, Quaternion rotation, Vector3f scale) {
		pos = position;
		rot = rotation;
		this.scale = scale;
		this.time = time;
	}

	/**
	 * @return the rotation for this keyframe
	 */
	public Quaternion getRot() {
		return rot;
	}


	/**
	 * @return the position for this keyframe
	 */
	public Vector3f getPos() {
		return pos;
	}


	/**
	 * @return the scale for this keyframe
	 */
	public Vector3f getScale() {
		return scale;
	}


	/**
	 * @return the time for this keyframe
	 */
	public float getTime() {
		return time;
	}
	
	
}
