package code.model.product;

import code.app.VirtualWarehouse;
import code.model.ModelLoader;
import code.model.SharedMeshManager;
import code.world.WarehouseWorld;

import com.jme.scene.Node;
import com.jme.scene.Spatial;

/**
 * The large boxes found on the pallets on the racks in the warehouse.
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 *
 */
public class LargeProductBox extends Product {

	private String binNumber;
	private WarehouseWorld ww;
	
	public LargeProductBox(WarehouseWorld ww, String binNumber, String name) {
		this.ww = ww;
		this.setName(name);
		this.binNumber = binNumber;
		loadModel();
	}
	
	private void loadModel() {
		Spatial productModel = null;
		productModel = ModelLoader.loadModel("obj", "data/models/boxes/generic/box.obj",
				"data/models/boxes/generic/", ww.getVirtualWarehouse().getSharedNodeManager(), true);
		attachChild(productModel);
	}
	
	public Product pickSmallProduct() {
		Product smallBox = new SmallProductBox(ww.getVirtualWarehouse().getSharedNodeManager(), "SmallProduct");
		return smallBox;
	}

	public String getBinNumber() {
		return binNumber;
	}
	
	
}
