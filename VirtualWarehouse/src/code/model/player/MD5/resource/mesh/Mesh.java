package code.model.player.MD5.resource.mesh;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import com.jme.bounding.BoundingBox;
import com.jme.bounding.OrientedBoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.TexCoords;
import com.jme.scene.TriMesh;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.RenderState.StateType;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.jme.util.export.Savable;
import com.jme.util.geom.BufferUtils;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import code.model.player.MD5.interfaces.mesh.IJoint;
import code.model.player.MD5.interfaces.mesh.IMesh;
import code.model.player.MD5.interfaces.mesh.primitive.ITriangle;
import code.model.player.MD5.interfaces.mesh.primitive.IVertex;
import code.model.player.MD5.interfaces.mesh.primitive.IWeight;

/**
 * <code>Mesh</code> defines the concrete implementation of a mesh.
 * It does not directly process any geometric information but
 * delegates the process down to the primitive elements it maintains.
 * <p>
 * <code>Mesh</code> cannot be cloned directly. The cloning process
 * of a <code>Mesh</code> can only be initiated by the cloning process
 * of the parent <code>IIMD5Node</code>.
 * <p>
 * This class is used internally by <code>MD5Importer</code> only.
 * 
 * @author Yi Wang (Neakor)
 * @version Modified date: 04-03-2009 17:40 EST
 */
public class Mesh extends TriMesh implements IMesh {
	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = -6431941710991131243L;
	/**
	 * The <code>String</code> color map file name.
	 */
	private String color;
	/**
	 * The <code>String</code> normal map file name.
	 */
	private String normal;
	/**
	 * The <code>String</code> specular map file name.
	 */
	private String specular;
	/**
	 * The array of <code>IVertex</code> in this mesh.
	 */
	private IVertex[] vertices;
	/**
	 * The array of <code>ITriangle</code> in this mesh.
	 */
	private ITriangle[] triangles;
	/**
	 * The array of <code>IWeight</code> in this mesh.
	 */
	private IWeight[] weights;
	/**
	 * The <code>Integer</code> anisotropic level value.
	 */
	private int anisotropic;
	/**
	 * The <code>MinificationFilter</code> enumeration.
	 */
	private MinificationFilter miniFilter;
	/**
	 * The <code>MagnificationFilter</code> enumeration.
	 */
	private MagnificationFilter magFilter;
	/**
	 * The flag indicates if oriented bounding should be used.
	 */
	private boolean orientedBounding;
	/**
	 * The temporary <code>List</code> of <code>IVertex</code>
	 * for averaging normal calculation.
	 */
	private final List<IVertex> tempVertices;

	/**
	 * Constructor of <code>Mesh</code>.
	 */
	public Mesh() {
		super();
		this.tempVertices = new ArrayList<IVertex>();
	}
	
	/**
	 * Constructor of <code>Mesh</code>.
	 * @param texture The <code>String</code> texture file name without extension.
	 * @param vertices The array of <code>IVertex</code> in this mesh.
	 * @param triangles The array of <code>ITriangle</code> in this mesh.
	 * @param weights The array of <code>IWeight</code> in this mesh.
	 */
	public Mesh(String texture, IVertex[] vertices, ITriangle[] triangles, IWeight[] weights, int anisotropic, MinificationFilter miniFilter,
			MagnificationFilter magFilter, boolean orientedBounding) {
		this.color = texture;
		this.vertices = vertices;
		this.triangles = triangles;
		this.weights = weights;
		this.anisotropic = anisotropic;
		this.miniFilter = miniFilter;
		this.magFilter = magFilter;
		this.orientedBounding = orientedBounding;
		this.tempVertices = new ArrayList<IVertex>();
	}

	@Override
	public void initialize(String name) {
		this.setName(name + "Mesh");
		this.setNormalsMode(Spatial.NormalsMode.AlwaysNormalize);
		this.processIndex();
		this.processVertex();
		this.processNormal(true);
		this.processTexture();
		this.processBounding();
	}

	@Override
	public void updateMesh() {
		this.processVertex();
		this.processNormal(false);
		this.updateModelBound();
	}

	/**
	 * Process and setup the index buffer.
	 */
	private void processIndex() {
		IntBuffer indexBuffer = BufferUtils.createIntBuffer(this.triangles.length*3);
		indexBuffer.clear();
		for(ITriangle triangle : this.triangles) {
			for(int j = 0; j < 3; j++) {
				indexBuffer.put(triangle.getVertex(j).getIndex());
			}
		}
		indexBuffer.flip();
		this.setIndexBuffer(indexBuffer);
	}

	/**
	 * Process and setup the vertex position buffer.
	 */
	private void processVertex() {
		FloatBuffer vertexBuffer = this.getVertexBuffer();
		if(vertexBuffer == null) {
			vertexBuffer = BufferUtils.createVector3Buffer(this.vertices.length);
			this.setVertexBuffer(vertexBuffer);
		}
		vertexBuffer.clear();
		for(int i = 0; i < this.vertices.length; i++) {
			this.vertices[i].resetInformation();
			this.vertices[i].processPosition();
			BufferUtils.setInBuffer(this.vertices[i].getPosition(), vertexBuffer, i);
		}
	}

	/**
	 * Process and setup the normal position buffer.
	 * @param init The <code>Boolean</code> initialization flag.
	 */
	private void processNormal(boolean init) {
		// Triangles have to process the normal first in case the vertices are not in order.
		for(int i = 0; i < this.triangles.length; i++) {
			this.triangles[i].processNormal();
		}
		// Average vertex normals with same vertex positions.
		if(init) this.averageNormal();
		// Put into buffer.
		FloatBuffer normalBuffer = this.getNormalBuffer();
		if(normalBuffer == null) {
			normalBuffer = BufferUtils.createVector3Buffer(this.vertices.length);
			this.setNormalBuffer(normalBuffer);
		}
		normalBuffer.clear();
		for(int i = 0; i < this.vertices.length; i++) {
			BufferUtils.setInBuffer(this.vertices[i].getNormal(), normalBuffer, i);
		}
	}
	
	/**
	 * Average normals for vertices with same position.
	 */
	private void averageNormal() {
		this.tempVertices.clear();
		for(int i = 0; i < this.vertices.length; i++) {
			final IVertex v1 = this.vertices[i];
			this.tempVertices.add(v1);
			// Find all vertices with same position.
			for(int j = 0; j < this.vertices.length; j++) {
				final IVertex v2 = this.vertices[j];
				if(v1 != v2 && v2.getPosition().equals(v1.getPosition())) {
					this.tempVertices.add(v2);
				}
			}
			// Average vertices in list.
			float x = 0;
			float y = 0;
			float z = 0;
			for(IVertex vertex : this.tempVertices) {
				x += vertex.getNormal().getX();
				y += vertex.getNormal().getY();
				z += vertex.getNormal().getZ();
			}
			final int size = this.tempVertices.size();
			x = x / size;
			y = y / size;
			z = z / size;
			final Vector3f sharedNormal = new Vector3f(x, y, z);
			sharedNormal.normalizeLocal();
			for(IVertex vertex : this.tempVertices) {
				vertex.setNormalReference(sharedNormal);
			}
			// Clear out this group.
			this.tempVertices.clear();
		}
	}

	/**
	 * Process and setup the <code>TextureState</code> and texture UV buffer.
	 */
	private void processTexture() {
		FloatBuffer textureBuffer = BufferUtils.createVector2Buffer(this.vertices.length);
		float maxU = 1; float maxV = 1; float minU = 0; float minV = 0;
		int index = 0;
		for(IVertex vertex : this.vertices) {
			BufferUtils.setInBuffer(vertex.getTextureCoords(), textureBuffer, index);
			if(vertex.getTextureCoords().x > maxU) maxU = vertex.getTextureCoords().x;
			else if(vertex.getTextureCoords().x < minU) minU = vertex.getTextureCoords().x;
			if(vertex.getTextureCoords().y > maxV) maxV = vertex.getTextureCoords().y;
			else if(vertex.getTextureCoords().y < minV) minV = vertex.getTextureCoords().y;
			index++;
		}
		this.setTextureCoords(new TexCoords(textureBuffer));
		
		// Get texture state.
		TextureState state = (TextureState)this.getRenderState(StateType.Texture);
		if(state == null) {
			state= DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
			this.setRenderState(state);
		}
		// Set color map.
		if(this.color != null) state.setTexture(this.loadTexture(this.color, maxU, maxV), 0);
		// Set normal map.
		if(this.normal != null) state.setTexture(this.loadTexture(this.normal, maxU, maxV), 1);
		// Set specular map.
		if(this.specular != null) state.setTexture(this.loadTexture(this.specular, maxU, maxV), 2);
	}
	
	/**
	 * Load the texture linked by given file and set its wrap modes based on given values.
	 * @param file The <code>String</code> file location.
	 * @param maxU The <code>Float</code> maximum u value.
	 * @param maxV The <code>Float</code> maximum v value.
	 * @return The loaded <code>Texture</code> instance.
	 */
	private Texture loadTexture(String file, float maxU, float maxV) {
		// Add a locator according to the texture string.
		int last = file.lastIndexOf("/") + 1;
		if(last < 0) last = file.length();
		File path = new File(file.substring(0, last));
		
		System.out.println(file);
		
		try {
			if(path != null) {
				SimpleResourceLocator locator = new SimpleResourceLocator(path.toURI().toURL());
				ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, locator);
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		// Load URL.
		URL url = ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, file);
		// Load the texture and set the wrap mode.
		Texture map = TextureManager.loadTexture(url, this.miniFilter, this.magFilter, this.anisotropic, true);
		if(map != null) {
			if(maxU > 1) map.setWrap(Texture.WrapAxis.S, Texture.WrapMode.Repeat);
			else map.setWrap(Texture.WrapAxis.S, Texture.WrapMode.Clamp);
			if(maxV > 1) map.setWrap(Texture.WrapAxis.T, Texture.WrapMode.Repeat);
			else map.setWrap(Texture.WrapAxis.T, Texture.WrapMode.Clamp);
		}
		return map;
	}

	/**
	 * Process and setup the bounding volume of the <code>Mesh</code>.
	 */
	private void processBounding() {
		if(this.orientedBounding) this.setModelBound(new OrientedBoundingBox());
		else this.setModelBound(new BoundingBox());
		this.updateModelBound();
		this.updateGeometricState(0, true);
	}

	@Override
	public void setJoints(IJoint[] joints) {
		for(IWeight weight : this.weights) {
			weight.setJoint(joints[weight.getJoint().getIndex()]);
		}
	}

	@Override
	public IVertex getVertex(int index) {
		return this.vertices[index];
	}

	@Override
	public IWeight getWeight(int index) {
		return this.weights[index];
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class getClassTag() {
		return Mesh.class;
	}

	@Override
	public void write(JMEExporter ex) throws IOException {
		super.write(ex);
		OutputCapsule oc = ex.getCapsule(this);
		// Save all texture locations.
		TextureState state = (TextureState)this.getRenderState(StateType.Texture);
		Texture colorMap = state.getTexture(0);
		Texture normalMap = state.getTexture(1);
		Texture specularMap = state.getTexture(2);
		if(colorMap != null) {
			String colorRaw = colorMap.getImageLocation();
			oc.write(colorRaw.substring(colorRaw.indexOf("/"), colorRaw.length()), "ColorMap", null);
		}
		if(normalMap != null) {
			String normalRaw = normalMap.getImageLocation();
			oc.write(normalRaw.substring(normalRaw.indexOf("/"), normalRaw.length()), "NormalMap", null);
		}
		if(specularMap != null) {
			String specularRaw = specularMap.getImageLocation();
			oc.write(specularRaw.substring(specularRaw.indexOf("/"), specularRaw.length()), "SpecularMap", null);
		}
		// Write out primitives.
		oc.write(this.vertices, "Vertices", null);
		oc.write(this.triangles, "Triangles", null);
		oc.write(this.weights, "Weights", null);
		// Write out settings.
		oc.write(this.anisotropic, "Anisotropic", 0);
		oc.write(this.miniFilter.name(), "MinFilter", null);
		oc.write(this.magFilter.name(), "MagFilter", null);
		oc.write(this.orientedBounding, "OrientedBounding", false);
	}

	@Override
	public void read(JMEImporter im) throws IOException {
		super.read(im);
		Savable[] temp = null;
		InputCapsule ic = im.getCapsule(this);
		// Read in texture map locations.
		this.color = ic.readString("ColorMap", null);
		this.normal = ic.readString("NormalMap", null);
		this.specular = ic.readString("SpecularMap", null);
		// Read in primitives.
		temp = ic.readSavableArray("Vertices", null);
		this.vertices = new IVertex[temp.length];
		for(int i = 0; i < temp.length; i++) {
			this.vertices[i] = (IVertex)temp[i];
		}
		temp = ic.readSavableArray("Triangles", null);
		this.triangles = new ITriangle[temp.length];
		for(int i = 0; i < temp.length; i++) {
			this.triangles[i] = (ITriangle)temp[i];
		}
		temp = ic.readSavableArray("Weights", null);
		this.weights = new IWeight[temp.length];
		for(int i = 0; i < temp.length; i++) {
			this.weights[i] = (IWeight)temp[i];
		}
		// Read in settings.
		this.anisotropic = ic.readInt("Anisotropic", 0);
		this.miniFilter = MinificationFilter.valueOf(ic.readString("MinFilter", null));
		this.magFilter = MagnificationFilter.valueOf(ic.readString("MagFilter", null));
		this.orientedBounding = ic.readBoolean("OrientedBounding", false);
	}

	@Override
	public IMesh clone(IJoint[] clonedJoints) {
		// Weights need to be cloned first.
		IWeight[] clonedWeights = new IWeight[this.weights.length];
		for(int i = 0; i < clonedWeights.length; i++) clonedWeights[i] = this.weights[i].clone(clonedJoints);
		// Then pass cloned weights to clone vertices.
		IVertex[] clonedVertices = new IVertex[this.vertices.length];
		for(int i = 0; i < clonedVertices.length; i++) clonedVertices[i] = this.vertices[i].clone(clonedWeights);
		// Then pass cloned vertices to clone triangles.
		ITriangle[] clonedTriangles = new ITriangle[this.triangles.length];
		for(int i = 0; i < clonedTriangles.length; i++) clonedTriangles[i] = this.triangles[i].clone(clonedVertices);
		return new Mesh(new String(this.color), clonedVertices, clonedTriangles, clonedWeights, this.anisotropic, this.miniFilter, this.magFilter, this.orientedBounding);
	}
}
