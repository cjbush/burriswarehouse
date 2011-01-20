package code.model.player.MD5.interfaces;

import com.jme.util.export.Savable;
import code.model.player.MD5.interfaces.anim.IFrame;

/**
 * <code>IMD5Animation</code> defines the interface of a completed
 * loaded MD5 animation.
 *
 * @author Yi Wang (Neakor)
 * @version Creation date: 11-17-2008 22:46 EST
 * @version Modified date: 02-19-2009 21:32 EST
 */
public interface IMD5Animation extends Savable {

	/**
	 * Update the frame index based on given values.
	 * @param time The <code>Float</code> frame interpolation.
	 * @param repeat The <code>Controller</code> repeat type.
	 * @param speed The <code>Float</code> speed of the controller.
	 */
	public void update(float time, int repeat, float speed);
	
	/**
	 * Add a child MD5 animation.
	 * @param animation The child <code>IMD5Animation</code> to be added.
	 */
	public void addAnimation(IMD5Animation animation);
	
	/**
	 * Remove the given animation.
	 * @param animation The child <code>IMD5Animation</code> to be removed.
	 */
	public void removeAnimation(IMD5Animation animation);
	
	/**
	 * Retrieve the total time of a complete cycle of this animation.
	 * @return The <code>Float</code> complete cycle time.
	 */
	public float getAnimationTime();
	
	/**
	 * Retrieve the total number of frames.
	 * @return The <code>Integer</code> number of frames.
	 */
	public int getFrameCount();
	
	/**
	 * Get the complete percentage of the current cycle.
	 * @return The <code>Float</code> percentage value.
	 */
	public float getPercentage();

	/**
	 * Retrieve the previous frame.
	 * @return The previous <code>IFrame</code>.
	 */
	public IFrame getPreviousFrame();
	
	/**
	 * Retrieve the previous frame index number.
	 * @return The <code>Integer</code> index number.
	 */
	public int getPreviousIndex();
	
	/**
	 * Retrieve the starting time of the previous frame.
	 * @return The <code>Float</code> starting time.
	 */
	public float getPreviousTime();
	
	/**
	 * Retrieve the next frame.
	 * @return The next <code>IFrame</code>.
	 */
	public IFrame getNextFrame();
	
	/**
	 * Retrieve the next frame index number.
	 * @return The <code>Integer</code> index number.
	 */
	public int getNextIndex();
	
	/**
	 * Retrieve the starting time of the next frame.
	 * @return The <code>Float</code> starting time.
	 */
	public float getNextTime();
	
	/**
	 * Retrieve the IDs of joints of this animation.
	 * @return The array of <code>String</code> IDs.
	 */
	public String[] getJointIDs();
	
	/**
	 * Retrieve the name of this animation.
	 * @return The <code>String</code> name.
	 */
	public String getName();
	
	/**
	 * Check if this animation is being played backward.
	 * @return True if the animation is being played backward. False forward.
	 */
	public boolean isBackward();
	
	/**
	 * Check if one cycle of this animation is complete, but the new one has not yet started.
	 * @return True if one cycle is complete. False otherwise.
	 */
	public boolean isCyleComplete();
	
	/**
	 * Reset the time and frame index information.
	 */
	public void reset();
	
	/**
	 * Clone this animation.
	 * @return The cloned <code>IMD5Animation</code> instance.
	 */
	public IMD5Animation clone();
}
