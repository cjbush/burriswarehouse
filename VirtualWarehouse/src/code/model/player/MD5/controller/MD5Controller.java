package code.model.player.MD5.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;
import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.jme.util.export.Savable;
import code.model.player.MD5.interfaces.IMD5Animation;
import code.model.player.MD5.interfaces.IMD5Controller;
import code.model.player.MD5.interfaces.IMD5Node;
import code.model.player.MD5.interfaces.mesh.IJoint;

/**
 * <code>MD5Controller</code> defines the concrete implementation
 * of a MD5 controller unit.
 * <p>
 * <code>MD5Controller</code> interpolates the previous and next
 * frame then updates the skeleton with interpolated translation
 * and orientation values.
 * 
 * @author Yi Wang (Neakor)
 * @version Modified date: 02-20-2009 20:20 EST
 */
public class MD5Controller extends Controller implements IMD5Controller {
	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = 1029065355427370006L;
	/**
	 * The <code>Logger</code> instance.
	 */
	private static final Logger logger = Logger.getLogger(MD5Controller.class.getName());
	/**
	 * The update <code>Object</code> lock.
	 */
	private final Object updateLock;
	/**
	 * The modify <code>Object</code> lock.
	 */
	private final Object modifyLock;
	/**
	 * The <code>Float</code> total time elapsed in the current cycle.
	 */
	private float time;
	/**
	 * The <code>IMD5Node</code> instance.
	 */
	private IMD5Node node;
	/**
	 * The array of <code>IJoint</code> this controller controls.
	 */
	private IJoint[] joints;
	/**
	 * The current active <code>IMD5Animation</code>.
	 */
	private IMD5Animation activeAnimation;
	/**
	 * The <code>Map</code> of controlled <code>IMD5Animation</code>.
	 */
	private Map<String, IMD5Animation> animations;
	/**
	 * The <code>Float</code> temporary interpolation value.
	 */
	private float interpolation;
	/**
	 * The <code>Vector3f</code> temporary translation.
	 */
	private final Vector3f translation;
	/**
	 * The <code>Quaternion</code> temporary orientation.
	 */
	private final Quaternion orientation;
	/**
	 * The flag indicates if fading is in process.
	 */
	private boolean fading;
	/**
	 * The <code>Float</code> fading duration value in seconds.
	 */
	private float duration;
	/**
	 * The array of <code>Vector3f</code> translations used for fading.
	 */
	private Vector3f[] translations;
	/**
	 * The array of <code>Quaternion</code> orientations used for fading.
	 */
	private Quaternion[] orientations;
	/**
	 * The flag indicates if fading should scale with controller speed.
	 */
	private boolean scale;
	
	/**
	 * Constructor of <code>MD5Controller</code>.
	 */
	public MD5Controller(){
		this.updateLock = new Object();
		this.modifyLock = new Object();
		this.translation = new Vector3f();
		this.orientation = new Quaternion();
	}

	/**
	 * Constructor of <code>MD5Controller</code>.
	 * @param node The <code>IMD5Node</code> to be controlled.
	 */
	public MD5Controller(IMD5Node node) {
		this.node = node;
		this.updateLock = new Object();
		this.modifyLock = new Object();
		this.joints = this.node.getJoints();
		this.animations = new HashMap<String, IMD5Animation>();
		this.translation = new Vector3f();
		this.orientation = new Quaternion();
	}

	@Override
	public void update(float time) {
		if(this.activeAnimation == null) return;
		this.updateTime(time);
		synchronized(this.updateLock) {
			if(!this.fading) {
				this.activeAnimation.update(time, this.getRepeatType(), this.getSpeed());
				this.updateJoints();
			}
			else this.updateFading();
		}
	}

	/**
	 * Update the total time elapsed with given value based on the repeat type. The
	 * time is reseted after one cycle of the animation is completed.
	 * @param time The time between the last update and the current one.
	 */
	private void updateTime(float time) {
		if(this.activeAnimation != null) {
			if(this.fading) {
				if(this.scale) this.time += time * this.getSpeed();
				else this.time += time;
				return;
			}
			switch(this.getRepeatType()) {
			case Controller.RT_WRAP:
				this.time += time * this.getSpeed();
				if(this.activeAnimation.isCyleComplete()) this.time = 0.0f;
				break;
			case Controller.RT_CLAMP:
				this.time += time * this.getSpeed();
				if(this.activeAnimation.isCyleComplete()) this.time = 0.0f;
				break;
			case Controller.RT_CYCLE:
				if(!this.activeAnimation.isBackward()) this.time += time * this.getSpeed();
				else this.time -= time * this.getSpeed();
				if(this.activeAnimation.isCyleComplete()) {
					if(!this.activeAnimation.isBackward()) this.time = 0;
					else this.time = this.activeAnimation.getAnimationTime();
				}
				break;
			}
		}
	}

	/**
	 * Update the skeleton during normal animating process.
	 */
	private void updateJoints() {
		this.interpolation = this.getInterpolation();
		for(int i = 0; i < this.joints.length; i++) {
			this.translation.interpolate(this.activeAnimation.getPreviousFrame().getTranslation(i),
					this.activeAnimation.getNextFrame().getTranslation(i), this.interpolation);
			this.orientation.slerp(this.activeAnimation.getPreviousFrame().getOrientation(i),
					this.activeAnimation.getNextFrame().getOrientation(i), this.interpolation);
			this.joints[i].updateTransform(this.translation, this.orientation);
		}
		this.node.flagUpdate();
	}

	/**
	 * Update the fading process.
	 */
	private void updateFading() {
		this.interpolation = this.time/this.duration;
		for(int i = 0; i < this.joints.length; i++) {
			this.translation.interpolate(this.translations[i], this.activeAnimation.getPreviousFrame().getTranslation(i), this.interpolation);
			this.orientation.slerp(this.orientations[i], this.activeAnimation.getPreviousFrame().getOrientation(i), this.interpolation);
			this.joints[i].updateTransform(this.translation, this.orientation);
		}
		if(this.interpolation >= 1) {
			this.fading = false;
			this.time = 0;
		}
		this.node.flagUpdate();
	}

	/**
	 * Retrieve the frame interpolation value.
	 * @return The <code>Float</code> interpolation value.
	 */
	private float getInterpolation() {
		float prev = this.activeAnimation.getPreviousTime();
		float next = this.activeAnimation.getNextTime();
		if(prev == next) return 0.0f;
		float interpolation = (this.time - prev) / (next - prev);
		// Add 1 if it is playing backwards.
		if(this.activeAnimation.isBackward()) interpolation = 1 + interpolation;
		if(interpolation < 0.0f) return 0.0f;
		else if (interpolation > 1.0f) return 1.0f;
		else return interpolation;
	}

	/**
	 * Validate the given animation with controlled skeleton.
	 * @param animation The <code>IMD5Animation</code> to be validated.
	 * @return True if the given animation is usable with the skeleton. False otherwise.
	 */
	private boolean validateAnimation(IMD5Animation animation) {
		if(this.joints.length != animation.getJointIDs().length) return false;
		else {
			boolean result = true;
			for(int i = 0; i < this.joints.length && result; i++) {
				result = this.joints[i].getName().equals(animation.getJointIDs()[i]);
			}
			return result;
		}
	}

	@Override
	public void addAnimation(IMD5Animation animation) {
		if(this.animations.containsKey(animation.getName())) return;
		if(this.validateAnimation(animation)) {
			synchronized(this.modifyLock) {
				this.animations.put(animation.getName(), animation);
				if(this.activeAnimation == null) this.fadeTo(animation, 0, true);
			}
		}
		else throw new IllegalArgumentException("Animation does not match skeleton system.");
	}

	@Override
	public void removeAnimation(IMD5Animation animation) {
		synchronized(this.modifyLock) {
			this.animations.remove(animation.getName());
			if(animation == this.activeAnimation) {
				synchronized(this.updateLock) {
					this.activeAnimation = null;
				}
			}
		}
	}

	@Override
	public void fadeTo(String name, float duration, boolean scale) {
		this.fadeTo(this.animations.get(name), duration, scale);
	}

	@Override
	public void fadeTo(IMD5Animation animation, float duration, boolean scale) {
		this.setActiveAnimation(animation);
		this.enabledFading(duration, scale);
	}

	/**
	 * Enable fading between the current frame and the new active animation.
	 * @param duration The <code>Float</code> fading duration in seconds.
	 */
	private void enabledFading(float duration, boolean scale) {
		synchronized(this.updateLock) {
			this.activeAnimation.reset();
			this.fading = true;
			this.duration = FastMath.abs(duration);
			this.scale = scale;
			this.time = 0;
			if(this.translations == null && this.orientations == null) {
				this.translations = new Vector3f[this.joints.length];
				this.orientations = new Quaternion[this.joints.length];
				for(int i = 0; i < this.joints.length; i++) {
					this.translations[i] = this.joints[i].getTranslation().clone();
					this.orientations[i] = this.joints[i].getOrientation().clone();
				}
			} else {
				for(int i = 0; i < this.joints.length; i++) {
					this.translations[i].set(this.joints[i].getTranslation());
					this.orientations[i].set(this.joints[i].getOrientation());
				}
			}
		}
	}

	/**
	 * Set the given animation to the be active animation.
	 * @param animation The <code>IMD5Animation</code> to be set.
	 */
	private void setActiveAnimation(IMD5Animation animation) {
		synchronized(this.updateLock) {
			if(animation == null) MD5Controller.logger.info("Given animation is null.");
			else this.addAnimation(animation);
			this.activeAnimation = animation;
		}
	}

	@Override
	public IMD5Animation getActiveAnimation() {
		return this.activeAnimation;
	}

	@Override
	public Collection<IMD5Animation> getAnimations() {
		return this.animations.values();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class getClassTag() {
		return MD5Controller.class;
	}

	@Override
	public void write(JMEExporter e) throws IOException {
		super.write(e);
		OutputCapsule oc = e.getCapsule(this);
		oc.write(this.node, "Node", null);
		oc.write(this.joints, "Joints", null);
		oc.write(this.activeAnimation, "ActiveAnimation", null);
		oc.writeStringSavableMap(this.animations, "Animations", null);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void read(JMEImporter e) throws IOException {
		super.read(e);
		InputCapsule ic = e.getCapsule(this);
		this.node = (IMD5Node)ic.readSavable("Node", null);
		Savable[] temp = ic.readSavableArray("Joints", null);
		this.joints = new IJoint[temp.length];
		for(int i = 0; i < temp.length; i++) {
			this.joints[i] = (IJoint)temp[i];
		}
		this.activeAnimation = (IMD5Animation)ic.readSavable("ActiveAnimation", null);
		this.animations = (HashMap<String, IMD5Animation>)ic.readStringSavableMap("Animations", null);
	}
}
