/**
 * 
 */
package code.autoplay.replay;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import com.jme.animation.SpatialTransformer;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;
import com.jme.scene.Spatial;
import com.jme.util.Timer;

/**
 * This class defines a <a href="http://www.jmonkeyengine.com/doc/com/jme/scene/Controller.html">
 * Controller</a> that behaves similarly to a 
 * <a href="http://www.jmonkeyengine.com/doc/com/jme/animation/SpatialTransformer.html">
 * SpatialTranformer</a> but can be updated continuously throughout the game play, so that not
 * of the data needs to be known before the game starts.
 * 
 * <b>Date:</b> December 2010
 * @author Jordan Hinshaw
 *
 */
public class UpdatableTransformer extends Controller {

	//Constants
	/**
	 * Since UpdatableTransformer only works on one Spatial, the object index
	 * require by SpatialTransformer will always be 0
	 */
	protected static final int OBJ_INDEX = 0;
	
	/**
	 * Since UpdatableTransformer only works on one Spatial, the parent index
	 * require by SpatialTransformer will always be -1
	 */
	protected static final int PARENT_INDEX = -1;
	
	/**
	 * Since UpdatableTransformer only works on one Spatial, the number of Spatials
	 * that the SpatialTransformer works on will always be 1.
	 */
	protected static final int NUM_OBJS = 1;
	
	//Variables
	
	/**
	 * Stores SpatialTransformers that are have not yet been activated.
	 */
	protected BlockingDeque<SpatialTransformer> preQ;
	
	/**
	 * Stores SpatialTransformers that have already been activated and
	 * are past their maxTime. This would allow for rewinding the replay.
	 */
	protected Deque<SpatialTransformer> postQ;
	
	/**
	 * This is the SpatialTransformer that is currently active. There will 
	 * only be one active SpatialTransformer at a time.
	 */
	protected SpatialTransformer active = null;
	
	/**
	 * Stores the information for the most recently set keyframe. 
	 */
	protected Keyframe prevKeyframe;
	
	protected Spatial obj;
	private float duration;
	private float currBoundary;
	
	private float worldTime = 0;
	private boolean finishedUpdating = false;
	
	/**
	 * Constructor. Creates a new UpdatableTransformer that will control the provided
	 * <code>Spatial</code>. 
	 * 
	 * @param object - the <code>Spatial</code> that this Controller will act on
	 * @param cutoffTime - defines the duration of the internal <code>SpatialTransformers
	 * </code> 
	 */
	public UpdatableTransformer(Spatial object, float cutoffTime) {
		preQ = new LinkedBlockingDeque<SpatialTransformer>();
		postQ = new LinkedList<SpatialTransformer>();
		obj = object;
		duration = cutoffTime;
		currBoundary = cutoffTime;
		prevKeyframe = new Keyframe(-1, null, null, null);
	}
	

	/**
	 * Add sets a new keyframe that represents the position, rotation, and scale for the
	 * controlled model at the given time. Keyframes <b>must</b> be in chronological order
	 * (ie. the second keyframe must occur after the first, and third after the second, 
	 * etc.). Any deviation from chronological ordering will result in a thrown error.
	 * 
	 * @param time - the time for this keyframe 
	 * @param pos - the position for this keyframe
	 * @param rot - the rotation for this keyframe
	 * @param scale - the scale for this keyframe
	 * @throws KeyframeUpdateExpection
	 */
	public void addKeyframe(float time, Vector3f pos, Quaternion rot, Vector3f scale) 
				throws KeyframeUpdateExpection {
		
		SpatialTransformer trans;
		float prevTime = prevKeyframe.getTime();
		
		//the given time cannot be less that the previous given time
		if(time < prevTime) {
			throw new KeyframeUpdateExpection("Given keyframe time: " + time + 
					"precedes previously given keyframe times. Keyframes must be " +
					"chronological in order.");
		}
		else if(prevTime < 0) {
			/*
			 * If prevTime < 0 then this is the first position update given and
			 * a new SpatialTransformer need to be created. 
			 */			
			trans = generateTransformer();
			
			//set the position, rotation, and scale in the transformer
			setValues(trans, time, pos, rot, scale);
			
			//add the transformer to the transformer queue
			preQ.addLast(trans);
		}
		else if(time > currBoundary) {
			/*
			 * If time > currBoundary then the this position update is past the 
			 * cut-off boundary for the current spatialTransformer and a new one 
			 * needs to be created. The first keyframe in the new transformer will
			 * be the same as the last keyframe in the previous one, so that the
			 * information between keyframes will be correctly interpolated
			 */
			
			//call interpolate missing on the previous transformer first
			preQ.getLast().interpolateMissing();
			
			//create and set up new transformer
			trans = generateTransformer();
			
			
			//set the first keyframe of the new transformer to the previous keyframe
			setValues(trans, prevTime, prevKeyframe.getPos(), prevKeyframe.getRot(), 
					prevKeyframe.getScale());
			
			
			//set the new keyframe
			setValues(trans, time, pos, rot, scale);
			
			//add the transformer to the transformer queue
			preQ.addLast(trans);
			
			//update currBoundary
			currBoundary += duration;
			
			//test
			//System.out.println("updated");
		}
		else if( time < currBoundary ) {
			/*
			 * In this case the given time still applies to the latest transformer. So
			 * grab the transformer and update it. 
			 */
			trans = preQ.getLast();
			setValues(trans, time, pos, rot, scale);
		}

		
		//update prevKeyframe
		prevKeyframe = new Keyframe(time, pos, rot, scale);
	}
	
	
	/**
	 * This method lets the UpdatableTransformer no that no more updates will keyframes
	 * will be added. This allows for the animation to clamp at the last keyframe should
	 * the time go beyond the time of the last keyframe.
	 */
	public void finishedUpdates() {
		finishedUpdating = true;
	}
	
	
	/**
	 * Sets the values of the given <code>SpatialTranformer</code>.
	 * 
	 * @param transform - the transformer to modify
	 * @param time - the time of the keyframe
	 * @param pos - the position for the keyframe
	 * @param rot - the rotation for the keyframe
	 * @param scale - the scale for the keyframe
	 */
	protected void setValues(SpatialTransformer transform, float time, Vector3f pos,
			Quaternion rot, Vector3f scale) {
		
		transform.setPosition(OBJ_INDEX, time, pos);
		//transform.setRotation(OBJ_INDEX, time, rot);
		transform.setScale(OBJ_INDEX, time, scale);
	}
	
	/**
	 * Creates a new SpatialTransformer for <code>obj</code>
	 * 
	 * @return a new <code>SpatialTransformer</code>
	 */
	protected SpatialTransformer generateTransformer(){
		SpatialTransformer trans = new SpatialTransformer(NUM_OBJS);
		trans.setObject(obj, OBJ_INDEX, PARENT_INDEX);
		trans.setRepeatType(RT_CLAMP);
		
		return trans;
	}
	
	
	@Override
	public void update(float time) {
		try {
			worldTime += time;
			
			//float maxTime = preQ.getFirst().getMaxTime();

			if(active == null) { 
				//This will only be true the first time update is called
				active = preQ.takeFirst();
				//active = preQ.pollFirst();
				
				obj.addController( active );
			}
			else if(worldTime >= active.getMaxTime() ) {
				/* 
				 * When the game time reaches beyond maxTime of the currently active
				 * controller, remove it and start the next one.
				 */
				if( !finishedUpdating || preQ.size() > 0 ) {
					/*
					 * This condition will NOT be met when the user has signaled that no
					 * more key frames will be added and the last controller has been
					 * pulled from the preQ. Under such conditions, the currently active
					 * controller must stay attached to the Spatial allowed to clamp.
					 */
					obj.removeController(active);
					postQ.addLast( active );
					
					active = preQ.takeFirst();
					//active = preQ.pollFirst();
					
					obj.addController(active);
				}
				
			}
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

		
	
}
