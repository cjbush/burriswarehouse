package code.model.action.pallet;

import java.util.ArrayList;
import java.util.List;

import code.app.VirtualWarehouse;
import code.model.ModelLoader;
import code.model.SharedMeshManager;
import code.model.action.product.LargeProductBox;
import code.world.WarehouseWorld;

import com.jme.bounding.BoundingBox;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Quad;

/**
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 *
 */
public class Pallet extends Node {
	
	public static final String PALLET_MODEL_FOLDER = "data/models/pallets/main/";
	public static final String PALLET_MODEL = "palletMain.obj";
	
	public static final int MAX_PRODUCT = 2;
	
	private boolean canPickup;
	
	private Box frontCollisionMesh;
	private Box backCollisionMesh;
	private Box rightCollisionMesh;
	private Box leftCollisionMesh;
	
	private Node productNode;
	private boolean inUse = false;

	/**
	 * Creates a pallet.
	 * @param ww
	 * @param addProduct If false, an empty pallet will be created. If true, boxes of product will
	 * 						be added to the pallet.
	 * @param binNumber
	 * @param canPickup If the pallet can be picked up by a pallet jack.
	 */
	public Pallet(WarehouseWorld ww, boolean addProduct, String binNumber, boolean canPickup, boolean ensureProduct) {
		
		this.canPickup = canPickup;
		
		//give each pallet a unique name
		int num = ww.getPalletsList().size();
		setName("pallet"+num);
		
		loadModel(ww.getVirtualWarehouse().getSharedNodeManager(),ww);
		productNode = new Node("product node");
		attachChild(productNode);
		if (addProduct)
		{
			//add between 0 and x boxes of product to the pallet for randomness
			boolean boxAdded = false;
			for (int i=0; i<MAX_PRODUCT; i++)
			{
				int randomNumber = (int)(Math.random()*10); 
				if (randomNumber < 5)
				{
					addProductBox(ww, binNumber, num, i);
					boxAdded = true;
				}
			}
			if (ensureProduct && boxAdded == false)
			{
				addProductBox(ww, binNumber, num, 0);
			}
		}
		
		if (canPickup)
		{
			//setup special collision meshes for picking up pallets;
			//the pallet mesh itself is not collidable; instead, two
			//collision meshes are placed over two of the ends of the
			//pallet, so that the pallet jack can only enter from the
			//other two sides; collision meshes are also placed over
			//the other two ends, which use collision masks so that
			//they are not taken into account for collision detection,
			//but can still be collided with
						
			//ww.setChildrenCollidable(this, false);
			updateGeometricState(0, true);
			
			float xExtent = ((BoundingBox) this.getWorldBound()).xExtent;
			float yExtent = ((BoundingBox) this.getWorldBound()).yExtent;
			float zExtent = ((BoundingBox) this.getWorldBound()).zExtent;
			
			Vector3f center = new Vector3f(0,yExtent,0);
			frontCollisionMesh = new Box("collision mesh", center, xExtent, yExtent, .005f);
			backCollisionMesh = new Box("collision mesh", center, xExtent, yExtent, .005f);
			rightCollisionMesh = new Box("collision mesh", center, .005f, yExtent, zExtent);
			leftCollisionMesh = new Box("collision mesh", center, .005f, yExtent, zExtent);
			
			attachChild(frontCollisionMesh);
			attachChild(backCollisionMesh);
			attachChild(rightCollisionMesh);
			attachChild(leftCollisionMesh);
			
			//set a different collision mask since the player can go through
			//these parts; we don't want these to be collidable, but we still
			//want to be able to detect collisions with them to know when to
			//attach the pallet to the jack 
			rightCollisionMesh.setCollisionMask(2);
			leftCollisionMesh.setCollisionMask(2);
			
			frontCollisionMesh.setLocalTranslation(0, 0, zExtent+.025f);
			backCollisionMesh.setLocalTranslation(0, 0, -zExtent-.025f);
			rightCollisionMesh.setLocalTranslation(xExtent, 0, 0);
			leftCollisionMesh.setLocalTranslation(-xExtent, 0, 0);
			
			frontCollisionMesh.setModelBound(new BoundingBox());
			frontCollisionMesh.updateModelBound();
			backCollisionMesh.setModelBound(new BoundingBox());
			backCollisionMesh.updateModelBound();
			rightCollisionMesh.setModelBound(new BoundingBox());
			rightCollisionMesh.updateModelBound();
			leftCollisionMesh.setModelBound(new BoundingBox());
			leftCollisionMesh.updateModelBound();
			
			//don't show these objects
			frontCollisionMesh.setCullHint(CullHint.Always);
			backCollisionMesh.setCullHint(CullHint.Always);
			rightCollisionMesh.setCullHint(CullHint.Always);
			leftCollisionMesh.setCullHint(CullHint.Always);
		}
		
		setRenderQueueMode(Renderer.QUEUE_OPAQUE);
	}
	
	private void addProductBox(WarehouseWorld ww, String binNumber, int num, int i) {
		LargeProductBox product = new LargeProductBox(ww, binNumber, "product"+i+"pallet"+num);
		updateGeometricState(0.0f, true);
		float heightOffset = (((BoundingBox) this.getWorldBound()).yExtent)*2;
		product.setLocalTranslation(0, heightOffset, 0);
		productNode.attachChild(product);
	}
	
	private void loadModel(SharedMeshManager smm, WarehouseWorld ww) {
		Spatial palletModel = null;
		palletModel = ModelLoader.loadModel("obj", PALLET_MODEL_FOLDER + PALLET_MODEL, PALLET_MODEL_FOLDER, smm, true, ww.getVirtualWarehouse().getDisplay().getRenderer(), "object");
		attachChild(palletModel);
	}
	
	public boolean canBePickedUp() {
		return canPickup;
	}
	
	public Spatial getPickupCollisionMesh1() {
		return rightCollisionMesh;
	}
	
	public Spatial getPickupCollisionMesh2() {
		return leftCollisionMesh;
	}
	
	public List<Spatial> getProducts() {
		return productNode.getChildren();
	}
	
	public Node getProductNode() {
		return productNode;
	}
	
	public boolean isInUse() {
		return inUse;
	}

	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}
	
	//TODO: make sure to unlock pallet before moving
}
