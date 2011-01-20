package code.model.pallet;

import java.util.ArrayList;
import java.util.List;

import code.app.VirtualWarehouse;
import code.model.ModelLoader;
import code.model.SharedMeshManager;
import code.model.product.LargeProductBox;
import code.world.WarehouseWorld;

import com.jme.bounding.BoundingBox;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

/**
 * A stack of empty pallets.
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 *
 */
public class PalletStack extends Node {
	
	public static final String PALLET_MODEL_FOLDER = "data/models/pallets/main/";
	public static final String PALLET_MODEL = "palletMain.obj";
	
	private Node productNode;
	
	/**
	 * A stack of pallets. They are just for visual effect to populate the warehouse, and cannot be interacted with by the player.
	 * @param ww
	 * @param count The number of pallets in the stack.
	 */
	public PalletStack(WarehouseWorld ww, int count) {
	
		//the y translation for the next pallet to be placed on the stack
		float nextHeight = 0;
		
		for (int i=0; i<count; i++)
		{
			Spatial palletModel = null;
			palletModel = ModelLoader.loadModel("obj", PALLET_MODEL_FOLDER + PALLET_MODEL, PALLET_MODEL_FOLDER, ww.getVirtualWarehouse().getSharedNodeManager(), true);
			palletModel.setLocalTranslation(0, nextHeight, 0);
			attachChild(palletModel);
			
			//get the height of the model so we can place the next one on top of it
			this.updateGeometricState(0.0f, true);
			nextHeight = (((BoundingBox) this.getWorldBound()).yExtent)*2;
		}
		
		setRenderQueueMode(Renderer.QUEUE_OPAQUE);
	}
	
}
