package code.MD5.resource.mesh.primitive;

import java.io.IOException;
import java.io.Serializable;

import com.jme.math.Vector3f;
import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.jme.util.export.Savable;
import code.MD5.interfaces.mesh.primitive.ITriangle;
import code.MD5.interfaces.mesh.primitive.IVertex;

/**
 * <code>Triangle</code> defines the concrete implementation of a
 * triangle. It is responsible for calculating the normal vector
 * for each <code>IVertex</code>.
 * <p>
 * <code>Triangle</code> cannot be cloned directly. The cloning
 * process of a <code>Triangle</code> can only be initiated by the
 * cloning process of the parent <code>IMesh</code>.
 * <p>
 * This class is used internally by <code>MD5Importer</code> only.
 * 
 * @author Yi Wang (Neakor)
 * @version Modified date: 11-18-2008 22:57 EST
 */
public class Triangle implements Serializable, ITriangle {
	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = -6234457193386375719L;
	/**
	 * The <code>Integer</code> index value.
	 */
	private int index;
	/**
	 * The array of <code>IVertex</code> instances.
	 */
	private IVertex[] vertices;
	/**
	 * The first temporary <code>Vector3f</code> for normal calculation.
	 */
	private final Vector3f temp1;
	/**
	 * The second temporary <code>Vector3f</code> for normal calculation.
	 */
	private final Vector3f temp2;
	
	/**
	 * Constructor of <code>Triangle</code>.
	 */
	public Triangle() {
		this(-1, null);
	}

	/**
	 * Constructor of <code>Triangle</code>.
	 * @param index The <code>Integer</code> index value.
	 * @param vertices The array of <code>IVertex</code> instances.
	 */
	public Triangle(int index, IVertex[] vertices) {
		this.index = index;
		this.vertices = vertices;
		this.temp1 = new Vector3f();
		this.temp2 = new Vector3f();
	}

	@Override
	public void processNormal() {
		IVertex vertex1 = this.vertices[0];
		IVertex vertex2 = this.vertices[1];
		IVertex vertex3 = this.vertices[2];
		this.temp1.set(vertex2.getPosition()).subtractLocal(vertex1.getPosition());
		this.temp2.set(vertex3.getPosition()).subtractLocal(vertex2.getPosition());
		this.temp1.crossLocal(this.temp2);
		this.temp1.normalizeLocal();
		vertex1.setNormal(this.temp2.set(this.temp1).multLocal(1.0f/(float)vertex1.getUsedTimes()));
		vertex2.setNormal(this.temp2.set(this.temp1).multLocal(1.0f/(float)vertex2.getUsedTimes()));
		vertex3.setNormal(this.temp1.multLocal(1.0f/(float)vertex3.getUsedTimes()));
	}

	@Override
	public IVertex getVertex(int index) {
		return this.vertices[index];
	}

	@Override
	public int getIndex() {
		return this.index;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class getClassTag() {
		return Triangle.class;
	}

	@Override
	public void write(JMEExporter ex) throws IOException {
		OutputCapsule oc = ex.getCapsule(this);
		oc.write(this.index, "Index", -1);
		oc.write(this.vertices, "Vertices", null);
	}

	@Override
	public void read(JMEImporter im) throws IOException {
		InputCapsule ic = im.getCapsule(this);
		this.index = ic.readInt("Index", -1);
		Savable[] temp = ic.readSavableArray("Vertices", null);
		this.vertices = new IVertex[temp.length];
		for(int i = 0; i < this.vertices.length; i++) this.vertices[i] = (IVertex)temp[i];
	}

	@Override
	public ITriangle clone(IVertex[] clonedVertices) {
		IVertex[] vertices = new IVertex[this.vertices.length];
		for(int i = 0; i < vertices.length; i++) vertices[i] = clonedVertices[this.vertices[i].getIndex()];
		return new Triangle(this.index, vertices);
	}
}
