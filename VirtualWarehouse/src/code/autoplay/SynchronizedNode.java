package code.autoplay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.jme.bounding.BoundingVolume;
import com.jme.intersection.CollisionResults;
import com.jme.intersection.PickResults;
import com.jme.math.Matrix3f;
import com.jme.math.Matrix4f;
import com.jme.math.Quaternion;
import com.jme.math.Ray;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.renderer.Camera.FrustumIntersect;
import com.jme.scene.Controller;
import com.jme.scene.Geometry;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.RenderState.StateType;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.Savable;

/**
 * A concurrency-safe version of the JME <code>Node</code> class. This is implemented
 * by making every method of the <code>Node</code> class <code>synchronized</code>.<br/><br/>
 * 
 * Used to prevent a <code>ConcurrentModificationException</code> that was thrown 
 * during the execution <code>ReplayController</code> thread.<br/><br/>
 * 
 * <b>Date:</b> November 2010
 * @author Jordan Hinshaw
 *
 */
public class SynchronizedNode extends Node {

	/**
	 * 
	 */
	public SynchronizedNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 */
	public SynchronizedNode(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	/* (non-Javadoc)
	 * @see com.jme.scene.Node#applyRenderState(java.util.Stack<? extends com.jme.scene.state.RenderState>[])
	 */
	@Override
	protected synchronized void applyRenderState(Stack<? extends RenderState>[] states) {

		super.applyRenderState(states);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#attachChild(com.jme.scene.Spatial)
	 */
	@Override
	public synchronized int attachChild(Spatial child) {

		return super.attachChild(child);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#attachChildAt(com.jme.scene.Spatial, int)
	 */
	@Override
	public synchronized int attachChildAt(Spatial child, int index) {

		return super.attachChildAt(child, index);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#childChange(com.jme.scene.Geometry, int, int)
	 */
	@Override
	public synchronized void childChange(Geometry geometry, int index1, int index2) {

		super.childChange(geometry, index1, index2);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#descendantMatches(java.lang.Class, java.lang.String)
	 */
	@Override
	public synchronized <T extends Spatial> List<T> descendantMatches(
			Class<T> spatialSubclass, String nameRegex) {

		return super.descendantMatches(spatialSubclass, nameRegex);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#descendantMatches(java.lang.Class)
	 */
	@Override
	public synchronized <T extends Spatial> List<T> descendantMatches(
			Class<T> spatialSubclass) {

		return super.descendantMatches(spatialSubclass);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#descendantMatches(java.lang.String)
	 */
	@Override
	public synchronized <T extends Spatial> List<T> descendantMatches(String nameRegex) {

		return super.descendantMatches(nameRegex);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#detachAllChildren()
	 */
	@Override
	public synchronized void detachAllChildren() {

		super.detachAllChildren();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#detachChild(com.jme.scene.Spatial)
	 */
	@Override
	public synchronized int detachChild(Spatial child) {

		return super.detachChild(child);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#detachChildAt(int)
	 */
	@Override
	public synchronized Spatial detachChildAt(int index) {

		return super.detachChildAt(index);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#detachChildNamed(java.lang.String)
	 */
	@Override
	public synchronized int detachChildNamed(String childName) {

		return super.detachChildNamed(childName);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#draw(com.jme.renderer.Renderer)
	 */
	@Override
	public synchronized void draw(Renderer r) {

		super.draw(r);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#findCollisions(com.jme.scene.Spatial, com.jme.intersection.CollisionResults, int)
	 */
	@Override
	public synchronized void findCollisions(Spatial scene, CollisionResults results,
			int requiredOnBits) {

		super.findCollisions(scene, results, requiredOnBits);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#findPick(com.jme.math.Ray, com.jme.intersection.PickResults, int)
	 */
	@Override
	public synchronized void findPick(Ray toTest, PickResults results, int requiredOnBits) {

		super.findPick(toTest, results, requiredOnBits);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#getChild(int)
	 */
	@Override
	public synchronized Spatial getChild(int i) {

		return super.getChild(i);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#getChild(java.lang.String)
	 */
	@Override
	public synchronized Spatial getChild(String name) {

		return super.getChild(name);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#getChildIndex(com.jme.scene.Spatial)
	 */
	@Override
	public synchronized int getChildIndex(Spatial sp) {

		return super.getChildIndex(sp);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#getChildren()
	 */
	@Override
	public synchronized List<Spatial> getChildren() {

		return super.getChildren();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#getQuantity()
	 */
	@Override
	public synchronized int getQuantity() {

		return super.getQuantity();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#getTriangleCount()
	 */
	@Override
	public synchronized int getTriangleCount() {

		return super.getTriangleCount();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#getVertexCount()
	 */
	@Override
	public synchronized int getVertexCount() {

		return super.getVertexCount();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#hasChild(com.jme.scene.Spatial)
	 */
	@Override
	public synchronized boolean hasChild(Spatial spat) {

		return super.hasChild(spat);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#hasCollision(com.jme.scene.Spatial, boolean, int)
	 */
	@Override
	public synchronized boolean hasCollision(Spatial scene, boolean checkTriangles,
			int requiredOnBits) {

		return super.hasCollision(scene, checkTriangles, requiredOnBits);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#lockBounds()
	 */
	@Override
	public synchronized void lockBounds() {

		super.lockBounds();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#lockMeshes(com.jme.renderer.Renderer)
	 */
	@Override
	public synchronized void lockMeshes(Renderer r) {

		super.lockMeshes(r);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#lockShadows()
	 */
	@Override
	public synchronized void lockShadows() {

		super.lockShadows();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#lockTransforms()
	 */
	@Override
	public synchronized void lockTransforms() {

		super.lockTransforms();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#read(com.jme.util.export.JMEImporter)
	 */
	@Override
	public synchronized void read(JMEImporter e) throws IOException {

		super.read(e);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#setCollisionMask(int, boolean)
	 */
	@Override
	public synchronized void setCollisionMask(int collisionBits, boolean includeChildren) {

		super.setCollisionMask(collisionBits, includeChildren);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#setCollisionMask(int)
	 */
	@Override
	public synchronized void setCollisionMask(int collisionBits) {

		super.setCollisionMask(collisionBits);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#setModelBound(com.jme.bounding.BoundingVolume)
	 */
	@Override
	public synchronized void setModelBound(BoundingVolume modelBound) {

		super.setModelBound(modelBound);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#sortLights()
	 */
	@Override
	public synchronized void sortLights() {

		super.sortLights();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#swapChildren(int, int)
	 */
	@Override
	public synchronized void swapChildren(int index1, int index2) {

		super.swapChildren(index1, index2);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#unlockBounds()
	 */
	@Override
	public synchronized void unlockBounds() {

		super.unlockBounds();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#unlockMeshes(com.jme.renderer.Renderer)
	 */
	@Override
	public synchronized void unlockMeshes(Renderer r) {

		super.unlockMeshes(r);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#unlockShadows()
	 */
	@Override
	public synchronized void unlockShadows() {

		super.unlockShadows();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#unlockTransforms()
	 */
	@Override
	public synchronized void unlockTransforms() {

		super.unlockTransforms();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#updateModelBound()
	 */
	@Override
	public synchronized void updateModelBound() {

		super.updateModelBound();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#updateWorldBound()
	 */
	@Override
	public synchronized void updateWorldBound() {

		super.updateWorldBound();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#updateWorldBound(boolean)
	 */
	@Override
	public synchronized void updateWorldBound(boolean recursive) {

		super.updateWorldBound(recursive);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#updateWorldData(float)
	 */
	@Override
	public synchronized void updateWorldData(float time) {

		super.updateWorldData(time);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#updateWorldVectors(boolean)
	 */
	@Override
	public synchronized void updateWorldVectors(boolean recurse) {

		super.updateWorldVectors(recurse);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#write(com.jme.util.export.JMEExporter)
	 */
	@Override
	public synchronized void write(JMEExporter e) throws IOException {

		super.write(e);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#addController(com.jme.scene.Controller)
	 */
	@Override
	public synchronized void addController(Controller controller) {

		super.addController(controller);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#clearControllers()
	 */
	@Override
	public synchronized void clearControllers() {

		super.clearControllers();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#clearRenderState(int)
	 */
	@Override
	public synchronized void clearRenderState(int renderStateType) {

		super.clearRenderState(renderStateType);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#clearRenderState(com.jme.scene.state.RenderState.StateType)
	 */
	@Override
	public synchronized void clearRenderState(StateType type) {

		super.clearRenderState(type);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getClassTag()
	 */
	@Override
	public synchronized Class<? extends Spatial> getClassTag() {

		return super.getClassTag();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getCollisionMask()
	 */
	@Override
	public synchronized int getCollisionMask() {

		return super.getCollisionMask();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getController(int)
	 */
	@Override
	public synchronized Controller getController(int i) {

		return super.getController(i);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getControllerCount()
	 */
	@Override
	public synchronized int getControllerCount() {

		return super.getControllerCount();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getControllers()
	 */
	@Override
	public synchronized ArrayList<Controller> getControllers() {

		return super.getControllers();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getCullHint()
	 */
	@Override
	public synchronized CullHint getCullHint() {

		return super.getCullHint();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getLastFrustumIntersection()
	 */
	@Override
	public synchronized FrustumIntersect getLastFrustumIntersection() {

		return super.getLastFrustumIntersection();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getLightCombineMode()
	 */
	@Override
	public synchronized LightCombineMode getLightCombineMode() {

		return super.getLightCombineMode();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getLocalCullHint()
	 */
	@Override
	public synchronized CullHint getLocalCullHint() {

		return super.getLocalCullHint();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getLocalLightCombineMode()
	 */
	@Override
	public synchronized LightCombineMode getLocalLightCombineMode() {

		return super.getLocalLightCombineMode();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getLocalNormalsMode()
	 */
	@Override
	public synchronized NormalsMode getLocalNormalsMode() {

		return super.getLocalNormalsMode();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getLocalRenderQueueMode()
	 */
	@Override
	public synchronized int getLocalRenderQueueMode() {

		return super.getLocalRenderQueueMode();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getLocalRotation()
	 */
	@Override
	public synchronized Quaternion getLocalRotation() {

		return super.getLocalRotation();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getLocalScale()
	 */
	@Override
	public synchronized Vector3f getLocalScale() {

		return super.getLocalScale();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getLocalTextureCombineMode()
	 */
	@Override
	public synchronized TextureCombineMode getLocalTextureCombineMode() {

		return super.getLocalTextureCombineMode();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getLocalToWorldMatrix(com.jme.math.Matrix4f)
	 */
	@Override
	public synchronized Matrix4f getLocalToWorldMatrix(Matrix4f store) {

		return super.getLocalToWorldMatrix(store);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getLocalTranslation()
	 */
	@Override
	public synchronized Vector3f getLocalTranslation() {

		return super.getLocalTranslation();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getLocks()
	 */
	@Override
	public synchronized int getLocks() {

		return super.getLocks();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getName()
	 */
	@Override
	public synchronized String getName() {

		return super.getName();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getNormalsMode()
	 */
	@Override
	public synchronized NormalsMode getNormalsMode() {

		return super.getNormalsMode();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getParent()
	 */
	@Override
	public synchronized Node getParent() {

		return super.getParent();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getRenderQueueMode()
	 */
	@Override
	public synchronized int getRenderQueueMode() {

		return super.getRenderQueueMode();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getRenderState(int)
	 */
	@Override
	public synchronized RenderState getRenderState(int type) {

		return super.getRenderState(type);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getRenderState(com.jme.scene.state.RenderState.StateType)
	 */
	@Override
	public synchronized RenderState getRenderState(StateType type) {

		return super.getRenderState(type);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getTextureCombineMode()
	 */
	@Override
	public synchronized TextureCombineMode getTextureCombineMode() {

		return super.getTextureCombineMode();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getUserData(java.lang.String)
	 */
	@Override
	public synchronized Savable getUserData(String key) {

		return super.getUserData(key);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getWorldBound()
	 */
	@Override
	public synchronized BoundingVolume getWorldBound() {

		return super.getWorldBound();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getWorldRotation()
	 */
	@Override
	public synchronized Quaternion getWorldRotation() {

		return super.getWorldRotation();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getWorldScale()
	 */
	@Override
	public synchronized Vector3f getWorldScale() {

		return super.getWorldScale();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getWorldTranslation()
	 */
	@Override
	public synchronized Vector3f getWorldTranslation() {

		return super.getWorldTranslation();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#getZOrder()
	 */
	@Override
	public synchronized int getZOrder() {

		return super.getZOrder();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#hasAncestor(com.jme.scene.Node)
	 */
	@Override
	public synchronized boolean hasAncestor(Node ancestor) {

		return super.hasAncestor(ancestor);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#isCollidable()
	 */
	@Override
	public synchronized boolean isCollidable() {

		return super.isCollidable();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#isCollidable(int)
	 */
	@Override
	public synchronized boolean isCollidable(int requiredOnBits) {

		return super.isCollidable(requiredOnBits);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#localToWorld(com.jme.math.Vector3f, com.jme.math.Vector3f)
	 */
	@Override
	public synchronized Vector3f localToWorld(Vector3f in, Vector3f store) {

		return super.localToWorld(in, store);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#lock()
	 */
	@Override
	public synchronized void lock() {

		super.lock();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#lock(com.jme.renderer.Renderer)
	 */
	@Override
	public synchronized void lock(Renderer r) {

		super.lock(r);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#lockBranch()
	 */
	@Override
	public synchronized void lockBranch() {

		super.lockBranch();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#lockMeshes()
	 */
	@Override
	public synchronized void lockMeshes() {

		super.lockMeshes();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#lookAt(com.jme.math.Vector3f, com.jme.math.Vector3f, boolean)
	 */
	@Override
	public synchronized void lookAt(Vector3f position, Vector3f upVector,
			boolean takeParentInAccount) {

		super.lookAt(position, upVector, takeParentInAccount);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#lookAt(com.jme.math.Vector3f, com.jme.math.Vector3f)
	 */
	@Override
	public synchronized void lookAt(Vector3f position, Vector3f upVector) {

		super.lookAt(position, upVector);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#matches(java.lang.Class, java.lang.String)
	 */
	@Override
	public synchronized boolean matches(Class<? extends Spatial> spatialSubclass,
			String nameRegex) {

		return super.matches(spatialSubclass, nameRegex);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#matches(java.lang.String)
	 */
	@Override
	public synchronized boolean matches(String nameRegex) {

		return super.matches(nameRegex);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#onDraw(com.jme.renderer.Renderer)
	 */
	@Override
	public synchronized void onDraw(Renderer r) {

		super.onDraw(r);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#propagateBoundToRoot()
	 */
	@Override
	public synchronized void propagateBoundToRoot() {

		super.propagateBoundToRoot();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#propagateStatesFromRoot(java.util.Stack[])
	 */
	@Override
	public synchronized void propagateStatesFromRoot(Stack[] states) {

		super.propagateStatesFromRoot(states);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#removeController(com.jme.scene.Controller)
	 */
	@Override
	public synchronized boolean removeController(Controller controller) {

		return super.removeController(controller);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#removeController(int)
	 */
	@Override
	public synchronized Controller removeController(int index) {

		return super.removeController(index);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#removeFromParent()
	 */
	@Override
	public synchronized boolean removeFromParent() {

		return super.removeFromParent();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#removeUserData(java.lang.String)
	 */
	@Override
	public synchronized Savable removeUserData(String key) {

		return super.removeUserData(key);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#rotateUpTo(com.jme.math.Vector3f)
	 */
	@Override
	public synchronized void rotateUpTo(Vector3f newUp) {

		super.rotateUpTo(newUp);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#setCullHint(com.jme.scene.Spatial.CullHint)
	 */
	@Override
	public synchronized void setCullHint(CullHint hint) {

		super.setCullHint(hint);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#setIsCollidable(boolean)
	 */
	@Override
	public synchronized void setIsCollidable(boolean isCollidable) {

		super.setIsCollidable(isCollidable);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#setLastFrustumIntersection(com.jme.renderer.Camera.FrustumIntersect)
	 */
	@Override
	public synchronized void setLastFrustumIntersection(FrustumIntersect intersects) {

		super.setLastFrustumIntersection(intersects);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#setLightCombineMode(com.jme.scene.Spatial.LightCombineMode)
	 */
	@Override
	public synchronized void setLightCombineMode(LightCombineMode mode) {

		super.setLightCombineMode(mode);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#setLocalRotation(com.jme.math.Matrix3f)
	 */
	@Override
	public synchronized void setLocalRotation(Matrix3f rotation) {

		super.setLocalRotation(rotation);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#setLocalRotation(com.jme.math.Quaternion)
	 */
	@Override
	public synchronized void setLocalRotation(Quaternion quaternion) {

		super.setLocalRotation(quaternion);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#setLocalScale(float)
	 */
	@Override
	public synchronized void setLocalScale(float localScale) {

		super.setLocalScale(localScale);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#setLocalScale(com.jme.math.Vector3f)
	 */
	@Override
	public synchronized void setLocalScale(Vector3f localScale) {

		super.setLocalScale(localScale);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#setLocalTranslation(float, float, float)
	 */
	@Override
	public synchronized void setLocalTranslation(float x, float y, float z) {

		super.setLocalTranslation(x, y, z);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#setLocalTranslation(com.jme.math.Vector3f)
	 */
	@Override
	public synchronized void setLocalTranslation(Vector3f localTranslation) {

		super.setLocalTranslation(localTranslation);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#setLocks(int, com.jme.renderer.Renderer)
	 */
	@Override
	public synchronized void setLocks(int locks, Renderer r) {

		super.setLocks(locks, r);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#setLocks(int)
	 */
	@Override
	public synchronized void setLocks(int lockedMode) {

		super.setLocks(lockedMode);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#setName(java.lang.String)
	 */
	@Override
	public synchronized void setName(String name) {

		super.setName(name);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#setNormalsMode(com.jme.scene.Spatial.NormalsMode)
	 */
	@Override
	public synchronized void setNormalsMode(NormalsMode mode) {

		super.setNormalsMode(mode);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#setParent(com.jme.scene.Node)
	 */
	@Override
	protected synchronized void setParent(Node parent) {

		super.setParent(parent);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#setRenderQueueMode(int)
	 */
	@Override
	public synchronized void setRenderQueueMode(int renderQueueMode) {

		super.setRenderQueueMode(renderQueueMode);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#setRenderState(com.jme.scene.state.RenderState)
	 */
	@Override
	public synchronized RenderState setRenderState(RenderState rs) {

		return super.setRenderState(rs);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#setTextureCombineMode(com.jme.scene.Spatial.TextureCombineMode)
	 */
	@Override
	public synchronized void setTextureCombineMode(TextureCombineMode mode) {

		super.setTextureCombineMode(mode);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#setUserData(java.lang.String, com.jme.util.export.Savable)
	 */
	@Override
	public synchronized void setUserData(String key, Savable data) {

		super.setUserData(key, data);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#setZOrder(int, boolean)
	 */
	@Override
	public synchronized void setZOrder(int zOrder, boolean setOnChildren) {

		super.setZOrder(zOrder, setOnChildren);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#setZOrder(int)
	 */
	@Override
	public synchronized void setZOrder(int zOrder) {

		super.setZOrder(zOrder);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#toString()
	 */
	@Override
	public synchronized String toString() {

		return super.toString();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#unlock()
	 */
	@Override
	public synchronized void unlock() {

		super.unlock();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#unlock(com.jme.renderer.Renderer)
	 */
	@Override
	public synchronized void unlock(Renderer r) {

		super.unlock(r);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#unlockBranch()
	 */
	@Override
	public synchronized void unlockBranch() {

		super.unlockBranch();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#unlockMeshes()
	 */
	@Override
	public synchronized void unlockMeshes() {

		super.unlockMeshes();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#updateGeometricState(float, boolean)
	 */
	@Override
	public synchronized void updateGeometricState(float time, boolean initiator) {

		super.updateGeometricState(time, initiator);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#updateRenderState()
	 */
	@Override
	public synchronized void updateRenderState() {

		super.updateRenderState();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#updateRenderState(java.util.Stack[])
	 */
	@Override
	protected synchronized void updateRenderState(Stack[] parentStates) {

		super.updateRenderState(parentStates);
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#updateWorldRotation()
	 */
	@Override
	protected synchronized void updateWorldRotation() {

		super.updateWorldRotation();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#updateWorldScale()
	 */
	@Override
	protected synchronized void updateWorldScale() {

		super.updateWorldScale();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#updateWorldTranslation()
	 */
	@Override
	protected synchronized void updateWorldTranslation() {

		super.updateWorldTranslation();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#updateWorldVectors()
	 */
	@Override
	public synchronized void updateWorldVectors() {

		super.updateWorldVectors();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#worldToLocal(com.jme.math.Vector3f, com.jme.math.Vector3f)
	 */
	@Override
	public synchronized Vector3f worldToLocal(Vector3f in, Vector3f store) {

		return super.worldToLocal(in, store);
	}

}
