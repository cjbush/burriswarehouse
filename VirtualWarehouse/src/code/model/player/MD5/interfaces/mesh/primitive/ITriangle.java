package code.model.player.MD5.interfaces.mesh.primitive;

import com.jme.util.export.Savable;

/**
 * <code>ITriangle</code> defines a triangle of a mesh. It is composed
 * of three vertices.
 *
 * @author Yi Wang (Neakor)
 * @version Creation date: 11-17-2008 20:32 EST
 * @version Modified date: 11-18-2008 22:41 EST
 */
public interface ITriangle extends Savable {

	/**
	 * Process the normal of vertices and store the normal values in the
	 * <code>IVertex</code> instances.
	 */
	public void processNormal();

	/**
	 * Retrieve the vertex with given array index.
	 * @param index The <code>Integer</code> index number in the <code>ITriangle</code>.
	 * @return The <code>IVertex</code> instance with given array index.
	 */
	public IVertex getVertex(int index);
	
	/**
	 * Retrieve the index value of this triangle.
	 * @return The <code>Integer</code> index value.
	 */
	public int getIndex();

	/**
	 * Clone this triangle.
	 * @param clonedVertices The array of cloned <code>IVertex</code>.
	 * @return The cloned copy of this <code>ITriangle</code>
	 */
	public ITriangle clone(IVertex[] clonedVertices);
}