package code.model.player.MD5.interfaces.mesh.primitive;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.util.export.Savable;

/**
 * <code>IVertex</code> defines the interface of a single vertex in
 * a mesh system. It maintains its own transformation based on the
 * joints and weight that affects it.
 * <p>
 * <code>IVertex</code> maintains its texture coordinates, normal
 * vector and position vector.
 *
 * @author Yi Wang (Neakor)
 * @version Creation date: 11-17-2008 20:50 EST
 * @version Modified date: 04-03-2009 17:38 EST
 */
public interface IVertex extends Savable {

	/**
	 * Process the vertex position.
	 */
	public void processPosition();

	/**
	 * Reset the normal and position information of this vertex.
	 */
	public void resetInformation();

	/**
	 * Increment the number of times this vertex has been used by
	 * a triangle
	 */
	public void incrementUsedTimes();

	/**
	 * Set the texture coordinates of this vertex.
	 * @param u The u value.
	 * @param v The un-inverted v value.
	 */
	public void setTextureCoords(float u, float v);

	/**
	 * Set weights that affects this vertex
	 * @param weights The variable argument of <code>IWeight</code>.
	 */
	public void setWeights(IWeight... weights);

	/**
	 * Set the normal vector of this vertex.
	 * @param normal The normal <code>Vector3f</code> to be set.
	 */
	public void setNormal(Vector3f normal);
	
	/**
	 * Set this vertex to directly use the given reference as normal.
	 * @param normal The <code>Vector3f</code> normal reference.
	 */
	public void setNormalReference(Vector3f normal);

	/**
	 * Retrieve the texture coordinates of this vertex.
	 * @return The <code>Vector2f</code> texture coordinates.
	 */
	public Vector2f getTextureCoords();

	/**
	 * Retrieve the number of times this vertex has been used.
	 * @return The <code>Integer</code> number of used times.
	 */
	public int getUsedTimes();

	/**
	 * Retrieve the position of this vertex.
	 * @return The <code>Vector3f</code> position.
	 */
	public Vector3f getPosition();

	/**
	 * Retrieve the normal of this vertex.
	 * @return The normal <code>Vector3f</code>.
	 */
	public Vector3f getNormal();
	
	/**
	 * Retrieve the index value of this vertex.
	 * @return The <code>Integer</code> index.
	 */
	public int getIndex();

	/**
	 * Clone this vertex.
	 * @return The cloned copy of this <code>Vertex</code>
	 */
	public IVertex clone(IWeight[] clonedWeights);
}