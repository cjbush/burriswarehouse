package code.model.player.MD5;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import com.jme.scene.Controller;
import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.jme.util.export.Savable;
import code.model.player.MD5.interfaces.IMD5Animation;
import code.model.player.MD5.interfaces.anim.IFrame;

/**
 * <code>MD5Animation</code> is the final product of MD5 animation.
 * <p>
 * <code>MD5Animation</code> is added to a <code>IMD5Controller</code>
 * for animating the skeletal <code>IMD5Node</code>.
 * <p>
 * <code>MD5Animation</code> provides the cloning functionality so
 * that users can fast clone animations that may be used by multiple
 * <code>IMD5Node</code>. The newly cloned <code>MD5Animation</code>
 * is initialized and ready to be used.
 *
 * @author Yi Wang (Neakor)
 * @version Modified date: 02-19-2009 21:36 EST
 */
public class MD5Animation implements Serializable, IMD5Animation {
	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = 3646737896444759738L;
	/**
	 * The <code>String</code> name of this animation.
	 */
	private String name;
	/**
	 * The <code>String</code> joint IDs.
	 */
	private String[] jointIDs;
	/**
	 * The array of key <code>IFrame</code>.
	 */
	private IFrame[] frames;
	/**
	 * The <code>Float</code> frame rate.
	 */
	private float frameRate;
	/**
	 * The array of <code>Float</code> starting time of each frame.
	 */
	private float[] frameTimes;
	/**
	 * The flag indicates if this animation is being played backwards.
	 */
	private boolean backward;
	/**
	 * The time elapsed since last change in key frame.
	 */
	private float time;
	/**
	 * The index of the previous frame.
	 */
	private int prevFrame;
	/**
	 * The index of the next frame.
	 */
	private int nextFrame;
	/**
	 * The flag indicates if this cycle is completed but the new cycle has not yet started.
	 */
	private boolean complete;
	/**
	 * The children <code>IMD5Animation</code>.
	 */
	private ArrayList<IMD5Animation> animations;

	/**
	 * Constructor of <code>MD5Animation</code>.
	 */
	public MD5Animation() {
		this.prevFrame = 0;
		this.nextFrame = 1;
	}

	/**
	 * Constructor of <code>MD5Animation</code>.
	 * @param name The <code>String</code> name of this animation.
	 * @param IDs The <code>String</code> joint IDs.
	 * @param frames The array of key <code>IFrame</code>.
	 * @param framerate The <code>Float</code> frame rate.
	 */
	public MD5Animation(String name, String[] IDs, IFrame[] frames, float framerate) {
		this.name = name;
		this.jointIDs = IDs;
		this.frames = frames;
		this.frameRate = framerate;
		final float timeperframe = 1.0f/this.frameRate;
		this.frameTimes = new float[this.frames.length];
		for(int i = 0; i < this.frameTimes.length; i++) {
			this.frameTimes[i] = (float)i * timeperframe;
		}
		this.prevFrame = 0;
		this.nextFrame = 1;
	}

	@Override
	public void update(float time, int repeat, float speed) {
		if(this.complete && repeat != Controller.RT_CLAMP) this.complete = false;
		switch(repeat) {
		case Controller.RT_CLAMP:
			this.updateClamp(time, speed);
			break;
		case Controller.RT_CYCLE:
			this.updateCycle(time, speed);
			break;
		case Controller.RT_WRAP:
			this.updateWrap(time, speed);
			break;
		}
		if(this.animations != null) {
			for(IMD5Animation anim : this.animations) {
				anim.update(time, repeat, speed);
			}
		}
	}

	/**
	 * Update frame index when the wrap mode is set to clamp.
	 * @param time The <code>Float</code> time between last update and the current one.
	 * @param speed The speed of the <code>Controller</code>.
	 */
	private void updateClamp(float time, float speed) {
		if(this.complete) return;
		this.time = this.time + (time * speed);
		while(this.time >= this.frameTimes[this.nextFrame]) {
			this.nextFrame++;
			this.prevFrame = this.nextFrame - 1;
			if(this.nextFrame > this.frames.length - 1) {
				this.nextFrame = this.frames.length - 1;
				this.prevFrame = this.frames.length - 2;
				this.complete = true;
				this.time = 0.0f;
				break;
			}
		}
	}

	/**
	 * Update frame index when the wrap mode is set to cycle.
	 * @param time The <code>Float</code> time between last update and the current one.
	 * @param speed The speed of the <code>Controller</code>.
	 */
	private void updateCycle(float time, float speed) {
		if(!this.backward) {
			this.time = this.time + (time * speed);
			while(this.time >= this.frameTimes[this.nextFrame]) {
				this.nextFrame++;
				this.prevFrame = this.nextFrame - 1;
				if(this.nextFrame > this.frames.length - 1) {
					this.backward = true;
					this.prevFrame = this.frames.length - 1;
					this.nextFrame = this.prevFrame - 1;
					this.complete = true;
					this.time = this.frameTimes[this.prevFrame];
					break;
				}
			}
		} else {
			this.time = this.time - (time * speed);
			while(this.time <= this.frameTimes[this.nextFrame]) {
				this.nextFrame--;
				this.prevFrame = this.nextFrame + 1;
				if(this.nextFrame < 0) {
					this.backward = false;
					this.prevFrame = 0;
					this.nextFrame = this.prevFrame + 1;
					this.complete = true;
					this.time = 0.0f;
					break;
				}
			}
		}
	}

	/**
	 * Update frame index when the wrap mode is set to wrap.
	 * @param time The <code>Float</code> time between last update and the current one.
	 * @param speed The speed of the <code>Controller</code>.
	 */
	private void updateWrap(float time, float speed) {
		this.time = this.time + (time * speed);
		if(this.time >= this.frameTimes[this.nextFrame]) {
			this.nextFrame++;
			this.prevFrame = this.nextFrame - 1;
			while(this.nextFrame > this.frames.length - 1) {
				this.prevFrame = 0;
				this.nextFrame = this.prevFrame + 1;
				this.complete = true;
				this.time = 0.0f;
				break;
			}
		}
	}

	@Override
	public void addAnimation(IMD5Animation animation) {
		if(this.animations == null) this.animations = new ArrayList<IMD5Animation>();
		if(!this.animations.contains(animation)) this.animations.add(animation);
	}

	@Override
	public void removeAnimation(IMD5Animation animation) {
		this.animations.remove(animation);
	}

	@Override
	public float getAnimationTime() {
		return (1.0f/this.frameRate)*(float)this.frames.length;
	}

	@Override
	public int getFrameCount() {
		return this.frames.length;
	}

	@Override
	public float getPercentage() {
		return (float)this.nextFrame / (float)this.getFrameCount();
	}

	@Override
	public IFrame getPreviousFrame() {
		return this.frames[this.prevFrame];
	}

	@Override
	public int getPreviousIndex() {
		return this.prevFrame;
	}

	@Override
	public float getPreviousTime() {
		if(this.frameTimes != null) return this.frameTimes[this.prevFrame];
		return ((float)this.prevFrame) * (1.0f/this.frameRate);
	}

	@Override
	public IFrame getNextFrame() {
		return this.frames[this.nextFrame];
	}

	@Override
	public int getNextIndex() {
		return this.nextFrame;
	}

	@Override
	public float getNextTime() {
		if(this.frameTimes != null) return this.frameTimes[this.nextFrame];
		return ((float)this.nextFrame) * (1.0f/this.frameRate);
	}

	@Override
	public String[] getJointIDs() {
		return this.jointIDs;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class getClassTag() {
		return MD5Animation.class;
	}

	@Override
	public boolean isBackward() {
		return this.backward;
	}

	@Override
	public boolean isCyleComplete() {
		return this.complete;
	}

	@Override
	public void write(JMEExporter ex) throws IOException {
		OutputCapsule oc = ex.getCapsule(this);
		oc.write(this.name, "Name", null);
		oc.write(this.jointIDs, "JointIDs", null);
		oc.write(this.frames, "Frames", null);
		oc.write(this.frameRate, "FrameRate", 0);
		oc.write(this.frameTimes, "FrameTimes", null);
		oc.writeSavableArrayList(this.animations, "Animations", null);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void read(JMEImporter im) throws IOException {
		InputCapsule ic = im.getCapsule(this);
		this.name = ic.readString("Name", null);
		this.jointIDs = ic.readStringArray("JointIDs", null);
		Savable[] temp = ic.readSavableArray("Frames", null);
		this.frames = new IFrame[temp.length];
		for(int i = 0; i < temp.length; i++) {
			this.frames[i] = (IFrame)temp[i];
		}
		this.frameRate = ic.readFloat("FrameRate", 0);
		this.frameTimes = ic.readFloatArray("FrameTimes", null);
		this.animations = (ArrayList<IMD5Animation>)ic.readSavableArrayList("Animations", null);
	}

	@Override
	public void reset() {
		this.backward = false;
		this.time = 0;
		this.prevFrame = 0;
		this.nextFrame = 1;
		this.complete = false;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	
	@Override
	public boolean equals(Object object) {
		if(object instanceof MD5Animation) {
			MD5Animation given = (MD5Animation)object;
			return given.name.equals(this.name);
		}
		return false;
	}

	@Override
	public IMD5Animation clone() {
		String[] clonedIDs = new String[this.jointIDs.length];
		for(int i = 0; i < clonedIDs.length; i++) clonedIDs[i] = new String(this.jointIDs[i]);
		IFrame[] clonedFrames = new IFrame[this.frames.length];
		for(int i = 0; i < clonedFrames.length; i++) clonedFrames[i] = this.frames[i].clone();
		MD5Animation clone = new MD5Animation(new String(this.name), clonedIDs, clonedFrames, this.frameRate);
		if(this.animations != null) {
			ArrayList<IMD5Animation> clonedChildren = new ArrayList<IMD5Animation>();
			for(IMD5Animation child : this.animations) clonedChildren.add(child.clone());
		}
		return clone;
	}
}
