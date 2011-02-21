package code.MD5.interfaces;

import com.jme.util.export.Savable;
import code.MD5.interfaces.mesh.IJoint;
import code.MD5.interfaces.mesh.IMesh;

/**
 * <code>IMD5Node</code> defines the interface of a completed loaded
 * MD5 model node. It can be maintained by other MD5 nodes as their
 * dependent child, which will force the dependent child to follow
 * the joints of its parent.
 *
 * @author Yi Wang (Neakor)
 * @version Creation date: 11-17-2008 22:27 EST
 * @version Modified date: 02-21-2009 16:54 EST
 */
public interface IMD5Node extends Savable {

	/**
	 * Initialize the <code>IMD5Node</code>.
	 */
	public void initialize();
	
	/**
	 * Add a controller to control this node.
	 * @param controller The <code>IMD5Controller</code> instance.
	 */
	public void addController(IMD5Controller controller);
	
	/**
	 * Attach the given MD5 node to the joint with given ID.
	 * @param node The <code>IMD5Node</code> needs to be attached.
	 * @param jointID The <code>String</code> ID of the joint.
	 */
	public void attachChild(IMD5Node node, String jointID);
	
	/**
	 * Attach the given MD5 node to the joint with given index.
	 * @param node The <code>IMD5Node</code> needs to be attached.
	 * @param jointIndex The <code>Integer</code> index of the joint.
	 */
	public void attachChild(IMD5Node node, int jointIndex);
	
	/**
	 * Attach the given MD5 node as a dependent child which shares the
	 * skeleton with this node.
	 * @param node The dependent <code>IMD5Node</code> needs to be attached.
	 */
	public void attachDependent(IMD5Node node);
	
	/**
	 * Remove the given controller from this node.
	 * @param controller The <code>IMD5Controller</code> instance.
	 */
	public void removeController(IMD5Controller controller);
	
	/**
	 * Remove the given child from this node.
	 * @param node The <code>IMD5Node</code> instance.
	 */
	public void removeChild(IMD5Node node);
	
	/**
	 * Remove the given dependent child.
	 * @param node The <code>IMD5Node</code> instance.
	 */
	public void removeDependent(IMD5Node node);
	
	/**
	 * Notify the <code>IMD5Node</code> that its skeleton has been modified.
	 */
	public void flagUpdate();
	
	/**
	 * Retrieve the name of the node.
	 * @return The <code>String</code> name.
	 */
	public String getName();
	
	/**
	 * Retrieve the skeleton of this MD5 node.
	 * @return The array of <code>IJoint</code>.
	 */
	public IJoint[] getJoints();
	
	/**
	 * Retrieve the joint with given index.
	 * @param index The <code>Integer</code> index number.
	 * @return The <code>IJoint</code> instance.
	 */
	public IJoint getJoint(int index);
	
	/**
	 * Retrieve the root joint.
	 * @return The root <code>IJoint</code> instance.
	 */
	public IJoint getRootJoint();
	
	/**
	 * Retrieve the mesh with given index.
	 * @param index The <code>Integer</code> index number.
	 * @return The <code>IMesh</code> instance.
	 */
	public IMesh getMesh(int index);
	
	/**
	 * Retrieve all the meshes maintained by this node.
	 * @return The array of <code>IMesh</code> instances.
	 */
	public IMesh[] getMeshes();
	
	/**
	 * Check if this MD5 node is a dependent child of some other node.
	 * @return True if this MD5 node is dependent. False otherwise.
	 */
	public boolean isDependent();
	
	/**
	 * Clone this MD5 node.
	 * @return The cloned <code>IMD5Node</code> instance.
	 */
	public IMD5Node clone();
}
