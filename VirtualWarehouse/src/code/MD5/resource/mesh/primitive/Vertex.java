package code.MD5.resource.mesh.primitive;

import java.io.IOException;
import java.io.Serializable;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.jme.util.export.Savable;
import code.MD5.interfaces.mesh.primitive.IVertex;
import code.MD5.interfaces.mesh.primitive.IWeight;

/**
 * <code>Vertex</code> defines the concrete implementation of a vertex.
 * <p>
 * <code>Vertex</code> cannot be cloned directly. The cloning process
 * of <code>Vertex</code> can only be initiated by the cloning process
 * of the parent <code>IMesh</code>.
 * <p>
 * This class is used internally by <code>MD5Importer</code> only.
 * 
 * @author Yi Wang (Neakor)
 * @version Modified date: 04-03-2009 17:39 EST
 */
public class Vertex implements Serializable, IVertex {
	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = 6774812007144718188L;
	/**
	 * The <code>Integer</code> index of this vertex.
	 */
	private int index;
	/**
	 * The <code>Vector2f</code> texture coordinates.
	 */
	private Vector2f textureCoords;
	/**
	 * The array of <code>IWeight</code> instances.
	 */
	private IWeight[] weights;
	/**
	 * The number of times this vertex has been used.
	 */
	private int usedTimes;
	/**
	 * The <code>Vector3f</code> normal.
	 */
	private Vector3f normal;
	/**
	 * The referenced <code>Vector3f</code> normal.
	 */
	private Vector3f normalRef;
	/**
	 * The <code>Vector3f</code> position.
	 */
	private Vector3f position;
	/**
	 * The temporary <code>Vector3f</code> for position calculation.
	 */
	private final Vector3f temp;

	/**
	 * Constructor of <code>Vertex</code>.
	 */
	public Vertex() {
		this.temp = new Vector3f();
	}

	/**
	 * Constructor of <code>Vertex</code>.
	 * @param index The <code>Integer</code> index value.
	 */
	public Vertex(int index) {
		this.index = index;
		this.position = new Vector3f();
		this.temp = new Vector3f();
	}
	
	/**
	 * Constructor of <code>Vertex</code>.
	 * @param index The <code>Integer</code> index of this vertex.
	 * @param textureCoords The <code>Vector2f</code> texture coordinates.
	 * @param weights The array of <code>IWeight</code> instances.
	 * @param usedTimes The number of times this vertex has been used.
	 * @param normal The <code>Vector3f</code> normal.
	 * @param position The <code>Vector3f</code> position.
	 */
	private Vertex(int index, Vector2f textureCoords, IWeight[] weights, int usedTimes, Vector3f normal, Vector3f position) {
		this.index = index;
		this.textureCoords = textureCoords;
		this.weights = weights;
		this.usedTimes = usedTimes;
		this.normal = normal;
		this.position = position;
		this.temp = new Vector3f();
	}

	@Override
	public void processPosition() {
		this.position.zero();
		for(IWeight weight : this.weights) {
			this.temp.set(weight.getPosition());
			weight.getJoint().getTransform().multPoint(this.temp);
			this.temp.multLocal(weight.getWeightValue());
			this.position.addLocal(this.temp);
		}
	}

	@Override
	public void resetInformation() {
		if(this.normal != null) this.normal.zero();
		if(this.normalRef != null) this.normalRef.zero();
		this.position.zero();
	}

	@Override
	public void incrementUsedTimes() {
		this.usedTimes++;
	}

	@Override
	public void setTextureCoords(float u, float v) {
		// Invert the v value.
		float invertV = 1.0f - v;
		this.textureCoords = new Vector2f(u, invertV);
	}

	@Override
	public void setWeights(IWeight... weights) {
		this.weights = new IWeight[weights.length];
		for(int i = 0; i < this.weights.length; i++) {
			this.weights[i] = weights[i];
		}
	}

	@Override
	public void setNormal(Vector3f normal) {
		// Use normal reference if possible.
		if(this.normalRef != null) {
			this.normalRef.addLocal(normal);
			return;
		}
		if(this.normal == null) this.normal = new Vector3f(normal);
		// If this vertex has been used, add the new value.
		else this.normal.addLocal(normal);
	}
	
	@Override
	public void setNormalReference(Vector3f normal) {
		this.normalRef = normal;
	}

	@Override
	public Vector2f getTextureCoords() {
		return this.textureCoords;
	}

	@Override
	public int getUsedTimes() {
		return this.usedTimes;
	}

	@Override
	public Vector3f getPosition() {
		return this.position;
	}

	@Override
	public Vector3f getNormal() {
		return (this.normalRef != null) ? this.normalRef : this.normal;
	}

	@Override
	public int getIndex() {
		return this.index;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class getClassTag() {
		return Vertex.class;
	}

	@Override
	public void write(JMEExporter ex) throws IOException {
		OutputCapsule oc = ex.getCapsule(this);
		oc.write(this.index, "Index", -1);
		oc.write(this.textureCoords, "TextureCoords", null);
		oc.write(this.weights, "Weights", null);
		oc.write(this.usedTimes, "UsedTimes", 0);
		oc.write(this.normal, "Normal", null);
		oc.write(this.normalRef, "NormalRef", null);
		oc.write(this.position, "Position", null);
	}

	@Override
	public void read(JMEImporter im) throws IOException {
		InputCapsule ic = im.getCapsule(this);
		this.index = ic.readInt("Index", -1);
		this.textureCoords = (Vector2f)ic.readSavable("TextureCoords", null);
		Savable[] temp = ic.readSavableArray("Weights", null);
		this.weights = new IWeight[temp.length];
		for(int i = 0; i < this.weights.length; i++) this.weights[i] = (IWeight)temp[i];
		this.usedTimes = ic.readInt("UsedTimes", 0);
		this.normal = (Vector3f)ic.readSavable("Normal", null);
		this.normalRef = (Vector3f)ic.readSavable("NormalRef", null);
		this.position = (Vector3f)ic.readSavable("Position", null);
	}

	@Override
	public IVertex clone(IWeight[] clonedWeights) {
		IWeight[] weights = new IWeight[this.weights.length];
		for(int i = 0; i < weights.length; i++) weights[i] = clonedWeights[this.weights[i].getIndex()];
		return new Vertex(this.index, this.textureCoords.clone(), weights, this.usedTimes, this.normal.clone(), this.position.clone());
	}
}
