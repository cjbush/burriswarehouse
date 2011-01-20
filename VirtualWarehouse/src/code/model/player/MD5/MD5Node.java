package code.model.player.MD5;

import java.io.IOException;
import java.util.ArrayList;

import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.jme.util.export.Savable;
import code.model.player.MD5.controller.MD5Controller;
import code.model.player.MD5.interfaces.IMD5Controller;
import code.model.player.MD5.interfaces.IMD5Node;
import code.model.player.MD5.interfaces.mesh.IJoint;
import code.model.player.MD5.interfaces.mesh.IMesh;

/**
 * <code>MD5Node</code> is the final product of MD5 loading process.
 * <p>
 * <code>ModelNode</code> maintains the loaded <code>IJoint</code>
 * and <code>IMesh</code> instances and update them accordingly.
 * <p>
 * <code>MD5Node</code> provides the cloning functionality so that
 * users can fast clone model nodes that may be used by multiple
 * entities. The newly cloned <code>MD5Node</code> is already
 * initialized and ready to be used.
 *
 * @author Yi Wang (Neakor)
 * @version Modified date: 02-21-2009 16:54 EST
 */
public class MD5Node extends Node implements IMD5Node {
	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = -2799207065296472869L;
	/**
	 * The flag indicates if the skeleton has been modified.
	 */
	private boolean update;
	/**
	 * The flag indicates if model node shares skeleton with its parent.
	 */
	private boolean dependent;
	/**
	 * The array of <code>IJoint</code> skeleton.
	 */
	private IJoint[] joints;
	/**
	 * The array of <code>IMesh</code> instances.
	 */
	private IMesh[] meshes;
	/**
	 * The <code>ArrayList</code> of dependent <code>IMD5Node</code>.
	 */
	private ArrayList<IMD5Node> dependents;

	/**
	 * Constructor of <code>MD5Node</code>.
	 */
	public MD5Node() {
		super();
	}

	/**
	 * Constructor of <code>MD5Node</code>.
	 * @param name The <code>String</code> name.
	 */
	public MD5Node(String name, IJoint[] joints, IMesh[] meshes) {
		super(name);
		this.joints = joints;
		this.meshes = meshes;
		this.dependents = new ArrayList<IMD5Node>();
	}

	@Override
	public void initialize() {
		if(this.meshes != null) {
			for(int i = 0; i < this.meshes.length; i++) {
				this.detachChild((Spatial)this.meshes[i]);
			}
		}
		if(!this.dependent) this.processJoints();
		for(int i = 0; i < this.meshes.length; i++) {
			this.meshes[i].initialize(this.name);
			this.attachChild((Spatial)this.meshes[i]);
		}
	}

	/**
	 * Process the skeleton relative transformations.
	 */
	private void processJoints() {
		for(IJoint joint : this.joints) {
			joint.processRelative();
		}
	}

	@Override
	public void updateGeometricState(float time, boolean initiator) {
		if(this.update) {
			if(!this.dependent) this.processJoints();
			for(int i = 0; i < this.meshes.length; i++) {
				this.meshes[i].updateMesh();
			}
			this.update = false;
		}
		super.updateGeometricState(time, initiator);
	}

	@Override
	public void addController(IMD5Controller controller) {
		super.addController((MD5Controller)controller);
	}

	@Override
	public void attachChild(IMD5Node node, String jointID) {
		int jointIndex = -1;
		for(int i = 0; i < this.joints.length; i++) {
			if(this.joints[i].getName().equals(jointID)) {
				jointIndex = i;
				break;
			}
		}
		this.attachChild(node, jointIndex);
	}

	@Override
	public void attachChild(IMD5Node node, int jointIndex) {
		node.getRootJoint().setSuperParent(this.getJoint(jointIndex));
		this.attachChild((Spatial)node);
		node.initialize();
	}

	@Override
	public void attachDependent(IMD5Node node) {
		if(this.dependents.contains(node)) return;
		this.dependents.add(node);
		((MD5Node)node).setDependent(true, this);
		this.attachChild((Spatial)node);
		node.initialize();
	}

	@Override
	public void removeController(IMD5Controller controller) {
		super.removeController((MD5Controller)controller);
	}

	@Override
	public void removeChild(IMD5Node node) {
		node.getRootJoint().setSuperParent(null);
		this.detachChild((Spatial)node);
		node.initialize();
	}

	@Override
	public void removeDependent(IMD5Node node) {
		this.dependents.remove(node);
		this.setDependent(false, this);
		this.detachChild((Spatial)node);
		node.initialize();
	}

	@Override
	public void flagUpdate() {
		this.update = true;
		for(IMD5Node dependent : this.dependents) {
			dependent.flagUpdate();
		}
	}

	/**
	 * Set this MD5 node as a dependent child of another MD5 node. This
	 * makes this node share the skeleton structure of its parent node.
	 * @param dependent True if this mesh should be set as dependent.
	 * @param parent The <code>IMD5Node</code> parent.
	 */
	protected void setDependent(boolean dependent, IMD5Node parent) {
		this.dependent = dependent;
		if(this.dependent) {
			this.joints = parent.getJoints();
			for(IMesh mesh : this.meshes) {
				mesh.setJoints(this.joints);
			}
		} else {
			for(int i = 0; i < this.joints.length; i++) {
				IJoint clone = this.joints[i].clone();
				this.joints[i] = clone;
			}
			for(IMesh mesh : this.meshes) {
				mesh.setJoints(this.joints);
			}
		}
	}

	@Override
	public IJoint[] getJoints() {
		return this.joints;
	}

	@Override
	public IJoint getJoint(int index) {
		return this.joints[index];
	}

	@Override
	public IJoint getRootJoint() {
		if(this.joints[0].getParent() == null) return this.joints[0];
		else {
			for(int i = 1; i < this.joints.length; i++) {
				if(this.joints[i].getParent() == null) return this.joints[i];
			}
		}
		return null;
	}

	@Override
	public IMesh getMesh(int index) {
		return this.meshes[index];
	}

	@Override
	public IMesh[] getMeshes() {
		return this.meshes;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class getClassTag() {
		return MD5Node.class;
	}

	@Override
	public boolean isDependent() {
		return this.dependent;
	}

	@Override
	public void write(JMEExporter ex) throws IOException {
		for(int i = 0; i < this.meshes.length; i++) {
			this.detachChild((Spatial)this.meshes[i]);
		}
		super.write(ex);
		OutputCapsule oc = ex.getCapsule(this);
		oc.write(this.dependent, "Dependent", false);
		oc.write(this.joints, "Joints", null);
		oc.write(this.meshes, "Meshes", null);
		oc.writeSavableArrayList(this.dependents, "Dependents", null);
		for(int i = 0; i < this.meshes.length; i++) {
			this.attachChild((Spatial)this.meshes[i]);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void read(JMEImporter im) throws IOException {
		super.read(im);
		InputCapsule ic = im.getCapsule(this);
		this.dependent = ic.readBoolean("Dependent", false);
		Savable[] temp = null;
		temp = ic.readSavableArray("Joints", null);
		this.joints = new IJoint[temp.length];
		for(int i = 0; i < temp.length; i++) {
			this.joints[i] = (IJoint)temp[i];
		}
		temp = ic.readSavableArray("Meshes", null);
		this.meshes = new IMesh[temp.length];
		for(int i = 0; i < temp.length; i++) {
			this.meshes[i] = (IMesh)temp[i];
		}
		this.dependents = ic.readSavableArrayList("Dependents", null);
		this.initialize();
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
		if(object instanceof MD5Node) {
			MD5Node given = (MD5Node)object;
			return given.name.equals(this.name);
		}
		return false;
	}
	
	@Override
	public IMD5Node clone() {
		// Clone all the joints.
		IJoint[] clonedJoints = new IJoint[this.joints.length];
		for(int i = 0; i < clonedJoints.length; i++) clonedJoints[i] = this.joints[i].clone();
		// Set the parents and super parents of the cloned joints.
		for(IJoint joint : this.joints) {
			IJoint cloned = clonedJoints[joint.getIndex()];
			if(joint.getParent() != null) {
				IJoint parent = clonedJoints[joint.getParent().getIndex()];
				cloned.setParent(parent);
			}
			if(joint.getSuperParent() != null) {
				IJoint superParent = clonedJoints[joint.getSuperParent().getIndex()];
				cloned.setSuperParent(superParent);
			}
		}
		// The clone meshes based on cloned joints.
		IMesh[] clonedMeshes = new IMesh[this.meshes.length];
		for(int i = 0; i < clonedMeshes.length; i++) clonedMeshes[i] = this.meshes[i].clone(clonedJoints);
		MD5Node clone = new MD5Node(new String(this.name), clonedJoints, clonedMeshes);	
		// Attach the dependent children.
		clone.dependent = this.dependent;
		for(IMD5Node dependent : this.dependents) clone.attachDependent(dependent.clone());
		// Initialize the clone.
		clone.initialize();
		clone.setCullHint(this.getCullHint());
		clone.setIsCollidable(this.isCollidable());
		clone.setLightCombineMode(this.getLightCombineMode());
		clone.setLocalRotation(this.getLocalRotation().clone());
		clone.setLocalScale(this.getLocalScale().clone());
		clone.setLocalTranslation(this.getLocalTranslation().clone());
		clone.setNormalsMode(this.getLocalNormalsMode());
		clone.setRenderQueueMode(this.getRenderQueueMode());
		clone.setTextureCombineMode(this.getTextureCombineMode());
		clone.setZOrder(this.getZOrder());
		return clone;
	}
}
