package code.model.player.MD5.resource.anim;

import java.io.IOException;
import java.io.Serializable;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.jme.util.export.Savable;
import code.model.player.MD5.interfaces.anim.IFrame;

/**
 * <code>Frame</code> defines the concrete implementation of a frame.
 * <p>
 * <code>Frame</code> should not be cloned directly. The cloning
 * process of a <code>Frame</code> should be initiated by the cloning
 * process of the parent* <code>IMD5Animation</code>.
 * <p>
 * This class is used internally by <code>MD5Importer</code> only.
 * 
 * @author Yi Wang (Neakor)
 * @version Modified date: 11-18-2008 23:19 EST
 */
public class Frame implements Serializable, IFrame {
	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = 8891271219195292580L;
	/**
	 * The array of <code>Vector3f</code> translations.
	 */
	protected Vector3f[] translations;
	/**
	 * The array of <code>Quaternion</code> orientations.
	 */
	protected Quaternion[] orientations;

	/**
	 * Constructor of <code>Frame</code>.
	 */
	public Frame() {
		super();
	}

	/**
	 * Constructor of <code>Frame</code>.
	 * @param numJoints The <code>Integer</code> number of joints.
	 */
	public Frame(int numJoints) {
		this.translations = new Vector3f[numJoints];
		this.orientations = new Quaternion[numJoints];
		for(int i = 0; i < this.translations.length && i < this.orientations.length; i++) {
			this.translations[i] = new Vector3f();
			this.orientations[i] = new Quaternion();
		}
	}
	
	/**
	 * Constructor of <code>Frame</code>.
	 * @param translations The array of <code>Vector3f</code> translations.
	 * @param orientations The array of <code>Quaternion</code> orientations.
	 */
	protected Frame(Vector3f[] translations, Quaternion[] orientations) {
		this.translations = translations;
		this.orientations = orientations;
	}
	
	/**
	 * Set the transform of this <code>Frame</code>.
	 * @param jointIndex The index of the <code>Joint</code>.
	 * @param index The transform index number.
	 * @param value The transform value to be set.
	 */
	public void setTransform(int jointIndex, int index, float value) {
		switch(index) {
		case 0: this.translations[jointIndex].x = value; break;
		case 1: this.translations[jointIndex].y = value; break;
		case 2: this.translations[jointIndex].z = value; break;
		case 3: this.orientations[jointIndex].x = value; break;
		case 4: this.orientations[jointIndex].y = value; break;
		case 5:
			this.orientations[jointIndex].z = value;
			this.processOrientation(this.orientations[jointIndex]);
			break;
		}
	}
	
	/**
	 * Process the orientation to finalize it.
	 * @param raw The raw orientation value.
	 */
	private void processOrientation(Quaternion raw) {
		float t = 1.0f - (raw.x * raw.x) - (raw.y * raw.y) - (raw.z * raw.z);
		if (t < 0.0f) raw.w = 0.0f;
		else raw.w = -(FastMath.sqrt(t));
	}


	@Override
	public Vector3f getTranslation(int jointIndex) {
		return this.translations[jointIndex];
	}

	@Override
	public Quaternion getOrientation(int jointIndex) {
		return this.orientations[jointIndex];
	}

	@Override
	public float getTransformValue(int jointIndex, int transIndex) {
		switch(transIndex) {
		case 0: return this.translations[jointIndex].x;
		case 1: return this.translations[jointIndex].y;
		case 2: return this.translations[jointIndex].z;
		case 3: return this.orientations[jointIndex].x;
		case 4: return this.orientations[jointIndex].y;
		case 5: return this.orientations[jointIndex].z;
		default: return 0;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class getClassTag() {
		return Frame.class;
	}

	@Override
	public void write(JMEExporter ex) throws IOException {
		OutputCapsule oc = ex.getCapsule(this);
		oc.write(this.translations, "Translations", null);
		oc.write(this.orientations, "Orientations", null);
	}

	@Override
	public void read(JMEImporter im) throws IOException {
		Savable[] temp = null;
		InputCapsule ic = im.getCapsule(this);
		temp = ic.readSavableArray("Translations", null);
		this.translations = new Vector3f[temp.length];
		for(int i = 0; i < temp.length; i++) {
			this.translations[i] = (Vector3f)temp[i];
		}
		temp = ic.readSavableArray("Orientations", null);
		this.orientations = new Quaternion[temp.length];
		for(int i = 0; i < temp.length; i++) {
			this.orientations[i] = (Quaternion)temp[i];
		}
	}

	@Override
	public IFrame clone() {
		Vector3f[] clonedTranslations = new Vector3f[this.translations.length];
		for(int i = 0; i < clonedTranslations.length; i++) clonedTranslations[i] = this.translations[i].clone();
		Quaternion[] clonedOrientations = new Quaternion[this.orientations.length];
		for(int i = 0; i < clonedOrientations.length; i++) clonedOrientations[i] = this.orientations[i].clone();
		return new Frame(clonedTranslations, clonedOrientations);
	}
}
